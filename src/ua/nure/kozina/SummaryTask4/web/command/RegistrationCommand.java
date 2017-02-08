package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;
import ua.nure.kozina.SummaryTask4.validator.DataValidator;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.REGISTRATION_SUCCESS;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The RegistrationCommand processes an entered by user registration data.
 *
 * @author V. Kozina-Kravchenko
 */
class RegistrationCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(RegistrationCommand.class);

    /**
     * Constructs a new RegistrationCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public RegistrationCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("RegistrationCommand started");
        HttpSession session = request.getSession(true);
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        User user = null;
        String path = Path.REGISTRATION;
        try {
            user = new User();
            setUserData(request, user);
            DataValidator.checkCaptcha(request);
            new UserManager().addUser(user);
            LOGGER.trace("New user id: " + user.getId());
            path = Path.AUTHORIZATION;
            session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(REGISTRATION_SUCCESS));
        } catch (ApplicationException e) {
            String error = mu.getMessage(e.getMessage());
            LOGGER.error(error);
            session.setAttribute(ERROR_MESSAGE_TYPE, error);
            session.setAttribute(TEMP_USER, user);
        }
        LOGGER.debug("RegistrationCommand finished");
        return path;
    }

    /**
     * Checks and sets an entered by user registration data to the specified user instance.
     *
     * @param request an information about HTTP request
     * @param user    a user instance to add an entered information to
     * @throws ApplicationException if entered by user data is not valid
     */
    private void setUserData(HttpServletRequest request, User user) throws ApplicationException {
        String login = request.getParameter(LOGIN);
        String password1 = request.getParameter(PASSWORD_1);
        String password2 = request.getParameter(PASSWORD_2);
        String email = request.getParameter(EMAIL);
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        user.setLogin(login);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        checkUserData(user, password1, password2);
    }

    private void checkUserData(User user, String password1, String password2) throws ApplicationException {
        final String setEmptyValue = "";
        try {
            checkLogin(user.getLogin());
        } catch (ApplicationException e) {
            user.setLogin(setEmptyValue);
            throw new ApplicationException(e.getMessage(), e);
        }
        checkPasswords(password1, password2);
        String hashedPassword = getHashedPassword(password1);
        user.setPassword(hashedPassword);
        try {
            checkEMail(user.getEmail());
        } catch (ApplicationException e) {
            user.setEmail(setEmptyValue);
            throw new ApplicationException(e.getMessage(), e);
        }
        try {
            checkName(user.getFirstName());
        } catch (ApplicationException e) {
            user.setFirstName(setEmptyValue);
            throw new ApplicationException(e.getMessage(), e);
        }
        try {
            checkName(user.getLastName());
        } catch (ApplicationException e) {
            user.setLastName(setEmptyValue);
            throw new ApplicationException(e.getMessage(), e);
        }
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
}
