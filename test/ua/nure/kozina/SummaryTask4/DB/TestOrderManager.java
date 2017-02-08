package ua.nure.kozina.SummaryTask4.DB;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestOrderManager {

    private static final OrderManager MANAGER = new OrderManager();
    private static Order order = new Order();
    private static final long DAY_MS = 24*60*60*1000;

    @BeforeClass
    public static void setUp() throws DBException {
        order.setUserId(2);
        order.setRoomNumber(201);
        order.setState(OrderState.NEW);
        order.setDateCreation(new Date());
        order.setArrivalDate(new Date(System.currentTimeMillis() + DAY_MS));
        order.setLeavingDate(new Date(System.currentTimeMillis() + 2*DAY_MS));
        order.setBill(500);
        MANAGER.makeOrder(order);
    }

    @Test
    public void testFindAllOrders() throws DBException {
        List<Order> orders = MANAGER.findAllOrders();
        assertNotEquals(0, orders.size());
    }

    @Test
    public void testFindAllOrdersWithWrongUserId() throws DBException {
        assertEquals(0, MANAGER.findAllActiveOrdersByUserId(0).size());
    }

    @Test
    public void testFindAllOrdersByUserId() throws DBException {
        assertNotEquals(0, MANAGER.findOrdersByUserId(2).size());
    }

    @Test
    public void testFindOrdersByState() throws DBException {
        assertNotEquals(0, MANAGER.findOrdersByState(OrderState.NEW.ordinal()));
    }

    @Test
    public void testFindAllActiveOrders() throws DBException {
        assertNotEquals(0, MANAGER.findAllActiveOrders());
    }

    @Test
    public void testFindAllActiveOrdersByUserId() throws DBException {
        assertNotNull(MANAGER.findAllActiveOrdersByUserId(2));
    }

    @Test
    public void testGetOrderWithWrongId() throws DBException {
        assertNull(MANAGER.getOrder(0));
    }

    @Test
    public void testGetOrder() throws DBException {
        assertNotNull(MANAGER.getOrder(1));
    }

    @Test
    public void testUpdateOrder() throws DBException {
        order.setState(OrderState.CONFIRMED);
        MANAGER.updateOrder(order);
        Order updatedOrder = MANAGER.getOrder(order.getId());
        assertEquals(order.getState(), updatedOrder.getState());
    }

    @AfterClass
    public static void tearDown() throws DBException {
        MANAGER.deleteOrder(order);
    }
}
