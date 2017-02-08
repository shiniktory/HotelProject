package ua.nure.kozina.SummaryTask4.web;


import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.ControllerServlet;
import ua.nure.kozina.SummaryTask4.constants.CommandNames;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.COMMAND;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;

public class TestControllerServlet {

    private static ControllerServlet cs;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static HttpSession session;

    @BeforeClass
    public static void setUp() {
        cs = new ControllerServlet();
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(session.getAttribute(LANGUAGE)).thenReturn("en");
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testInit() throws ServletException {
        cs.init();
    }

    @Test (expected = NullPointerException.class)
    public void testServiceWithNullSession() throws ServletException, IOException {
        cs.service(request, response);
    }

    @Test
    public void testServiceWithCommand() throws ServletException, IOException {
        when(request.getParameter(COMMAND)).
                thenReturn(CommandNames.LOGOUT_COMMAND);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        cs.service(request, response);
    }

    @Test
    public void testServiceWithCommandException() throws ServletException, IOException {
        when(request.getParameter(COMMAND)).
                thenReturn(CommandNames.ADMIN_ALL_ORDERS_COMMAND);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(Path.ADMIN_ALL_ORDERS_PAGE)).
                thenReturn(mock(RequestDispatcher.class));
        cs.service(request, response);
        verify(request).getRequestDispatcher(Path.ADMIN_ALL_ORDERS_PAGE);
    }

    @Test
    public void testDestroy() {
        cs.destroy();
    }

}
