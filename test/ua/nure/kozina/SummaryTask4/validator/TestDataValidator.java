package ua.nure.kozina.SummaryTask4.validator;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.I_CAPTCHA;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.SECURITY_CODE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.USER_ENTERED_DATE_FORMAT;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;


public class TestDataValidator extends Mockito {

    private static final Logger LOGGER = LogManager.getLogger(TestDataValidator.class);

    @BeforeClass
    public static void setUp() {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            ClientConnectionPoolDataSource ds = new ClientConnectionPoolDataSource();
            ds.setDatabaseName("ST4DB");
            ds.setUser("admin");
            ds.setPassword("pass");
            ds.setDataSourceName("ST4DB");

            ic.bind("java:comp/env/jdbc/ST4DB", ds);
        } catch (NamingException ex) {
            LOGGER.error(ex);
        }
    }

    @Test(expected = ApplicationException.class)
    public void testCheckLoginForNull() throws ApplicationException {
        checkLogin(null);
    }

    @Test(expected = ApplicationException.class)
    public void testCheckLoginWithWrongLength() throws ApplicationException {
        String wrongLengthLogin = "asd";
        checkLogin(wrongLengthLogin);
    }

    @Test(expected = ApplicationException.class)
    public void testCheckLoginExistingUser() throws ApplicationException {
        String login = "admin";
        checkLogin(login);
    }

    @Test(expected = ApplicationException.class)
    public void testCheckLoginWithWrongPattern() throws ApplicationException {
        String wrongPatternLogin = "sdd 23osld";
        checkLogin(wrongPatternLogin);
    }

    @Test
    public void testCheckLoginValid() throws ApplicationException {
        String login = "admin123";
        assertTrue(checkLogin(login));
    }

    @Test(expected = ApplicationException.class)
    public void testTwoPasswordsNotEqual() throws ApplicationException {
        String password1 = "123456";
        String password2 = "123451";
        checkPasswords(password1, password2);
    }

    @Test
    public void testTwoEqualPasswords() throws ApplicationException {
        String password1 = "123456";
        String password2 = "123456";
        assertTrue(checkPasswords(password1, password2));
    }

    @Test(expected = ApplicationException.class)
    public void testPasswordForNull() throws ApplicationException {
        checkPassword(null);
    }

    @Test(expected = ApplicationException.class)
    public void testPasswordWithWrongLength() throws ApplicationException {
        checkPassword("asd");
    }

    @Test(expected = ApplicationException.class)
    public void testPasswordWithWrongPattern() throws ApplicationException {
        checkPassword("asd asd");
    }

    @Test()
    public void testPasswordValid() throws ApplicationException {
        assertTrue(checkPassword("password1"));
    }

    @Test(expected = ApplicationException.class)
    public void testEmailForNull() throws ApplicationException {
        checkEMail(null);
    }

    @Test(expected = ApplicationException.class)
    public void testEmailForWrongLength() throws ApplicationException {
        checkEMail("asd@m.m");
    }

    @Test(expected = ApplicationException.class)
    public void testEmailWithWrongPattern() throws ApplicationException {
        checkEMail("this_is_email");
    }

    @Test(expected = ApplicationException.class)
    public void testEmailExist() throws ApplicationException {
        checkEMail("admin@m.ua");
    }

    @Test
    public void testEmailValid() throws ApplicationException {
        assertTrue(checkEMail("newEmail@mail.ua"));
    }

    @Test(expected = ApplicationException.class)
    public void testNameForNull() throws ApplicationException {
        checkName(null);
    }

    @Test(expected = ApplicationException.class)
    public void testNameWithWrongLength() throws ApplicationException {
        checkName("n");
    }

    @Test()
    public void testNameValid() throws ApplicationException {
        assertTrue(checkName("ян"));
    }

    @Test(expected = ApplicationException.class)
    public void testDateForNull() throws ApplicationException {
        checkDate(null);
    }

    @Test(expected = ApplicationException.class)
    public void testDateForPatternMatch() throws ApplicationException {
        checkDate("day-month-year");
    }

    @Test()
    public void testDateMatches() throws ApplicationException {
        assertTrue(checkDate("2017-01-01"));
    }

    @Test(expected = ApplicationException.class)
    public void testOrderDatesWrongOrder() throws ApplicationException {
        DateFormat format = new SimpleDateFormat(USER_ENTERED_DATE_FORMAT);
        try {
            verifyDates(new Date(), format.parse("2017-01-01"));
        } catch (ParseException e) {
            LOGGER.error(e);
        }
    }

    @Test(expected = ApplicationException.class)
    public void testOrderDatesPassed() throws ApplicationException {
        DateFormat format = new SimpleDateFormat(USER_ENTERED_DATE_FORMAT);
        try {
            verifyDates(format.parse("2017-01-01"), format.parse("2017-01-02"));
        } catch (ParseException e) {
            LOGGER.error(e);
        }
    }

    @Test()
    public void testOrderDatesValid() throws ApplicationException {
        DateFormat format = new SimpleDateFormat(USER_ENTERED_DATE_FORMAT);
        try {
            verifyDates(format.parse("2018-01-01"), format.parse("2018-01-02"));
        } catch (ParseException e) {
            LOGGER.error(e);
        }
    }

    @Test(expected = ApplicationException.class)
    public void testRoomNumberNull() throws ApplicationException {
        checkRoomNumber(null);
    }

    @Test()
    public void testRoomNumber() throws ApplicationException {
        assertTrue(checkRoomNumber("201"));
    }

    @Test(expected = ApplicationException.class)
    public void testFeedbackNull() throws ApplicationException {
        checkFeedback(null);
    }

    @Test(expected = ApplicationException.class)
    public void testFeedbackWrongLength() throws ApplicationException {
        String tooLongComment = "This is very long feedback!This is very long feedback!" +
                "This is very long feedback!This is very long feedback!This is very long feedback!" +
                "This is very long feedback!This is very long feedback!This is very long feedback!";
        checkFeedback(tooLongComment);
    }

    @Test()
    public void testFeedbackValid() throws ApplicationException {
        assertTrue(checkFeedback("Good feedback"));
    }

    @Test(expected = ApplicationException.class)
    public void testCaptchaNull() throws ApplicationException {
        HttpServletRequest request = null;
        try {
            request = Mockito.mock(HttpServletRequest.class);
            Mockito.when(request.getParameter(SECURITY_CODE)).thenReturn("");
        } catch (Exception e) {
            LOGGER.error(e);
        }
        checkCaptcha(request);
    }

    @Test(expected = ApplicationException.class)
    public void testCaptchaWrong() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(SECURITY_CODE)).thenReturn("1");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getSession().getAttribute(I_CAPTCHA)).thenReturn("0");

        checkCaptcha(request);
    }

}
