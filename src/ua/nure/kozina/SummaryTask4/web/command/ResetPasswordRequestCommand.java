package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.ForgotPasswordQuery;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.EXPIRED_QUERY;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The ResetPasswordRequestCommand provides the check for user's token.
 *
 * @author V. Kozina-Kravchenko
 */
class ResetPasswordRequestCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(ResetPasswordRequestCommand.class);

    public ResetPasswordRequestCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("The ResetPasswordRequestCommand started");
        HttpSession session = request.getSession();
        String userToken = request.getParameter(TOKEN);
        UserManager manager = new UserManager();
        ForgotPasswordQuery query = manager.getQuery(userToken);
        LOGGER.trace(query);
        String path;
        if (query != null && System.currentTimeMillis() < query.getDateExpire().getTime()) {
            User user = manager.getUserByEMail(query.getEmail());
            session.setAttribute(TEMP_USER, user);
            session.setAttribute(QUERY, query);
            path = Path.RESET_PASSWORD_PAGE;
        } else {
            setRedirect(true);
            setErrorToSession(session);
            path = Path.AUTHORIZATION;
        }
        LOGGER.debug("The ResetPasswordRequestCommand finished");
        return path;
    }

    /**
     * Sets to the given session an error message that current request is
     * expired.
     *
     * @param session the session to set an error message
     */
    private void setErrorToSession(HttpSession session) {
        String language = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(language);
        String error = mu.getMessage(EXPIRED_QUERY);
        LOGGER.error(error);
        session.setAttribute(ERROR_MESSAGE_TYPE, error);
    }
}
