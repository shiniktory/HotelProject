package ua.nure.kozina.SummaryTask4.DB;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import java.util.Date;

import static org.junit.Assert.*;

public class TestRoomRequestManager {

    private static final RoomRequestManager MANAGER = new RoomRequestManager();
    private static RoomRequest request = new RoomRequest();
    private static final long DAY_MS = 24*60*60*1000;
    private static final long TESTED_USER_ID = 2;

    @BeforeClass
    public static void setUp() throws DBException {
        request.setUserId(TESTED_USER_ID);
        request.setPlaceCount(1);
        request.setRoomClass(new ApartmentManager().getApartmentClassById(1));
        request.setArrivalDate(new Date(System.currentTimeMillis() + DAY_MS));
        request.setLeavingDate(new Date(System.currentTimeMillis() + 2 * DAY_MS));
        request.setState(OrderState.NEW);
        MANAGER.makeRequest(request);
    }

    @Test
    public void testFindActiveRequests() throws DBException {
        assertNotEquals(0, MANAGER.findAllActiveRequests().size());
    }

    @Test
    public void testFindActiveRequestsByUserId() throws DBException {
        assertNotEquals(0, MANAGER.findAllActiveRequestsByUserId(TESTED_USER_ID));
    }

    @Test
    public void testFindActiveRequestsWithWrongUserId() throws DBException {
        assertEquals(0, MANAGER.findAllActiveRequestsByUserId(0).size());
    }

    @Test
    public void testGetRequestNull() throws DBException {
        assertNull(MANAGER.getRequest(0));
    }

    @Test
    public void testGetRequest() throws DBException {
        assertNotNull(MANAGER.getRequest(request.getId()));
    }

    @Test
    public void testUpdateRequest() throws DBException {
        request.setState(OrderState.CLOSED);
        MANAGER.updateRequest(request);
        RoomRequest updatedRequest = MANAGER.getRequest(request.getId());
        assertEquals(request.getState(), updatedRequest.getState());
    }

    @AfterClass
    public static void tearDown() throws DBException {
        MANAGER.deleteRequest(request);
    }
}
