package ua.nure.kozina.SummaryTask4.stateAndRole;

import org.junit.Test;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;


public class TestStatesAndRole {

    @Test
    public void testGetApartmentStateById() {
        assertNotNull(ApartmentState.getStateById(0));
    }

    @Test
    public void testApartmentStateGetLocalized() {
        MessageUtil mu = new MessageUtil("en");
        String enState = mu.getMessage(ApartmentState.FREE.getLocalized());
        mu = new MessageUtil("ru");
        String ruState = mu.getMessage(ApartmentState.FREE.getLocalized());
        assertNotEquals(enState, ruState);
    }

    @Test
    public void testGetOrderStateById() {
        assertNotNull(OrderState.getStateById(0));
    }

    @Test
    public void testOrderStateGetLocalized() {
        MessageUtil mu = new MessageUtil("en");
        String enState = mu.getMessage(OrderState.CLOSED.getLocalized());
        mu = new MessageUtil("ru");
        String ruState = mu.getMessage(OrderState.CLOSED.getLocalized());
        assertNotEquals(enState, ruState);
    }

    @Test
    public void testGetRoleById() {
        assertNotNull(Role.getRoleById(0));
    }
}
