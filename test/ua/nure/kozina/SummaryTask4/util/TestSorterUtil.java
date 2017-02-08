package ua.nure.kozina.SummaryTask4.util;


import org.junit.Test;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class TestSorterUtil {


    @Test
    public void testSortApartmentsByPrice() throws DBException {
        List<Apartment> apartments = new ApartmentManager().findAllApartmentsByClassId(1);
        Apartment roomWithMaxPrice = new ApartmentManager().getApartment(103);
        int indexBefore = apartments.indexOf(roomWithMaxPrice);
        SorterUtil.sortByPrice(apartments);
        int indexAfter = apartments.indexOf(roomWithMaxPrice);
        assertNotEquals(indexBefore, indexAfter);
    }

    @Test
    public void testSortApartmentsByPlaceCount() throws DBException {
        List<Apartment> apartments = new ApartmentManager().findAllApartmentsByClassId(1);
        Apartment roomWithMaxPlaceCount = new ApartmentManager().getApartment(103);
        int indexBefore = apartments.indexOf(roomWithMaxPlaceCount);
        SorterUtil.sortByPlaceCount(apartments);
        int indexAfter = apartments.indexOf(roomWithMaxPlaceCount);
        assertNotEquals(indexBefore, indexAfter);
    }

    @Test
    public void testSortApartmentsByClass() throws DBException {
        ApartmentManager manager = new ApartmentManager();
        List<Apartment> apartments = manager.findAllApartmentsByClassId(2);
        Apartment testRoom = manager.getApartment(101);
        apartments.add(testRoom);
        int indexBefore = apartments.indexOf(testRoom);
        SorterUtil.sortByClass(apartments);
        int indexAfter = apartments.indexOf(testRoom);
        assertNotEquals(indexBefore, indexAfter);
    }

    @Test
    public void testSortApartmentsByStatus() throws DBException {
        List<Apartment> apartments = new ApartmentManager().findAllApartmentsByClassId(1);
        int indexBefore = 0;
        Apartment testRoom = apartments.get(0);
        testRoom.setState(ApartmentState.UNAVAILABLE);
        SorterUtil.sortByStatus(apartments);
        int indexAfter = apartments.indexOf(testRoom);
        assertNotEquals(indexBefore, indexAfter);
    }

    @Test
    public void testSortFeedbacks() throws DBException {
        List<Feedback> feedbacks = new UserManager().findAllFeedbacks();
        Feedback testFeedback = feedbacks.get(0);
        int indexBefore = 0;
        SorterUtil.sortFeedbacksByDateCreated(feedbacks);
        int indexAfter = feedbacks.indexOf(testFeedback);
        assertNotEquals(indexBefore, indexAfter);
    }

    @Test
    public void testSortOrders() throws DBException {
        List<Order> orders = new OrderManager().findAllOrders();
        Order testOrder = orders.get(0);
        int indexBefore = 0;
        SorterUtil.setSortOrdersByDateCreated(orders);
        int indexAfter = orders.indexOf(testOrder);
        assertNotEquals(indexBefore, indexAfter);
    }

}
