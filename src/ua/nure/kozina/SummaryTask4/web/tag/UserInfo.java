package ua.nure.kozina.SummaryTask4.web.tag;

import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

/**
 * The UserInfo class represents a function for view an information about current logged user.
 *
 * @author V. Kozina-Kravchenko
 */
public class UserInfo {

    /**
     * Returns a string representation of information about current logged user.
     *
     * @param user the current logged user
     * @return a string representation of information about current logged user
     */
    public static String userInfo(User user, String language) {
        String userInfo = "";
        if (user != null) {
            String role = new MessageUtil(language).getMessage(user.getUserRole().getLocalized());
            userInfo = user.getFirstName() + " " + user.getLastName() +
                    " (" + role + ")";
        }
        return userInfo;
    }
}
