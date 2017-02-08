package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.constants.DBColumns;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;

import static ua.nure.kozina.SummaryTask4.DB.DBManager.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The ApartmentManager contains a methods for managing the hotel apartments and their categories.
 *
 * @author V. Kozina-Kravchenko
 */
public class ApartmentManager {

    private static final Logger LOGGER = LogManager.getLogger(ApartmentManager.class);

    /**
     * SQL queries.
     */
    private static final String SELECT_ALL_APARTMENT_CLASSES = "SELECT * FROM apartment_classes";
    private static final String SELECT_APARTMENT_CLASS_BY_ID = "SELECT * FROM apartment_classes WHERE id=?";
    private static final String SELECT_ALL_APARTMENTS = "SELECT * FROM hotel";
    private static final String SELECT_APARTMENTS_BY_CLASS_ID = "SELECT * FROM hotel WHERE class_id=?";
    private static final String SELECT_FREE_APARTMENTS_BY_CLASS_AND_PLACE_COUNT = "SELECT * FROM hotel WHERE class_id=? AND place_count=? AND state_id=0";
    private static final String SELECT_APARTMENTS_BY_ROOM_NUMBER = "SELECT * FROM hotel WHERE number=?";
    private static final String ADD_APARTMENT = "INSERT INTO hotel VALUES (?,?,?,?,?)";
    private static final String ADD_APARTMENT_CLASS = "INSERT INTO apartment_classes VALUES (DEFAULT, ?)";
    private static final String UPDATE_APARTMENT = "UPDATE hotel SET place_count=?, class_id=?, state_id=?, price=? WHERE number=?";
    private static final String DELETE_APARTMENT = "DELETE FROM hotel WHERE number=?";
    private static final String DELETE_APARTMENT_CLASS = "DELETE FROM apartment_classes WHERE id=?";

    /**
     * Returns a database connection.
     *
     * @return a database connection
     * @throws DBException if connection failed
     */
    private Connection getConnection() throws DBException {
        return DBManager.getInstance().getConnection();
    }

    /**
     * Returns the list of all apartment classes from database.
     *
     * @return the list of all apartment classes from database
     * @throws DBException
     */
    public List<ApartmentClass> findApartmentClasses() throws DBException {
        List<ApartmentClass> classes = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_APARTMENT_CLASSES);
            while (rs.next()) {
                classes.add(getApartmentClass(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_APARTMENT_CLASSES, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_APARTMENT_CLASSES, e);
        } finally {
            close(conn, st, rs);
        }
        return classes;
    }

    /**
     * Returns an apartment class with the specified id.
     *
     * @param id a required apartment class id
     * @return an apartment class with the specified id
     * @throws DBException
     */
    public ApartmentClass getApartmentClassById(int id) throws DBException {
        ApartmentClass ac = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_APARTMENT_CLASS_BY_ID);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                ac = getApartmentClass(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_APARTMENT_CLASS_BY_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_APARTMENT_CLASS_BY_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return ac;
    }

    /**
     * Returns the list of all hotel apartments.
     *
     * @return the list of all hotel apartments
     * @throws DBException
     */
    public List<Apartment> findAllApartments() throws DBException {
        List<Apartment> apartments = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_APARTMENTS);
            while (rs.next()) {
                apartments.add(getApartment(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_APARTMENTS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_APARTMENTS, e);
        } finally {
            close(conn, st, rs);
        }
        return apartments;
    }

