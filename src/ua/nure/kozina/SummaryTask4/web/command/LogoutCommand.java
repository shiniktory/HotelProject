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

/**
 * The LogoutCommand performs a session invalidation and redirection to the authorization page.
 *
 * @author V. Kozina-Kravchenko
 */
class LogoutCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(LogoutCommand.class);

    /**
     * Constructs a new LogoutCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public LogoutCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("LogoutCommand started");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        LOGGER.debug("LogoutCommand finished");
        return Path.AUTHORIZATION;
    }
}
