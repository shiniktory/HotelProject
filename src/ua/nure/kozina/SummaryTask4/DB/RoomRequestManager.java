package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.constants.DBColumns;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import static ua.nure.kozina.SummaryTask4.DB.DBManager.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The RoomRequestManager contains methods to manage a room requests.
 *
 * @author V. Kozina-Kravchenko
 */
public class RoomRequestManager {

    private static final Logger LOGGER = LogManager.getLogger(RoomRequestManager.class);

    /**
     * SQL queries.
     */
    private static final String SELECT_ALL_ACTIVE_REQUESTS = "SELECT r.*, ac.id class_id, ac.class FROM requests r INNER JOIN apartment_classes ac ON r.class_id=ac.id WHERE request_state=0";
    private static final String SELECT_ALL_ACTIVE_REQUESTS_BY_USER_ID = "SELECT r.*, ac.id class_id, ac.class FROM requests r INNER JOIN apartment_classes ac ON r.class_id=ac.id WHERE request_state=0 AND user_id=?";
    private static final String SELECT_REQUEST_BY_ID = "SELECT r.*, ac.id class_id, ac.class FROM requests r INNER JOIN apartment_classes ac ON r.class_id=ac.id WHERE r.id=?";
    private static final String ADD_REQUEST = "INSERT INTO requests VALUES (DEFAULT, ?,?,?,?,?,?)";
    private static final String UPDATE_REQUEST = "UPDATE requests SET request_state=? WHERE id=?";
    private static final String DELETE_REQUEST = "DELETE FROM requests WHERE id=?";

    /**
     * Returns a connection with database.
     *
     * @return a connection with database
     * @throws DBException
     */
    private Connection getConnection() throws DBException {
        return DBManager.getInstance().getConnection();
    }

    /**
     * Returns the list of all active requests.
     *
     * @return the list of all active requests
     * @throws DBException
     */
    public List<RoomRequest> findAllActiveRequests() throws DBException {
        List<RoomRequest> roomRequests = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_ACTIVE_REQUESTS);
            while (rs.next()) {
                roomRequests.add(getRequest(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ACTIVE_REQUESTS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ACTIVE_REQUESTS, e);
        } finally {
            close(conn, st, rs);
        }
        return roomRequests;
    }

    /**
     * Returns the list of all active orders with the specified user id.
     *
     * @param userId a user's id to find all active orders
     * @return the list of all active orders with the specified user id
     * @throws DBException
     */
    public List<RoomRequest> findAllActiveRequestsByUserId(long userId) throws DBException {
        List<RoomRequest> roomRequests = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_ALL_ACTIVE_REQUESTS_BY_USER_ID);
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                roomRequests.add(getRequest(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ACTIVE_USER_REQUESTS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ACTIVE_USER_REQUESTS, e);
        } finally {
            close(conn, pst, rs);
        }
        return roomRequests;
    }

    /**
     * Returns a request with the specified id.
     *
     * @param id a request id
     * @return a request with the specified id
     * @throws DBException
     */
    public RoomRequest getRequest(long id) throws DBException {
        RoomRequest roomRequest = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_REQUEST_BY_ID);
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                roomRequest = getRequest(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_REQUEST_BY_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_REQUEST_BY_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return roomRequest;
    }

    /**
     * Adds a new request into the database.
     *
     * @param roomRequest a room request to add into the database
     * @return true if room request added successfully
     * @throws DBException
     */
    public boolean makeRequest(RoomRequest roomRequest) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(ADD_REQUEST, Statement.RETURN_GENERATED_KEYS);
            int j = 0;
            pst.setLong(++j, roomRequest.getUserId());
            pst.setInt(++j, roomRequest.getPlaceCount());
            pst.setInt(++j, roomRequest.getRoomClass().getId());
            pst.setDate(++j, new Date(roomRequest.getArrivalDate().getTime()));
            pst.setDate(++j, new Date(roomRequest.getLeavingDate().getTime()));
            pst.setInt(++j, roomRequest.getState().ordinal());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                roomRequest.setId(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_ADD_REQUEST, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_ADD_REQUEST, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Updates a room request.
     *
     * @param roomRequest a room request to update
     * @throws DBException
     */
    public void updateRequest(RoomRequest roomRequest) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(UPDATE_REQUEST);
            pst.setInt(1, roomRequest.getState().ordinal());
            pst.setLong(2, roomRequest.getId());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_UPDATE_REQUEST, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_UPDATE_REQUEST, e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Deletes the specified room request from the database.
     *
     * @param roomRequest a room request to delete
     * @throws DBException
     */
    public void deleteRequest(RoomRequest roomRequest) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_REQUEST);
            pst.setLong(1, roomRequest.getId());
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
     * Extracts a room request from the result set.
     *
     * @param rs the result set to extract a room request from
     * @return a room request from the result set
     * @throws SQLException
     */
    private RoomRequest getRequest(ResultSet rs) throws SQLException, DBException {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setId(rs.getLong(DBColumns.ID));
        roomRequest.setPlaceCount(rs.getInt(DBColumns.PLACE_COUNT));
        roomRequest.setUserId(rs.getLong(DBColumns.USER_ID));
        roomRequest.setRoomClass(extractApartmentClass(rs));
        roomRequest.setState(OrderState.getStateById(rs.getInt(DBColumns.REQUEST_STATE_ID)));
        roomRequest.setArrivalDate(rs.getDate(DBColumns.ARRIVAL_DATE));
        roomRequest.setLeavingDate(rs.getDate(DBColumns.LEAVING_DATE));
        return roomRequest;
    }

    private ApartmentClass extractApartmentClass(ResultSet rs) throws SQLException {
        ApartmentClass ac = new ApartmentClass();
        ac.setId(rs.getInt(DBColumns.APARTMENT_CLASS));
        ac.setName(rs.getString(DBColumns.APARTMENT_CLASS_NAME));
        return ac;
    }
}
