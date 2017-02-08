package ua.nure.kozina.SummaryTask4.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The MessageUtil class provides a functionality for localizing specified text.
 *
 * @author V. Kozina-Kravchenko
 */
public class MessageUtil {

    private static final Logger LOGGER = LogManager.getLogger(MessageUtil.class);

    /**
     * The name of resource bundle files.
     */
    private static final String RESOURCES = "messages";

    /**
     * The default locale value.
     */
    private static final String DEFAULT_LOCALE = "en";

    /**
     * The resource bundle instance configured for the specified locale.
     */
    private ResourceBundle bundle = null;

    /**
     * Constructs a new message utility instance and configures it for the specified
     * language.
     *
     * @param language the required language
     */
    public MessageUtil(String language) {
        String locale = DEFAULT_LOCALE;
        if (language != null && !language.isEmpty() && !"null".equals(language)) {
            locale = language;
        }
        bundle = ResourceBundle.getBundle(RESOURCES, new Locale(locale));
    }

    /**
     * Returns the message appropriate specified key and configured language.
     *
     * @param key a string key to get a message from the resource bundle
     * @return the message appropriate specified key and configured language
     */
    public String getMessage(String key) {
        LOGGER.trace("The resource key: " + key);
        String localizedMessage = key;
        try {
            if (localizedMessage != null) {
                localizedMessage = bundle.getString(key);
            }
        } catch (MissingResourceException e) {
            LOGGER.error(e);
        }
        LOGGER.trace("Localized message: " + localizedMessage);
        return localizedMessage;
    }
}
