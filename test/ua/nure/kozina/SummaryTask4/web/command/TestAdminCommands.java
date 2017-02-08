package ua.nure.kozina.SummaryTask4.web.command;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.stateAndRole.OrderState.*;

public class TestAdminCommands {
    private static HttpServletRequest request;
    private static HttpSession session;
    private static HttpServletResponse response;

    @BeforeClass
    public static void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testAdminAllOrdersCommand() throws ServletException, IOException, ApplicationException {
        Command command = new AdminAllOrdersCommand();
        String path = command.perform(request, response);
        verify(request, times(2)).setAttribute(STATES, OrderState.values());
        Assert.assertEquals(Path.ADMIN_ALL_ORDERS_PAGE, path);
    }

    @Test
    public void testAdminOrdersCommand() throws ServletException, IOException, ApplicationException {
        Command command = new AdminOrdersCommand();
        when(session.getAttribute(USER)).thenReturn(new User());
        command.perform(request, response);
        verify(request, times(1)).setAttribute(STATES, OrderState.values());
    }

    @Test
    public void testAdminChangeOrderStateCommand() throws ServletException, IOException, ApplicationException {
        Command command = new AdminChangeOrderStateCommand(true);
        long orderId = 1;
        when(request.getParameter(ORDER_ID)).thenReturn(String.valueOf(orderId));
        OrderState state = (new OrderManager().getOrder(1).getState() == CLOSED) ?
                NEW : CLOSED;
        when(request.getParameter(NEW_ORDER_STATE)).thenReturn(String.valueOf(state));
        command.perform(request, response);
        Order changedOrder = new OrderManager().getOrder(orderId);

        Assert.assertEquals(state, changedOrder.getState());
    }

}
