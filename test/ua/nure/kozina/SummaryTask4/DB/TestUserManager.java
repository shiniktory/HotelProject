package ua.nure.kozina.SummaryTask4.DB;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.entity.ForgotPasswordQuery;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;

import java.util.Date;

import static org.junit.Assert.*;

public class TestUserManager {

    private static final UserManager MANAGER = new UserManager();
    private static User user = new User();
    private static Feedback feedback = new Feedback();
    private static ForgotPasswordQuery query = new ForgotPasswordQuery();
    
    @BeforeClass
    public static void setUp() throws DBException {
        user.setLogin("testUser");
        user.setPassword("testPass");
        user.setEmail("test@test.com");
        user.setUserRole(Role.CLIENT);
        user.setFirstName("Test");
        user.setLastName("Test");
        MANAGER.addUser(user);

        feedback.setUser(user);
        feedback.setDateCreated(new Date());
        feedback.setText("Good");
        MANAGER.leaveFeedback(feedback);

        query.setEmail("test@test.com");
        query.setToken("token");
        query.setReset(false);
        query.setDateExpire(new Date());
        MANAGER.addForgotPasswordQuery(query);
    }
    
    @Test
    public void testFindAllUsers() throws DBException {
        assertNotEquals(0, MANAGER.findAllUsers());
    }

    @Test
    public void testGetUserWithEmptyLogin() throws DBException {
        assertNull(MANAGER.getUserByLogin(""));
    }

    @Test
    public void testGetUserByLogin() throws DBException {
        assertNotNull(MANAGER.getUserByLogin(user.getLogin()));
    }

    @Test
    public void testGetUserWithWrongId() throws DBException {
        assertNull(MANAGER.getUser(0));
    }

    @Test
    public void testGetUser() throws DBException {
        assertNotNull(MANAGER.getUser(user.getId()));
    }

    @Test
    public void testGetUserByEmail() throws DBException {
        assertNotNull(MANAGER.getUserByEMail(user.getEmail()));
    }

    @Test
    public void testFindAllFeedbacks() throws DBException {
        assertNotEquals(0, MANAGER.findAllFeedbacks().size());
    }

    @Test
    public void testFindFeedbacksByUserId() throws DBException {
        assertNotNull(MANAGER.getFeedbackByUserId(user.getId()));
    }

    @Test
    public void testGetEmptyForgotPasswordQuery() throws DBException {
        assertNull(MANAGER.getQuery(""));
    }

    @Test
    public void testGetForgotPasswordQuery() throws DBException {
        assertNotNull(MANAGER.getQuery("token"));
    }

    @Test (expected = DBException.class)
    public void testAddUserWithExistingLogin() throws DBException {
        MANAGER.addUser(user);
    }

    @Test
    public void testUpdateUser() throws DBException {
        user.setFirstName("TestNewName");
        MANAGER.updateUser(user);
        User updatedUser = MANAGER.getUser(user.getId());
        assertEquals(user.getFirstName(), updatedUser.getFirstName());
    }

    @Test
    public void testUpdateForgotPasswordQuery() throws DBException {
        query.setReset(true);
        MANAGER.updateForgotPasswordQuery(query);
    }

    @AfterClass
    public static void tearDown() throws DBException {
        MANAGER.deleteFeedback(feedback);
        MANAGER.deleteUser(user);
        MANAGER.deleteForgotPasswordQuery(query);
    }
}
