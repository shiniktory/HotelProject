package ua.nure.kozina.SummaryTask4.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestMessageUtil {

    @Test
    public void testInit() {
        String language = "en";
        new MessageUtil(language);
    }

    @Test
    public void testMessageReturnNull() {
        String result = new MessageUtil("en").getMessage(null);
        assertNull(result);
    }

    @Test
    public void testMessageReturnWrongKey() {
        String result = new MessageUtil("en").getMessage("admin");
        assertNotNull(result);
    }

    @Test
    public void testMessageReturnExistingKey() {
        String result = new MessageUtil("en").getMessage("role.admin");
        assertNotNull(result);
    }


}
