package ua.nure.kozina.SummaryTask4.validator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.I_CAPTCHA;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.SECURITY_CODE;

/**
 * The DataValidator class contains methods for validation entered by users data.
 *
 * @author V. Kozina-Kravchenko
 */
public class DataValidator {

    private static final Logger LOGGER = LogManager.getLogger(DataValidator.class);
    /**
     * The string representations of user login, password and e-mail and
     * entered by user date patterns.
     */
    private static final String LOGIN_PATTERN = "[a-z]+[a-z|\\d]+";
    private static final String PASSWORD_PATTERN = "[\\p{L}|\\d]+";
    private static final String EMAIL_PATTERN = ".+[@].+";
    private static final String DATE_STRING_PATTERN = "\\d{4}-\\d{2}-\\d{2}";

    /**
     * Values of minimum and maximum length restrictions for such user data as
     * login, password, e-mail or name.
     */
    private static final int MIN_LOGIN_LENGTH = 4;
    private static final int MAX_LOGIN_LENGTH = 25;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final int MIN_EMAIL_LENGTH = 8;
    private static final int MAX_EMAIL_LENGTH = 40;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 30;

    /**
     * The maximum text length entered by user as feedback.
     */
    private static final int MAX_FEEDBACK_LENGTH = 200;

    /**
     * Checks the specified login for compliance with requirements.
     *
     * @param login a user login to check
     * @return true if the given login is valid
     * @throws ApplicationException if the given login is not valid
     */
    public static boolean checkLogin(String login) throws ApplicationException {
        if (login != null && !login.isEmpty()) {
            if (login.length() < MIN_LOGIN_LENGTH || login.length() > MAX_LOGIN_LENGTH) {
                LOGGER.error(WRONG_LOGIN_LENGTH);
                throw new ApplicationException(WRONG_LOGIN_LENGTH);
            }
            User user = new UserManager().getUserByLogin(login.toLowerCase());
            if (user == null) {
                Pattern pattern = Pattern.compile(LOGIN_PATTERN);
                if (pattern.matcher(login.toLowerCase()).matches()) {
                    return true;
                } else {
                    LOGGER.error(LOGIN_DOES_NOT_MATCHES_PATTERN);
                    throw new ApplicationException(LOGIN_DOES_NOT_MATCHES_PATTERN);
                }
            } else {
                LOGGER.error(USER_IS_ALREADY_EXISTS);
                throw new ApplicationException(USER_IS_ALREADY_EXISTS);
            }
        } else {
            LOGGER.error(EMPTY_LOGIN);
            throw new ApplicationException(EMPTY_LOGIN);
        }
    }

    /**
     * Validates two passwords and checks them for equivalence.
     *
     * @param password1 the first password
     * @param password2 the second password
     * @return true if two passwords are valid and equivalent
     * @throws ApplicationException if one of passwords if not valid or both passwords are not
     *         equivalent
     */
    public static boolean checkPasswords(String password1, String password2)
            throws ApplicationException {
        checkPassword(password1);
        checkPassword(password2);
        if (password1.equals(password2)) {
            return true;
        } else {
            LOGGER.error(PASSWORDS_ARE_NOT_EQUAL);
            throw new ApplicationException(PASSWORDS_ARE_NOT_EQUAL);
        }
    }

    /**
     * Checks the specified password for compliance with requirements.
     *
     * @param password the password to check
     * @return true if password is valid
     * @throws ApplicationException if password is not valid
     */
    public static boolean checkPassword(String password) throws ApplicationException {
        if (password != null && !password.isEmpty()) {
            if (password.length() < MIN_PASSWORD_LENGTH ||
                password.length() > MAX_PASSWORD_LENGTH) {
                LOGGER.error(WRONG_PASSWORD_LENGTH);
                throw new ApplicationException(WRONG_PASSWORD_LENGTH);
            }
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            if (pattern.matcher(password).matches()) {
                return true;
            } else {
                LOGGER.error(PASSWORD_DOES_NOT_MATCHES_PATTERN);
                throw new ApplicationException(PASSWORD_DOES_NOT_MATCHES_PATTERN);
            }
        } else {
            LOGGER.error(EMPTY_PASSWORD);
            throw new ApplicationException(EMPTY_PASSWORD);
        }
    }