    /**
     * Returns the list of all hotel apartments with the specified apartment class id.
     *
     * @param classId an apartment class id
     * @return the list of all hotel apartments with the specified apartment class id
     * @throws DBException
     */
    public List<Apartment> findAllApartmentsByClassId(int classId) throws DBException {
        List<Apartment> apartments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_APARTMENTS_BY_CLASS_ID);
            pst.setInt(1, classId);
            rs = pst.executeQuery();
            while (rs.next()) {
                apartments.add(getApartment(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_APARTMENTS_BY_CLASS_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_APARTMENTS_BY_CLASS_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return apartments;
    }

    /**
     * Returns the list of all hotel apartments with the specified apartment class id and
     * place count.
     *
     * @param classId    a required apartment class id
     * @param placeCount a required place count
     * @return the list of all hotel apartments with the specified apartment class id and
     * place count
     * @throws DBException
     */
    public List<Apartment> findRoomsByClassAndPlaceCount(int classId, int placeCount)
            throws DBException {
        List<Apartment> apartments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_FREE_APARTMENTS_BY_CLASS_AND_PLACE_COUNT);
            pst.setInt(1, classId);
            pst.setInt(2, placeCount);
            rs = pst.executeQuery();
            while (rs.next()) {
                apartments.add(getApartment(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_APARTMENTS_BY_CLASS_AND_PLACE_COUNT, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_APARTMENTS_BY_CLASS_AND_PLACE_COUNT, e);
        } finally {
            close(conn, pst, rs);
        }
        return apartments;
    }

    /**
     * Returns an apartment with the specified room number.
     *
     * @param roomNumber a required room number
     * @return an apartment with the specified room number
     * @throws DBException
     */
    public Apartment getApartment(int roomNumber) throws DBException {
        Apartment apartment = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_APARTMENTS_BY_ROOM_NUMBER);
            pst.setInt(1, roomNumber);
            rs = pst.executeQuery();
            while (rs.next()) {
                apartment = getApartment(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_APARTMENT_BY_ROOM_NUMBER, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_APARTMENT_BY_ROOM_NUMBER, e);
        } finally {
            close(conn, pst, rs);
        }
        return apartment;
    }

    /**
     * Adds a new apartment into database.
     *
     * @param ap an apartment to add into database
     * @return true if apartment added successfully
     * @throws DBException
     */
    public boolean addApartment(Apartment ap) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(ADD_APARTMENT);
            int j = 0;
            pst.setInt(++j, ap.getRoomNumber());
            pst.setInt(++j, ap.getPlaceCount());
            pst.setInt(++j, ap.getApartmentClass().getId());
            pst.setInt(++j, ap.getState().ordinal());
            pst.setDouble(++j, ap.getPrice());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_ADD_APARTMENT, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_ADD_APARTMENT, e);
        } finally {
            close(pst);
            close(conn);
        }
        return true;
    }

    /**
     * Adds a new apartment class into the database.
     *
     * @param ac an apartment class to add into the database
     * @return true if apartment class added successfully
     * @throws DBException
     */
    public boolean addApartmentClass(ApartmentClass ac) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(ADD_APARTMENT_CLASS, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, ac.getName());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                ac.setId(rs.getInt(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_ADD_APARTMENT_CLASS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_ADD_APARTMENT_CLASS, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Updates a hotel apartment.
     *
     * @param ap an apartment to update
     * @throws DBException
     */
    public void updateApartment(Apartment ap) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(UPDATE_APARTMENT);
            int j = 0;
            pst.setInt(++j, ap.getPlaceCount());
            pst.setInt(++j, ap.getApartmentClass().getId());
            pst.setInt(++j, ap.getState().ordinal());
            pst.setDouble(++j, ap.getPrice());
            pst.setInt(++j, ap.getRoomNumber());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_UPDATE_APARTMENT, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_UPDATE_APARTMENT, e);
        } finally {
            close(pst);
            close(conn);
        }
    }


    /**
     * Deletes the specified apartment from the database.
     *
     * @param ap an apartment to delete
     * @throws DBException
     */
    public void deleteApartment(Apartment ap) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_APARTMENT);
            pst.setInt(1, ap.getRoomNumber());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(e);
            rollback(conn);
            throw new DBException(e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Deletes the specified apartment class from the database.
     *
     * @param ac an apartment class to delete
     * @throws DBException
     */
    public void deleteApartmentClass(ApartmentClass ac) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_APARTMENT_CLASS);
            pst.setInt(1, ac.getId());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(e);
            rollback(conn);
            throw new DBException(e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Extracts an apartment class from the result set.
     *
     * @param rs the result set to extract an apartment class from
     * @return an apartment class from the result set
     * @throws SQLException
     */
    private ApartmentClass getApartmentClass(ResultSet rs) throws SQLException {
        ApartmentClass ac = new ApartmentClass();
        ac.setId(rs.getInt(DBColumns.ID));
        ac.setName(rs.getString(DBColumns.APARTMENT_CLASS_NAME));
        return ac;
    }

    /**
     * Extract an apartment from the result set.
     *
     * @param rs the result set to extract an apartment from
     * @return an apartment from the result set
     * @throws SQLException
     * @throws DBException
     */
    private Apartment getApartment(ResultSet rs) throws SQLException, DBException {
        Apartment ap = new Apartment();
        ap.setRoomNumber(rs.getInt(DBColumns.ROOM_NUMBER));
        ap.setPlaceCount(rs.getInt(DBColumns.PLACE_COUNT));
        ap.setState(ApartmentState.getStateById(rs.getInt(DBColumns.APARTMENT_STATE_ID)));
        ap.setApartmentClass(getApartmentClassById(rs.getInt(DBColumns.APARTMENT_CLASS)));
        ap.setPrice(rs.getDouble(DBColumns.PRICE));
        return ap;
    }
}
