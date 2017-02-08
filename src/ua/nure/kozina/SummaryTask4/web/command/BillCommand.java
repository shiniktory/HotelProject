package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.ORDER;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.ROOM_NUMBER;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.USER;

/**
 * The BillCommand prepares information about current client's order to be paid.
 *
 * @author V. Kozina-Kravchenko
 */
class BillCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(BillCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("BillCommand started");
        HttpSession session = request.getSession();
        int roomNumber = (int) session.getAttribute(ROOM_NUMBER);
        LOGGER.trace("RoomNumber: " + roomNumber);
        User user = (User) session.getAttribute(USER);
        if (user != null) {
            List<Order> orders = user.getOrders();
            Order order = getOrder(orders, roomNumber);
            request.setAttribute(ORDER, order);
        }
        LOGGER.debug("BillCommand finished");
        return Path.BILL_PAGE;
    }

    /**
     * Returns clients order with specified room number.
     *
     * @param orders the list of clients orders
     * @param roomNumber the room number to find an appropriate order
     * @return an order with the specified room number
     */
    private Order getOrder(List<Order> orders, final int roomNumber) {
        return orders.stream().filter(o -> o.getRoomNumber() == roomNumber).
                findAny().orElse(null);
    }
}
