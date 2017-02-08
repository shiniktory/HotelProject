package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.ForgotPasswordQuery;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;
import ua.nure.kozina.SummaryTask4.validator.DataValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The class performs a user's password change.
 *
 * @author V. Kozina-Kravchenko
 */
class ResetPasswordCommand extends Command {
    private static final Logger LOGGER = LogManager.getLogger(ResetPasswordCommand.class);

    /**
     * Constructs a new ResetPasswordCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public ResetPasswordCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ResetPasswordCommand started");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(TEMP_USER);
        ForgotPasswordQuery query = (ForgotPasswordQuery) session.getAttribute(QUERY);
        String path;
        if (user != null && query != null && !query.isReset()) {
            String password1 = request.getParameter(PASSWORD_1);
            String password2 = request.getParameter(PASSWORD_2);
            DataValidator.checkPasswords(password1, password2);
            user.setPassword(getHashedPassword(password1));
            UserManager manager = new UserManager();
            manager.updateUser(user);
            query.setReset(true);
            manager.updateForgotPasswordQuery(query);
            session.setAttribute(USER, user);
            path = checkUserRole(user.getUserRole());
        } else {
            setErrorToSession(session);
            path = Path.AUTHORIZATION;
        }
        LOGGER.debug("ResetPasswordCommand finished");
        return path;
    }

    /**
     * Returns the hashed string using MD5 algorithm for the specified password.
     *
     * @param password the password string to be hashed
     * @return the hashed password
     */
    private String getHashedPassword(String password) {
        StringBuffer hexPassword = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(password.getBytes());
            byte[] bytePassword = md.digest();
            for (int i = 0; i < bytePassword.length; i++) {
                String hex = Integer.toHexString(0xFF & bytePassword[i]);
                if (hex.length() == 1) {
                    hexPassword.append('0');
                }
                hexPassword.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e);
        }
        return hexPassword.toString();
    }

    /**
     * Returns a path to the next page depends on specified user role.
     *
     * @param userRole a role of the current logged user
     * @return a path to the next page depends on specified user role
     */
    private String checkUserRole(Role userRole) {
        if (Role.ADMIN == userRole) {
            return Path.ADMIN_ORDERS_AND_REQUESTS_COMMAND;
        }
        if (Role.CLIENT == userRole) {
            return Path.ROOM_LIST_COMMAND;
        }
        return Path.AUTHORIZATION;
    }

    /**
     * Sets to the specified session an error message that user not found.
     *
     * @param session the current session to set an error message
     */
    private void setErrorToSession(HttpSession session) {
        String language = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(language);
        String error = mu.getMessage(NO_USER_OR_QUERY_USED);
        LOGGER.error(error);
        session.setAttribute(ERROR_MESSAGE_TYPE, error);
    }
}
