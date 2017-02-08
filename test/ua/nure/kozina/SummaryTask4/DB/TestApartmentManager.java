package ua.nure.kozina.SummaryTask4.DB;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;

import java.util.List;


import static org.junit.Assert.*;

public class TestApartmentManager {

    private static final ApartmentManager MANAGER = new ApartmentManager();
    private static Apartment apartment = new Apartment();
    private static ApartmentClass apartmentClass = new ApartmentClass();

    @BeforeClass
    public static void setUp() throws DBException {
        apartmentClass.setName("test");
        MANAGER.addApartmentClass(apartmentClass);

        apartment.setRoomNumber(500);
        apartment.setPlaceCount(1);
        apartment.setApartmentClass(apartmentClass);
        apartment.setPrice(500);
        apartment.setState(ApartmentState.FREE);
        MANAGER.addApartment(apartment);
    }

    @Test
    public void testFindAllApartmentClasses() throws DBException {
        List<ApartmentClass> classes = MANAGER.findApartmentClasses();
        assertNotEquals(0, classes.size());
    }

    @Test
    public void testGetApartmentClassByWrongId() throws DBException {
        assertNull(MANAGER.getApartmentClassById(0));
    }

    @Test
    public void testGetApartmentClassById() throws DBException {
        assertNotNull(MANAGER.getApartmentClassById(1));
    }

    @Test
    public void testFindAllApartments() throws DBException {
        List<Apartment> apartments = MANAGER.findAllApartments();
        assertNotEquals(0, apartments.size());
    }

    @Test
    public void testFindAllApartmentsByWrongClassId() throws DBException {
        List<Apartment> apartments = MANAGER.findAllApartmentsByClassId(0);
        assertEquals(0, apartments.size());
    }

    @Test
    public void testFindAllApartmentsByClassId() throws DBException {
        List<Apartment> apartments = MANAGER.findAllApartmentsByClassId(1);
        assertNotEquals(0, apartments.size());
    }

    @Test
    public void testFindRoomsByClassAndPlaceCount() throws DBException {
        List<Apartment> apartments = MANAGER.findRoomsByClassAndPlaceCount(1, 1);
        assertNotNull(apartments);
    }

    @Test
    public void testGetApartmentWithWrongNumber() throws DBException {
        assertNull(MANAGER.getApartment(0));
    }

    @Test
    public void testGetApartment() throws DBException {
        assertNotNull(MANAGER.getApartment(101));
    }

    @Test (expected = DBException.class)
    public void testAddWrongApartment() throws DBException {
        Apartment apartment = MANAGER.getApartment(101);
        MANAGER.addApartment(apartment);
    }

    @Test (expected = DBException.class)
    public void testAddWrongApartmentClass() throws DBException {
        ApartmentClass ac = MANAGER.getApartmentClassById(1);
        MANAGER.addApartmentClass(ac);
    }

    @Test
    public void testUpdateApartment() throws DBException {
        apartment.setState(ApartmentState.RESERVED);
        MANAGER.updateApartment(apartment);
        Apartment updatedApartment = MANAGER.getApartment(apartment.getRoomNumber());
        assertEquals(apartment.getState(), updatedApartment.getState());
    }

    @AfterClass
    public static void tearDown() throws DBException {
        MANAGER.deleteApartment(apartment);
        MANAGER.deleteApartmentClass(apartmentClass);
    }
}
