package ua.nure.kozina.SummaryTask4.util;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class TestMailSender {

    private static final MailSender sender = MailSender.getInstance();

    @BeforeClass
    public static void setUp() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("mailSenderProperties.properties");
        properties.load(input);
        String mail = properties.getProperty("mail.address");
        String pass = properties.getProperty("mail.password");
        sender.setCredentials(mail, pass);
    }

    @Test (expected = NullPointerException.class)
    public void testSendForgotPasswordMessageWithNullUser() throws ApplicationException {
        sender.sendForgotPasswordMessage(null, null);
    }

    @Test
    public void testSendForgotPasswordMessage() throws ApplicationException {
        User user = new User();
        user.setLogin("asdf");
        user.setEmail("asdf@asd.as");
        String token = "123456789";
        sender.sendForgotPasswordMessage(user, token);
    }

    @Test
    public void testSendInvoiceMessage() throws DBException {
        User user = new User();
        user.setLogin("asdf");
        user.setEmail("asdf@asd.as");
        Order order = new OrderManager().getOrder(1);
        sender.sendInvoiceMessage(user, order);
    }

    @Test
    public void testSendOrderCreatedMessage() throws ApplicationException {
        User user = new User();
        user.setLogin("asdf");
        user.setEmail("asdf@asd.as");
        sender.sendOrderCreatedMessage(user);
    }

}
