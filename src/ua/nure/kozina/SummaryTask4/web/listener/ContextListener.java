package ua.nure.kozina.SummaryTask4.web.listener;

/**
 * The Context listener.
 *
 * @author V. Kozina-Kravchenko
 */

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.nure.kozina.SummaryTask4.DB.StatusInspector;
import ua.nure.kozina.SummaryTask4.util.MailSender;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@WebListener()
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    /**
     * The status inspector instance that checks order statuses.
     */
    private final StatusInspector statusInspector = new StatusInspector();

    /**
     * The name of Log4J configuration file.
     */
    private static final String LOG4J_CONFIGURATION = "log4j2.xml";

    /**
     * The names of initial parameters.
     */
    private static final String LOCALES_INIT_PARAMETER = "locales";
    private static final String MAILBOX_INIT_PARAMETER = "mailbox";

    public void contextInitialized(ServletContextEvent sce) {
        // Configure the logger
        try {
            PropertyConfigurator.configure(LOG4J_CONFIGURATION);
            LOGGER.debug("Logger initialized");
        } catch (Exception e) {
            System.out.println("Cannot initialize logger");
            System.out.println(e.getMessage());
        }
        // Start a status inspector
        statusInspector.checkOrderStatuses();

        // Get initial locales
        ServletContext context = sce.getServletContext();
        String localesStr = context.getInitParameter(LOCALES_INIT_PARAMETER);
        List<String> locales = getList(localesStr);
        LOGGER.trace("Locales: " + localesStr);
        context.setAttribute(LOCALES_INIT_PARAMETER, locales);

        // Get initial mail credentials and configure a MailSender
        String mailCredentials = context.getInitParameter(MAILBOX_INIT_PARAMETER);
        LOGGER.trace("Mail credentials: " + mailCredentials);
        List<String> credentials = getList(mailCredentials);
        MailSender.getInstance().setCredentials(credentials.get(0), credentials.get(1));
    }

    public void contextDestroyed(ServletContextEvent sce) {
        statusInspector.interrupt();
    }

    /**
     * Returns the list of parameter names created from the string containing all parameters.
     *
     * @param parametersString the string containing all parameters
     * @return the list of parameter names created from the string containing all parameters
     */
    private List<String> getList(String parametersString) {
        List<String> parameters = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(parametersString, " ");
        while (tokenizer.hasMoreElements()) {
            parameters.add(tokenizer.nextToken());
        }
        return parameters;
    }
}
