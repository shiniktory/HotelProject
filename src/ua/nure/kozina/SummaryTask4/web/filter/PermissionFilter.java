package ua.nure.kozina.SummaryTask4.web.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.NEED_AUTHORIZATION;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.COMMAND;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.USER;

/**
 * The PermissionFilter provides a security function to access the commands.
 *
 * @author V. Kozina-Kravchenko
 */
@WebFilter(filterName = "PermissionFilter")
public class PermissionFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(PermissionFilter.class);

    /**
     * The lists of commands divided by user roles to access.
     */
    private List<String> noUser = new ArrayList<>();
    private List<String> common = new ArrayList<>();
    private List<String> admin = new ArrayList<>();
    private List<String> client = new ArrayList<>();

    /**
     * The names of initial command categories parameters.
     */
    private static final String NO_USER = "no-user";
    private static final String COMMON = "common";
    private static final String ADMIN = "admin";
    private static final String CLIENT = "client";

    public void destroy() {
        LOGGER.debug("PermissionFilter destroyed");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        LOGGER.debug("PermissionFilter doFilter started");
        HttpServletRequest request = (HttpServletRequest) req;
        if (checkPermission(request)) {
            LOGGER.debug("PermissionFilter doFilter finished");
            chain.doFilter(req, resp);
        } else {
            HttpSession session = ((HttpServletRequest) req).getSession();
            String locale = String.valueOf(session.getAttribute(LANGUAGE));
            MessageUtil mu = new MessageUtil(locale);
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(NEED_AUTHORIZATION));
            LOGGER.error(mu.getMessage(NEED_AUTHORIZATION));
            ((HttpServletResponse) resp).sendRedirect(Path.AUTHORIZATION);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        LOGGER.debug("PermissionFilter init started");
        noUser = getList(config.getInitParameter(NO_USER));
        common = getList(config.getInitParameter(COMMON));
        admin = getList(config.getInitParameter(ADMIN));
        client = getList(config.getInitParameter(CLIENT));
        LOGGER.debug("PermissionFilter init finished");
    }

    /**
     * Returns a list of command names formed from the string.
     *
     * @param parametersString the string with all command names
     * @return a list of command names formed from the string
     */
    private List<String> getList(String parametersString) {
        List<String> parameters = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(parametersString, " ");
        while (tokenizer.hasMoreElements()) {
            parameters.add(tokenizer.nextToken());
        }
        return parameters;
    }

    /**
     * Returns the result of the permission check for the user associated with the current
     * session.
     *
     * @param request an information about HTTP request
     * @return the result of the permission check for the user associated with the current
     *         session
     */
    private boolean checkPermission(HttpServletRequest request) {
        LOGGER.debug("CheckPermission started");
        String command = request.getParameter(COMMAND);
        LOGGER.trace("The command is: " + command);
        if (command != null && !command.isEmpty()) {
            if (noUser.contains(command)) {
                return true;
            }
            HttpSession session = request.getSession(false);
            if (session == null) {
                return false;
            }
            User user = (User) session.getAttribute(USER);
            if (user == null) {
                return false;
            }
            Role userRole = user.getUserRole();
            if (userRole == Role.ADMIN) {
                return admin.contains(command) || common.contains(command);
            } else if (userRole == Role.CLIENT) {
                return client.contains(command) || common.contains(command);
            }
        }
        LOGGER.debug("CheckPermission finished");
        return false;
    }
}
