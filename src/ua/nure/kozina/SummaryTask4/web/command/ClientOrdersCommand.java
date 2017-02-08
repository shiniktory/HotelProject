package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.DB.RoomRequestManager;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.NEED_AUTHORIZATION;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The ClientOrdersCommand prepares an information about orders and requests of current logged
 * client.
 *
 * @author V. Kozina-Kravchenko
 */
class ClientOrdersCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(ClientOrdersCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ClientOrdersCommand started");
        HttpSession session = request.getSession();
        String path;
        User user = (User) session.getAttribute(USER);
        if (user != null) {
            setOrdersAndRequestsToSession(session, user);
            path = Path.CLIENT_CABINET;
        } else {
            path = Path.AUTHORIZATION;
            String locale = String.valueOf(session.getAttribute(LANGUAGE));
            MessageUtil mu = new MessageUtil(locale);
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(NEED_AUTHORIZATION));
        }
        LOGGER.debug("ClientOrdersCommand finished");
        return path;
    }

    /**
     * Sets an information about specified user's orders and requests to the session
     * associated with this user.
     *
     * @param session the current user session
     * @param user current logged user
     * @throws DBException
     */
    private void setOrdersAndRequestsToSession(HttpSession session, User user)
            throws DBException {
        List<Order> orders = new OrderManager().findAllActiveOrdersByUserId(user.getId());
        user.setOrders(orders);
        session.setAttribute(USER, user);
        List<RoomRequest> roomRequests = new RoomRequestManager().
                findAllActiveRequestsByUserId(user.getId());
        session.setAttribute(REQUESTS, roomRequests);
    }
}
