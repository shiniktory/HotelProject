package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The LoginCommand processes entered by user login data.
 *
 * @author V. Kozina-Kravchenko
 */
class LoginCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    /**
     * Constructs a new LoginCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public LoginCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ApplicationException {
        LOGGER.debug("Login command starts");
        HttpSession session = request.getSession();
        String path = Path.AUTHORIZATION;
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        try {
            String login = request.getParameter(LOGIN);
            String password = request.getParameter(PASSWORD);
            User user = getUser(login, password);
            LOGGER.trace("User: " + user);
            Role userRole = user.getUserRole();
            path = checkUserRole(userRole);
            session.setAttribute(USER, user);
        } catch (ApplicationException e) {
            String error = new MessageUtil(locale).getMessage(e.getMessage());
            LOGGER.error(error);
            session.setAttribute(ERROR_MESSAGE_TYPE, error);
        }
        LOGGER.debug("Login command finished");
        return path;
    }

    /**
     * Returns a current logged user who has the specified login.
     *
     * @param login    a current user login
     * @param password a current user password
     * @return a current logged user who has the specified login and password
     * @throws ApplicationException if specified parameters are not valid or
     *          no such user with the given parameters found
     */
    private User getUser(String login, String password) throws ApplicationException {
        if (login == null || password == null ||
                login.isEmpty() || password.isEmpty()) {
            throw new ApplicationException(EMPTY_LOGIN_OR_PASSWORD);
        }
        String hashedPassword = getHashedPassword(password);
        User user = new UserManager().getUserByLogin(login);
        if (user == null) {
            throw new ApplicationException(NO_USER_WITH_SUCH_LOGIN);
        } else if (!hashedPassword.equals(user.getPassword())) {
            throw new ApplicationException(WRONG_PASSWORD);
        }
        return user;
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
}
