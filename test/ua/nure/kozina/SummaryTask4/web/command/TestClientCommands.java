package ua.nure.kozina.SummaryTask4.web.command;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.DB.RoomRequestManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.BILL_PAID;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

public class TestClientCommands {

    private static HttpServletRequest request;
    private static HttpSession session;
    private static HttpServletResponse response;
    private static final long DAY_MS = 24*60*60*1000;

    @BeforeClass
    public static void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testBillCommand() throws ServletException, IOException, ApplicationException {
        Command command = new BillCommand();

        when(session.getAttribute(USER)).thenReturn(new User());
        when(session.getAttribute(ROOM_NUMBER)).thenReturn(500);
        command.perform(request, response);
        verify(request).setAttribute(ORDER, null);
    }

    @Test
    public void testBillPayCommandWithWrongOrder() throws ServletException, IOException, ApplicationException {
        Command command = new BillPayCommand(true);
        when(session.getAttribute(USER)).thenReturn(new User());
        when(request.getParameter(ORDER_ID)).thenReturn("1");
        command.perform(request, response);
        MessageUtil mu = new MessageUtil("en");
        verify(session, never()).setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(BILL_PAID));
    }

    @Test
    public void testBillPayCommand() throws ServletException, IOException, ApplicationException {
        Command command = new BillPayCommand(true);
        when(session.getAttribute(USER)).thenReturn(new User());
        long orderId = makeNewConfirmedOrder();
        when(request.getParameter(ORDER_ID)).thenReturn(
                String.valueOf(orderId));
        command.perform(request, response);
        Order paidOrder = new OrderManager().getOrder(orderId);
        assertEquals(OrderState.PAID, paidOrder.getState());
        closeOrder(orderId);
    }

    private long makeNewConfirmedOrder() throws DBException {
        Order order = new Order();
        order.setUserId(2);
        order.setRoomNumber(101);
        order.setState(OrderState.CONFIRMED);
        order.setDateCreation(new Date());
        order.setArrivalDate(new Date( System.currentTimeMillis() + DAY_MS));
        order.setLeavingDate(new Date( System.currentTimeMillis() + 2*DAY_MS));
        order.setBill(400);
        new OrderManager().makeOrder(order);
        return order.getId();
    }

    private void closeOrder(long orderId) throws DBException {
        OrderManager manager = new OrderManager();
        Order order = manager.getOrder(orderId);
        manager.deleteOrder(order);
    }

    @Test
    public void testClientCancelRequestCommand() throws ServletException, IOException, ApplicationException {
        Command command = new ClientCancelRequestCommand(true);
        long reqId = makeNewRequest();
        when(request.getParameter(REQUEST_ID)).thenReturn(
                String.valueOf(reqId));
        command.perform(request, response);
        RoomRequest req = new RoomRequestManager().getRequest(reqId);
        assertEquals(OrderState.CLOSED, req.getState());
    }

    private long makeNewRequest() throws DBException {
        RoomRequest req = new RoomRequest();
        req.setUserId(2);
        req.setState(OrderState.NEW);
        req.setPlaceCount(1);
        req.setRoomClass(new ApartmentManager().getApartmentClassById(1));
        req.setArrivalDate(new Date(System.currentTimeMillis() + DAY_MS));
        req.setLeavingDate(new Date(System.currentTimeMillis() + 2 * DAY_MS));
        new RoomRequestManager().makeRequest(req);
        return req.getId();
    }

    @Test
    public void testClientOrdersCommandWithoutUser() throws ServletException, IOException, ApplicationException {
        Command command = new ClientOrdersCommand();
        when(session.getAttribute(USER)).thenReturn(null);
        String path = command.perform(request, response);
        assertEquals(Path.AUTHORIZATION, path);
    }

    @Test
    public void testClientOrdersCommandWithUser() throws ServletException, IOException, ApplicationException {
        Command command = new ClientOrdersCommand();
        when(session.getAttribute(USER)).thenReturn(new User());
        String path = command.perform(request, response);
        assertEquals(Path.CLIENT_CABINET, path);
    }

}
