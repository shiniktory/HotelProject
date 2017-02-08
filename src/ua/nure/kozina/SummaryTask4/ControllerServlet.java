package ua.nure.kozina.SummaryTask4;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;
import ua.nure.kozina.SummaryTask4.web.command.Command;
import ua.nure.kozina.SummaryTask4.web.command.CommandFactory;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.COMMAND;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;

/**
 * The ControllerServlet is the connecting link between the view and logical application parts.
 * The class functionality is to obtain the client's request data and process it.
 *
 * @author V. Kozina-Kravchenko
 */
@WebServlet
public class ControllerServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    /**
     * Obtains a command parameter from request and processes this command.
     *
     * @param request an information about HTTP request
     * @param response an information about HTTP response
     * @throws ServletException
     * @throws IOException
     */
    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.debug("Controller Servlet starts");
        String command = request.getParameter(COMMAND);
        LOGGER.trace("Command parameter: " + command);
        Command c = CommandFactory.getCommand(command);
        LOGGER.debug("Controller Servlet finished");
        performCommand(c, request, response);
    }

    /**
     * Performs the specified command and refers to the next page or command.
     *
     * @param command the requested command to perform
     * @param request an information about HTTP request
     * @param response an information about HTTP response
     * @throws ServletException
     * @throws IOException
     */
    private void performCommand(Command command, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = Path.ERROR_PAGE;
        HttpSession session = request.getSession();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        LOGGER.trace("Language from session: " + locale);
        MessageUtil mu = new MessageUtil(locale);
        if (command != null) {
            boolean isRedirect = command.isRedirect();
            try {
                path = command.perform(request, response);
            } catch (ApplicationException e) {
                String error = mu.getMessage(e.getMessage());
                LOGGER.error(e);
                if (isRedirect) {
                    session.setAttribute(ERROR_MESSAGE_TYPE, error);
                } else {
                    request.setAttribute(ERROR_MESSAGE_TYPE, error);
                }
            }
            LOGGER.trace("Path to the next page: " + path);
            if (isRedirect) {
                response.sendRedirect(path);
            } else {
                request.getRequestDispatcher(path).forward(request, response);
            }
        }
    }
}
