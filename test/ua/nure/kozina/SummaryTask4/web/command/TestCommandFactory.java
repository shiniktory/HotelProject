package ua.nure.kozina.SummaryTask4.web.command;

import org.junit.Test;
import ua.nure.kozina.SummaryTask4.constants.CommandNames;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestCommandFactory {

    @Test
    public void testInit() {
        new CommandFactory();
    }

    @Test
    public void testGetCommandNull() {
        assertNull(CommandFactory.getCommand(null));
    }

    @Test
    public void testGetCommand() {
        assertNotNull(CommandFactory.getCommand(CommandNames.BILL_COMMAND));
    }
}
