package ua.nure.kozina.SummaryTask4.web.tag;

import org.junit.Test;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestUserInfoTag {

    @Test(expected = NullPointerException.class)
    public void testUserInfoNullPointer() {
        UserInfo.userInfo(new User(), "ru");
    }

    @Test
    public void testUserInfoEmpty() {
        assertEquals("", UserInfo.userInfo(null, "ru"));
    }

    @Test
    public void testUserInfo() {
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Brown");
        user.setUserRole(Role.CLIENT);
        String info = UserInfo.userInfo(user, "ru");
        assertNotEquals("", info);
    }

    @Test
    public void testInit() {
        new UserInfo();
    }
}
