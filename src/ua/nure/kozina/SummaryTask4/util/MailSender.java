package ua.nure.kozina.SummaryTask4.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * The MailSender class designed for working with mail client. The class has methods for
 * sending email messages.
 *
 * @author V. Kozina-Kravchenko
 */
public class MailSender {

    private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

    /**
     * Tha values of message subjects.
     */
    private static final String FORGOT_PASSWORD_SUBJECT = "Password remind";
    private static final String INVOICE_SUBJECT = "Your order invoice";
    private static final String ORDER_CREATED_SUBJECT = "Order created";

    /**
     * The name of property file where stores email client settings.
     */
    private static final String PROPERTY_FILE = "mailSenderProperties.properties";

    /**
     * An instance of mail sender.
     */
    private static MailSender instance;

    /**
     * The value of email to send messages from.
     */
    private static String email;

    /**
     * The value of email password to send messages from.
     */
    private static String password;

    /**
     * The string representation of date format used in the messages.
     */
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    /**
     * The value of key property contains a link to remind password.
     */
    private static final String PASSWORD_REMIND_LINK_PROPERTY = "mail.change.address";

    /**
     * Constructs a new mail sender instance.
     */
    private MailSender() {
    }

    /**
     * Returns a configured mail sender instance.
     *
     * @return a configured mail sender instance
     */
    public static synchronized MailSender getInstance() {
        if (instance == null) {
            instance = new MailSender();
        }
        return instance;
    }

    /**
     * Sets a mailbox credentials.
     *
     * @param em   an email to send messages from
     * @param pass an email password
     */
    public void setCredentials(String em, String pass) {
        email = em;
        password = pass;
    }

    /**
     * Sends an email message to the specified email address with the given message subject
     * and text.
     *
     * @param email   an email address where to send message
     * @param subject a message subject value
     * @param message a message text
     * @throws MessagingException if an error occurs while sending the message
     */
    public void sendMessage(String email, String subject, String message)
            throws MessagingException {
        Message m = new MimeMessage(getSession());
        m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        m.setSubject(subject);
        m.setText(message);
        Transport.send(m);
    }

    /**
     * Sends a message to the specified user with password remind.
     *
     * @param user  the user to remind password
     * @param token a generated token to identify user
     * @throws ApplicationException if an error occurs while sending the message
     */
    public void sendForgotPasswordMessage(User user, String token) throws ApplicationException {
        String forgotMessage = getForgotMessage(user, token);
        try {
            sendMessage(user.getEmail(), FORGOT_PASSWORD_SUBJECT, forgotMessage);
        } catch (MessagingException e) {
            LOGGER.error(e);
            throw new ApplicationException(ErrorMessages.CANNOT_SEND_MESSAGE);
        }
    }

    /**
     * Returns a message text with password reminding created based on the specified user data.
     *
     * @param user  the user to reset password
     * @param token a generated token to identify user
     * @return a message text with password reminding created based on the specified user data
     */
    private String getForgotMessage(User user, String token) {
        String link = getProperties().getProperty(PASSWORD_REMIND_LINK_PROPERTY) + token;
        return "Hello, dear " + user.getLogin() + "!" + System.lineSeparator() +
                "To change your password follow this link: " + link + System.lineSeparator() +
                System.lineSeparator() +
                System.lineSeparator() + "Please, do not answer this message";
    }

    /**
     * Sends a message to the specified user with invoice created based on the given order.
     *
     * @param user  the user to send invoice
     * @param order an order to form an invoice based on
     */
    public void sendInvoiceMessage(User user, Order order) {
        String invoiceMessage = getInvoiceMessage(user, order);
        try {
            sendMessage(user.getEmail(), INVOICE_SUBJECT, invoiceMessage);
        } catch (MessagingException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Returns a message text with invoice information created based on the specified user
     * information and given user's order.
     *
     * @param user  the user to send invoice message
     * @param order an order to form invoice based on
     * @return a message text with invoice information created based on the specified user
     * information and given user's order
     */
    private String getInvoiceMessage(User user, Order order) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String invoiceMessage = "Hello, dear " + user.getLogin() + "!" + System.lineSeparator() +
                "You paid for staying at our hotel. Your order ¹" + order.getId() + "." +
                System.lineSeparator() + System.lineSeparator() + "INVOICE" + System.lineSeparator() +
                "-------------------------------------------" + System.lineSeparator() +
                "Order date: " + df.format(order.getDateCreation()) + System.lineSeparator() +
                "Bayer: " + user.getFirstName() + " " + user.getLastName() + System.lineSeparator() +
                "Hotel room number: " + order.getRoomNumber() + System.lineSeparator() +
                "Time to stay: " + df.format(order.getArrivalDate()) + " - " +
                df.format(order.getLeavingDate()) + System.lineSeparator() +
                "Invoice sum: " + order.getBill() + " UAH" + System.lineSeparator() +
                "Thank you for choosing us!" + System.lineSeparator() + System.lineSeparator() +
                "Please, do not answer this message";
        return invoiceMessage;
    }

    /**
     * Sends an information message to the specified user about an order creation.
     *
     * @param user the user to send message about order creation
     * @throws ApplicationException if an error occurs while sending the message
     */
    public void sendOrderCreatedMessage(User user) throws ApplicationException {
        String orderCreatedMessage = getOrderCreatedMessage(user);
        try {
            sendMessage(user.getEmail(), ORDER_CREATED_SUBJECT, orderCreatedMessage);
        } catch (MessagingException e) {
            LOGGER.error(e);
            throw new ApplicationException(ErrorMessages.CANNOT_SEND_MESSAGE, e);
        }
    }

    /**
     * Returns a message text about order creation formed based on the given user information.
     *
     * @param user the user to send a message
     * @return a message text about order creation formed based on the given user information
     */
    private String getOrderCreatedMessage(User user) {
        return "Hello, dear " + user.getLogin() + "!" + System.lineSeparator() +
                "You left a request for hotel room. Based on your request has been generated an order." +
                System.lineSeparator() + "Please check it in your personal cabinet on our site." +
                System.lineSeparator() + "Thank you for choosing us!" + System.lineSeparator() +
                System.lineSeparator() + "Please, do not answer this message";
    }

    /**
     * Returns a new mail session.
     *
     * @return a new mail session
     */
    private Session getSession() {
        return Session.getDefaultInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    /**
     * Returns a mail client properties loaded from property file.
     *
     * @return a mail client properties loaded from property file
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(PROPERTY_FILE);
            properties.load(input);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return properties;
    }
}