    /**
     * Checks the specified email address for compliance with the requirements.
     *
     * @param email an email address to check
     * @return true if the specified email is valid
     * @throws ApplicationException if the specified email is not valid
     */
    public static boolean checkEMail(String email) throws ApplicationException {
        if (email != null && !email.isEmpty()) {
            if (email.length() < MIN_EMAIL_LENGTH || email.length() > MAX_EMAIL_LENGTH) {
                LOGGER.error(WRONG_EMAIL_LENGTH);
                throw new ApplicationException(WRONG_EMAIL_LENGTH);
            }
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            if (pattern.matcher(email).matches()) {
                User user = new UserManager().getUserByEMail(email);
                if (user == null) {
                    return true;
                } else {
                    LOGGER.error(USER_WITH_SUCH_EMAIL_IS_ALREADY_EXISTS);
                    throw new ApplicationException(USER_WITH_SUCH_EMAIL_IS_ALREADY_EXISTS);
                }
            } else {
                LOGGER.error(EMAIL_DOES_NOT_MATCHES_PATTERN);
                throw new ApplicationException(EMAIL_DOES_NOT_MATCHES_PATTERN);
            }
        } else {
            LOGGER.error(EMPTY_EMAIL);
            throw new ApplicationException(EMPTY_EMAIL);
        }
    }

    /**
     * Checks the specified name for compliance with the requirements.
     *
     * @param name the name to check
     * @return true if the name is valid
     * @throws ApplicationException if the specified name is not valid
     */
    public static boolean checkName(String name) throws ApplicationException {
        if (name != null && !name.isEmpty()) {
            if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
                LOGGER.error(WRONG_NAME_LENGTH);
                throw new ApplicationException(WRONG_NAME_LENGTH);
            }
            return true;
        } else {
            LOGGER.error(EMPTY_NAME);
            throw new ApplicationException(EMPTY_NAME);
        }
    }

    /**
     * Checks the specified date for compliance with the requirements.
     *
     * @param date the date string to check
     * @return true if the specified date is valid
     * @throws ApplicationException if the specified date is not valid
     */
    public static boolean checkDate(String date) throws ApplicationException {
        if (date != null && !date.isEmpty()) {
            Pattern pattern = Pattern.compile(DATE_STRING_PATTERN);
            if (pattern.matcher(date).matches()) {
                return true;
            } else {
                LOGGER.error(DATE_DOES_NOT_MATCHES_PATTERN);
                throw new ApplicationException(DATE_DOES_NOT_MATCHES_PATTERN_WITH_CORRECT);
            }
        } else {
            LOGGER.error(EMPTY_DATE);
            throw new ApplicationException(EMPTY_DATE);
        }
    }

    /**
     * Checks two given dates for compliance with the requirements.
     *
     * @param arrivalDate the date when user arrive
     * @param leavingDate the date when user leave the room
     * @return true if two dates are valid
     * @throws ApplicationException if two dates are not valid
     */
    public static boolean verifyDates(Date arrivalDate, Date leavingDate)
            throws ApplicationException {
        long arrivalTime = arrivalDate.getTime();
        long leavingTime = leavingDate.getTime();
        if (arrivalTime < leavingTime) {
            long currentTime = System.currentTimeMillis();
            if (arrivalTime > currentTime && leavingTime > currentTime) {
                return true;
            } else {
                LOGGER.error(DATE_HAS_ALREADY_PASSED);
                throw new ApplicationException(DATE_HAS_ALREADY_PASSED);
            }
        } else {
            LOGGER.error(ARRIVAL_DATE_IS_GREATER_LEAVING);
            throw new ApplicationException(ARRIVAL_DATE_IS_GREATER_LEAVING);
        }
    }

    /**
     * Checks the room number for null or empty value.
     *
     * @param roomNumber the room number to check
     * @return true if room number value is valid
     * @throws ApplicationException if room number value is empty
     */
    public static boolean checkRoomNumber(String roomNumber) throws ApplicationException {
        if (roomNumber != null && !roomNumber.isEmpty()) {
            return true;
        } else {
            LOGGER.error(EMPTY_ROOM_NUMBER);
            throw new ApplicationException(EMPTY_ROOM_NUMBER);
        }
    }

    /**
     * Checks the feedback for compliance with the requirements.
     *
     * @param feedback the feedback to check
     * @return true if the specified feedback is valid
     * @throws ApplicationException if the specified feedback is not valid
     */
    public static boolean checkFeedback(String feedback) throws ApplicationException {
        if (feedback == null || feedback.isEmpty()) {
            throw new ApplicationException(EMPTY_FEEDBACK);
        }
        if (feedback.length() > MAX_FEEDBACK_LENGTH ) {
            throw new ApplicationException(FEEDBACK_TOO_LONG);
        }
        return true;
    }

    /**
     * Checks captcha.
     *
     * @param request an information about HTTP request
     * @throws ApplicationException if captcha is not valid
     */
    public static void checkCaptcha(HttpServletRequest request) throws ApplicationException {
        String captcha = request.getParameter(SECURITY_CODE);
        if (captcha == null || captcha.isEmpty()) {
            throw new ApplicationException(EMPTY_CAPTCHA);
        } else {
            String originalCode = String.valueOf(request.getSession().
                    getAttribute(I_CAPTCHA));
            if (!captcha.equals(originalCode)) {
                throw new ApplicationException(CAPTCHA_FAILED);
            }
        }
    }
}
