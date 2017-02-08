package ua.nure.kozina.SummaryTask4.web.tag;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.COMMON_ROLE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.USER;

/**
 * The PermissionTag class provides the permission check to access the current page.
 *
 * @author V. Kozina-Kravchenko
 */
public class PermissionTag extends TagSupport {

    private static final Logger LOGGER = LogManager.getLogger(PermissionTag.class);

    /**
     * The permission tag required role attribute.
     */
    private String role;

    /**
     * Returns a value of the role attribute.
     *
     * @return a value of the role attribute
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets a value of the role attribute.
     *
     * @param role a value of the role attribute
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int doStartTag() throws JspException {
        LOGGER.trace("Permission role is: " + role);
        HttpSession session = pageContext.getSession();
        User user = (User) session.getAttribute(USER);
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        String error = null;
        try {
            if (user == null) {
                session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(error));
                response.sendRedirect(Path.AUTHORIZATION);

            } else if (!role.equalsIgnoreCase(user.getUserRole().toString())) {
                if (!COMMON_ROLE.equalsIgnoreCase(role)) {
                    session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(error));
                    response.sendRedirect(Path.AUTHORIZATION);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return super.doStartTag();
    }
}
