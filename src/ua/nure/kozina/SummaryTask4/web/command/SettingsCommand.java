package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.PASSWORDS_ARE_NOT_EQUAL;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SETTINGS_CHANGED;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The SettingsCommand performs a user data changes.
 *
 * @author V. Kozina-Kravchenko
 */
class SettingsCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(SettingsCommand.class);

    /**
     * Constructs a new SettingsCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public SettingsCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("SettingsCommand started");
        HttpSession session = request.getSession();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        User user = (User) session.getAttribute(USER);
        try {
            if (changeUserData(request, user)) {
                new UserManager().updateUser(user);
                session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(SETTINGS_CHANGED));
            }
        } catch (ApplicationException e) {
            String error = mu.getMessage(e.getMessage());
            LOGGER.error(error);
            session.setAttribute(ERROR_MESSAGE_TYPE, error);
        }
        LOGGER.debug("SettingsCommand finished");
        return Path.SETTINGS;
    }

    /**
     * Obtains an entered user data, checks it for matching the requirements and sets changes to
     * the user instance.
     *
     * @param request an information about HTTP request
     * @param user    a current logged user who changes settings
     * @return true if some user data was changed
     * @throws ApplicationException if some entered user data is not valid
     */
    private boolean changeUserData(HttpServletRequest request, User user) throws ApplicationException {
        boolean isDataChanged = false;
        String firstName = request.getParameter(FIRST_NAME);
        if (checkName(firstName) && !user.getFirstName().equals(firstName)) {
            user.setFirstName(firstName);
            isDataChanged = true;
        }
        String lastName = request.getParameter(LAST_NAME);
        if (checkName(lastName) && !user.getLastName().equals(lastName)) {
            user.setLastName(lastName);
            isDataChanged = true;
        }
        String oldPassword = request.getParameter(OLD_PASSWORD);
        String password1 = request.getParameter(PASSWORD_1);
        String password2 = request.getParameter(PASSWORD_2);
        if (isAnyPasswordNotEmpty(oldPassword, password1, password2)) {
            if (arePasswordsValid(user, oldPassword, password1, password2)) {
                String hashedNewPassword = getHashedPassword(password1);
                user.setPassword(hashedNewPassword);
                isDataChanged = true;
            }
        }
        String email = request.getParameter(EMAIL);
        if (!user.getEmail().equals(email) && checkEMail(email)) {
            user.setEmail(email);
            isDataChanged = true;
        }
        return isDataChanged;
    }

    /**
     * Checks is any of password fields not empty.
     *
     * @param passwords an array of passwords to check
     * @return true if any password field is not empty
     */
    private boolean isAnyPasswordNotEmpty(String ... passwords) {
        for (String password : passwords) {
            if (password != null && !password.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates entered by user old, new and repeated new password.
     *
     * @param oldPassword an old password entered by user
     * @param password1   a new password
     * @param password2   a repeated new password
     * @return true if all passwords match the requirements
     * @throws ApplicationException id passwords are not valid
     */
    private boolean arePasswordsValid(User user, String oldPassword, String password1, String password2)
            throws ApplicationException {
        checkPassword(oldPassword);
        String hashedOldPassword = getHashedPassword(oldPassword);
        if (!hashedOldPassword.equals(user.getPassword())) {
            throw new ApplicationException(PASSWORDS_ARE_NOT_EQUAL);
        }
        checkPasswords(password1, password2);
        return !oldPassword.equals(password1);
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
