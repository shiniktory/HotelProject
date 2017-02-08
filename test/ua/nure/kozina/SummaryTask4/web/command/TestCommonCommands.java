package ua.nure.kozina.SummaryTask4.web.command;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

public class TestCommonCommands {

    private static HttpServletRequest request;
    private static HttpServletResponse response;

    @BeforeClass
    public static void setUp() {

        request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testForgotPasswordCommandWithNullEmail() throws ServletException, IOException, ApplicationException {
        Command command = new ForgotPasswordCommand(true);
        when(request.getParameter(EMAIL)).thenReturn(null);
        String path = command.perform(request, response);
        Assert.assertEquals(Path.FORGOT_PASSWORD, path);
    }

    @Test
    public void testForgotPasswordCommandWithWrongEmail() throws ServletException, IOException, ApplicationException {
        Command command = new ForgotPasswordCommand(true);
        when(request.getParameter(EMAIL)).thenReturn("aaaa@aa.aa");
        String path = command.perform(request, response);
        Assert.assertEquals(Path.FORGOT_PASSWORD, path);
    }

    @Test
    public void testForgotPasswordCommandWithExistingEmail() throws ServletException, IOException, ApplicationException {
        Command command = new ForgotPasswordCommand(true);
        when(request.getParameter(EMAIL)).thenReturn("hanna@s.ua");
        String path = command.perform(request, response);
        Assert.assertEquals(Path.AUTHORIZATION, path);
    }

}
