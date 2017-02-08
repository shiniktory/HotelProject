package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The LocaleCommand class performs Locale change.
 */
class LocaleCommand extends Command{

    private static final Logger LOGGER = LogManager.getLogger(LocaleCommand.class);

    /**
     * Constructs a new LocaleCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public LocaleCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("LocaleCommand started");
        HttpSession session = request.getSession(true);
        String localeName = request.getParameter(LANGUAGE);
        String returnPath = String.valueOf(session.getAttribute(CURRENT_PAGE));
        session.setAttribute(LANGUAGE, localeName);
        if (returnPath == null || returnPath.isEmpty() || "null".equals(returnPath) ) {
            returnPath = Path.AUTHORIZATION;
        }
        LOGGER.trace("Return path is: " + returnPath);
        return returnPath;
    }
}
