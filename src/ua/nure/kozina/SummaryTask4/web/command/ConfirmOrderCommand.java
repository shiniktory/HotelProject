package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The ConfirmOrderCommand changes order state based on the type of operation performed by client.
 *
 * @author V. Kozina-Kravchenko
 */
class ConfirmOrderCommand extends Command{

    private static final Logger LOGGER = LogManager.getLogger(ConfirmOrderCommand.class);

    /**
     * Constructs a new ConfirmOrderCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public ConfirmOrderCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ConfirmOrderCommand started");
        HttpSession session = request.getSession();
        String room = request.getParameter(ROOM_NUMBER);
        int roomNumber = Integer.parseInt(room);
        LOGGER.trace("Room number: " + roomNumber);
        session.setAttribute(ROOM_NUMBER, roomNumber);
        User user = (User) session.getAttribute(USER);
        String path = Path.CLIENT_ORDER_LIST;
        if (user != null) {
            Order order = getOrder(user.getOrders(), roomNumber);
            if (order != null) {
                path = processOrder(order, request.getParameter(OPERATION));
            }
        }
        LOGGER.debug("ConfirmOrderCommand finished");
        return path;
    }

    /**
     * Returns an order from the specified orders list with the given room number.
     *
     * @param orders the list of orders
     * @param roomNumber the room number to find order with
     * @return an order from the specified orders list with the given room number
     */
    private Order getOrder(List<Order> orders, int roomNumber) {
        return orders.stream().filter(
                o -> o.getRoomNumber() == roomNumber).
                findAny().orElse(null);
    }

    /**
     * Returns path to the next page depends on the current order state and changes
     * this state according to the performed operation type.
     *
     * @param order the current order to process
     * @param operation the type of performed operation
     * @return path to the next page depends on the current order state
     * @throws DBException
     */
    private String processOrder(Order order, String operation) throws DBException {
        String path = Path.CLIENT_ORDER_LIST;
        if (OrderState.NEW == order.getState()) {
            path = processOrderState(operation, order);
            LOGGER.trace("Order state id changed to " + order.getState());
            new OrderManager().updateOrder(order);
        } else if (order.getState() == OrderState.CONFIRMED) {
            path = Path.CLIENT_BILL_COMMAND;
        }
        return path;
    }

    /**
     * Returns path to next page depends on specified operation and changes order state
     * respectively.
     *
     * @param operation the type of client's performed operation
     * @param order the order to change state
     * @return path to next page depends on specified operation
     */
    private String processOrderState(String operation, Order order) {
        String path = Path.CLIENT_ORDER_LIST;
        if (CONFIRM_OPERATION.equals(operation)) {
            order.setState(OrderState.CONFIRMED);
            path = Path.CLIENT_BILL_COMMAND;
        } else if (CANCEL_OPERATION.equals(operation)) {
            order.setState(OrderState.CLOSED);
        }
        return path;
    }
}
