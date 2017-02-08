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
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.util.MailSender;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;
import ua.nure.kozina.SummaryTask4.validator.DataValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.REQUEST_ALREADY_CANCELED;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ROOM_IN_USAGE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.ORDER_FORMED;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.REQUEST_CANCELED;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

/**
 * The ProcessRequestCommand creates a new order based on the client's room request or cancels
 * if depends on the type of chosen operation.
 *
 * @author V. Kozina-Kravchenko
 */
class ProcessRequestCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(ProcessRequestCommand.class);

    /**
     * Constructs a new ProcessRequestCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public ProcessRequestCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ProcessRequestCommand started");
        HttpSession session = request.getSession();
        long requestId = Long.parseLong(request.getParameter(REQUEST_ID));
        LOGGER.trace("Request id: " + requestId);
        try {
            RoomRequest req = new RoomRequestManager().getRequest(requestId);
            if (req.getState() == OrderState.NEW) {
                String operationType = request.getParameter(OPERATION);
                performOperation(operationType, request, req);
            } else {
                throw new ApplicationException(REQUEST_ALREADY_CANCELED);
            }
        } catch (ApplicationException e) {
            String locale = String.valueOf(session.getAttribute(LANGUAGE));
            MessageUtil mu = new MessageUtil(locale);
            LOGGER.error(e);
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(e.getMessage()));
        }
        LOGGER.debug("ProcessRequestCommand finished");
        return Path.ADMIN_ORDERS_AND_REQUESTS_COMMAND;
    }

    /**
     * Performs a room request depends on the chosen operation type.
     *
     * @param operationType a chosen operation type
     * @param request       an information about HTTP request
     * @param req           a client's room request
     * @throws ApplicationException
     */
    private void performOperation(String operationType, HttpServletRequest request, RoomRequest req)
            throws ApplicationException {
        HttpSession session = request.getSession();
        LOGGER.trace("Admin operation type for request: " + operationType);
        if (CREATE_OPERATION.equals(operationType)) {
            String numberStr = request.getParameter(ROOM_NUMBER);
            DataValidator.checkRoomNumber(numberStr);
            int roomNumber = Integer.parseInt(numberStr);
            ApartmentManager apManager = new ApartmentManager();
            Apartment room = apManager.getApartment(roomNumber);
            performOrder(req, room, session);
        } else if (CANCEL_OPERATION.equals(operationType)) {
            closeRequest(req);
            String locale = String.valueOf(session.getAttribute(LANGUAGE));
            MessageUtil mu = new MessageUtil(locale);
            session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(REQUEST_CANCELED));
        }
    }

    /**
     * Creates a new order based on the specified room request if a chosen room number is free.
     *
     * @param req     a room request to create an order based on
     * @param room    a suitable for request room number
     * @param session a session associated with the current logged user
     * @throws ApplicationException if chosen room is already in usage or an error occurs
     *                              while perform an order
     */
    private void performOrder(RoomRequest req, Apartment room, HttpSession session)
            throws ApplicationException {
        if (ApartmentState.FREE == room.getState()) {
            Order order = makeOrderFromRequest(req, room);
            new OrderManager().makeOrder(order);
            LOGGER.trace("New order id: " + order.getId());
            closeRequest(req);
            sendInformMessage(order, session);
        } else {
            throw new ApplicationException(ROOM_IN_USAGE);
        }
    }

    /**
     * Creates a new order based on the given request and room number.
     *
     * @param roomRequest a room request to create a new order based on
     * @param room        a suitable room number to order
     * @return a new order created based on the given room request
     */
    private Order makeOrderFromRequest(RoomRequest roomRequest, Apartment room) {
        Order order = new Order();
        order.setUserId(roomRequest.getUserId());
        order.setRoomNumber(room.getRoomNumber());
        order.setBill(room.getPrice());
        order.setState(OrderState.NEW);
        order.setDateCreation(new Date());
        order.setArrivalDate(roomRequest.getArrivalDate());
        order.setLeavingDate(roomRequest.getLeavingDate());
        return order;
    }

    /**
     * Closes the specified user's room request.
     *
     * @param req a room request to close
     * @throws DBException
     */
    private void closeRequest(RoomRequest req) throws DBException {
        req.setState(OrderState.CLOSED);
        new RoomRequestManager().updateRequest(req);
    }

    /**
     * Sends an email massage to the specified user about successfully created order from request.
     *
     * @param order   an order to inform about
     * @param session a current logged user session
     * @throws ApplicationException
     */
    private void sendInformMessage(Order order, HttpSession session) throws ApplicationException {
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        User user = new UserManager().getUser(order.getUserId());
        MailSender.getInstance().sendOrderCreatedMessage(user);
        session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(ORDER_FORMED));
    }
}
