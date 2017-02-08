package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.ForgotPasswordQuery;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.util.MailSender;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.PASSWORD_SENT;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.EMAIL;

/**
 * The ForgotPasswordCommand sends an email message with password reminder.
 *
 * @author V. Kozina-Kravchenko
 */
class ForgotPasswordCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(ForgotPasswordCommand.class);

    /**
     * Constructs a new ForgotPasswordCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public ForgotPasswordCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ForgotPasswordCommand started");
        HttpSession session = request.getSession();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        String path = Path.AUTHORIZATION;
        try {
            String email = request.getParameter(EMAIL);
            LOGGER.trace("An email to send password remind " + email);
            User user = getUser(email);
            processPasswordRemind(user);
            session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(PASSWORD_SENT));
        } catch (ApplicationException e) {
            path = Path.FORGOT_PASSWORD;
            String error = mu.getMessage(e.getMessage());
            LOGGER.error(error, e);
            session.setAttribute(ERROR_MESSAGE_TYPE, error);
        }
        LOGGER.debug("ForgotPasswordCommand finished");
        return path;
    }

    /**
     * Returns user with the specified email.
     *
     * @param email an email to get a user
     * @return user with the specified email
     * @throws ApplicationException if check failed or user not found
     */
    public User getUser(String email) throws ApplicationException {
        if (email == null || email.isEmpty()) {
            throw new ApplicationException(ErrorMessages.EMPTY_EMAIL);
        }
        User user = new UserManager().getUserByEMail(email.toLowerCase());
        if (user != null) {
            return user;
        } else {
            throw new ApplicationException(ErrorMessages.NO_USER_WITH_SUCH_EMAIL);
        }
    }

    /**
     * Creates a new forgot password query for the specified user and sends
     * an email with the link for password change.
     *
     * @param user the user to reset password
     * @throws ApplicationException
     */
    private void processPasswordRemind(User user) throws ApplicationException {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        ForgotPasswordQuery query = new ForgotPasswordQuery();
        query.setEmail(user.getEmail());
        query.setToken(token);
        query.setDateExpire(new Date(System.currentTimeMillis() + ForgotPasswordQuery.QUERY_UPTIME));
        query.setReset(false);
        if (new UserManager().addForgotPasswordQuery(query)) {
            MailSender.getInstance().sendForgotPasswordMessage(user, token);
        }
    }
}
