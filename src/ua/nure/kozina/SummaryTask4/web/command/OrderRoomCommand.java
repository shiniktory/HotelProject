package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.EMPTY_ROOM_NUMBER;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ROOM_IN_USAGE;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.ORDER_FORMED;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The OrderRoomCommand performs a new order creation based on the data entered by user.
 *
 * @author V. Kozina-Kravchenko
 */
class OrderRoomCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(OrderRoomCommand.class);

    /**
     * The count of milliseconds in one day.
     */
    private static final long MS_IN_ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * Constructs a new OrderRoomCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public OrderRoomCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("Order room command started");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        String path = Path.CLIENT_ORDER_LIST;
        try {
            if (user != null) {
                processOrderParameters(request, user);
                session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(ORDER_FORMED));
            }
        } catch (Exception e) {
            LOGGER.error(e);
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(e.getMessage()));
            path = Path.ROOM_LIST_COMMAND;
        }
        LOGGER.debug("Order room command finished");
        return path;
    }

    /**
     * Gets and verifies parameters from user request and creates a new order
     * if entered by user parameters are valid.
     *
     * @param request a request information for HTTP servlets
     * @param user a current logged user
     * @throws ApplicationException
     * @throws ParseException
     */
    private void processOrderParameters(HttpServletRequest request, User user)
            throws ApplicationException, ParseException {
        String arDateStr = request.getParameter(DATE_FROM);
        String lvDateStr = request.getParameter(DATE_TO);
        DateFormat format = new SimpleDateFormat(USER_ENTERED_DATE_FORMAT);
        String[] numbers = request.getParameterValues(ROOM_NUMBER);
        if (numbers == null) {
            throw new ApplicationException(EMPTY_ROOM_NUMBER);
        }
        for (String number : numbers) {
            checkRoomNumber(number);
            int roomNumber = Integer.parseInt(number);
            checkDate(arDateStr);
            Date arrivalDate = format.parse(arDateStr);
            checkDate(lvDateStr);
            Date leavingDate = format.parse(lvDateStr);
            verifyDates(arrivalDate, leavingDate);
            processOrder(roomNumber, user, arrivalDate, leavingDate);
        }
    }

    /**
     * Processes an order depends on chosen room state. If room is free make an order
     * based on the specified parameters entered by user.
     *
     * @param roomNumber a chosen room number
     * @param user a current logged user who made order
     * @param arrivalDate a date when user arrive
     * @param leavingDate a date when user leave the room
     * @throws ApplicationException if chosen room is in usage or entered parameters are
     *         not valid
     */
    private void processOrder(int roomNumber, User user, Date arrivalDate, Date leavingDate)
            throws ApplicationException {
        if (checkRoomFree(roomNumber)) {
            LOGGER.trace("Room number is FREE");
            OrderManager manager = new OrderManager();
            Order order = createOrder(user, roomNumber, new Date(), arrivalDate, leavingDate);
            manager.makeOrder(order);
            LOGGER.trace("Order id: " + order.getId());
        } else {
            throw new ApplicationException(ROOM_IN_USAGE);
        }
    }

    /**
     * Checks is chosen room free.
     *
     * @param roomNumber a chosen room number
     * @return a result of checking is chosen room free
     * @throws DBException
     */
    private boolean checkRoomFree(int roomNumber) throws DBException {
        Apartment room = new ApartmentManager().getApartment(roomNumber);
        return (room != null && ApartmentState.FREE == room.getState());
    }

    /**
     * Returns an order created based on the specified user parameters.
     *
     * @param user the current logged user who made order
     * @param roomNumber a chosen room number
     * @param currentDate a date when order creates
     * @param arrivalDate a date when user arrive
     * @param leavingDate a date when user leave the room
     * @return an order created based on the specified user parameters
     * @throws DBException
     */
    private Order createOrder(User user, int roomNumber, Date currentDate,
                              Date arrivalDate, Date leavingDate) throws DBException {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setRoomNumber(roomNumber);
        order.setState(OrderState.NEW);
        order.setDateCreation(currentDate);
        order.setArrivalDate(arrivalDate);
        order.setLeavingDate(leavingDate);
        double priceForOneDay = new ApartmentManager().getApartment(roomNumber).getPrice();
        int countOfDays = (int) ((leavingDate.getTime() - arrivalDate.getTime()) / MS_IN_ONE_DAY);
        order.setBill(priceForOneDay * countOfDays);
        return order;
    }


}
