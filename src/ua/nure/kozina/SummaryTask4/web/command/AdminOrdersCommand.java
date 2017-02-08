package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.DB.RoomRequestManager;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
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
import java.util.*;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The AdminOrdersCommand prepares information about all active user orders and requests.
 *
 * @author V. Kozina-Kravchenko
 */
class AdminOrdersCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(AdminOrdersCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("AdminOrdersCommand started");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        if (user != null) {
            List<Order> orders = findAllActiveOrders();
            List<User> orderUsers = getUsersForOrders(orders);
            request.setAttribute(ORDERS, orders);
            request.setAttribute(ORDER_USERS, orderUsers);

            Map<RoomRequest, List<Apartment>> reqAndNumbers = getRequestsAndSuitableRooms();
            Map<RoomRequest, User> requestUsers = getRequestsWithUsers(reqAndNumbers);
            request.setAttribute(REQUESTS_AND_SUITABLE_NUMBERS, reqAndNumbers);
            request.setAttribute(REQUEST_USERS, requestUsers);
        }
        request.setAttribute(STATES, OrderState.values());
        LOGGER.debug("AdminOrdersCommand finished");
        return Path.ADMIN_ORDERS_AND_REQUESTS;
    }

    private Map<RoomRequest, User> getRequestsWithUsers(Map<RoomRequest, ?> reqAndNumbers)
            throws DBException {
        Map<RoomRequest, User>  map = new LinkedHashMap<>();
        UserManager manager = new UserManager();
        for (RoomRequest roomRequest : reqAndNumbers.keySet()) {
            map.put(roomRequest, manager.getUser(roomRequest.getUserId()));
        }
        return map;
    }

    private List<User> getUsersForOrders(List<Order> orders) throws DBException {
        List<User> orderUsers = new ArrayList<>();
        UserManager manager = new UserManager();
        for (Order order : orders) {
            orderUsers.add(manager.getUser(order.getUserId()));
        }
        return orderUsers;
    }

    /**
     * Returns the list of all active orders.
     *
     * @return the list of all active orders
     * @throws DBException
     */
    private List<Order> findAllActiveOrders() throws DBException {
        List<Order> orders= new ArrayList<>();
        OrderManager manager = new OrderManager();
        orders.addAll(manager.findOrdersByState(OrderState.NEW.ordinal()));
        orders.addAll(manager.findOrdersByState(OrderState.CONFIRMED.ordinal()));
        orders.addAll(manager.findOrdersByState(OrderState.PAID.ordinal()));
        return orders;
    }

    /**
     * Returns a container with room requests and associated with them lists of
     * suitable free room numbers.
     *
     * @return a container with room requests and associated with them lists of
     * suitable free room numbers
     * @throws DBException
     */
    private Map<RoomRequest, List<Apartment>> getRequestsAndSuitableRooms() throws DBException {
        List<RoomRequest> requestsList = new RoomRequestManager().findAllActiveRequests();
        LOGGER.trace("Requests list size: " + requestsList.size());
        Map<RoomRequest, List<Apartment>> requestsAndNumbers = new HashMap<>();
        ApartmentManager manager = new ApartmentManager();
        for (RoomRequest r : requestsList) {
            List<Apartment> suitableNumbers = manager.findRoomsByClassAndPlaceCount(
                    r.getRoomClass().getId(), r.getPlaceCount());
            requestsAndNumbers.put(r, suitableNumbers);
        }
        return requestsAndNumbers;
    }
}
