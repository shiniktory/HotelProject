package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.constants.DBColumns;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import static ua.nure.kozina.SummaryTask4.DB.DBManager.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The OrderManager class provides a functionality to manage the orders.
 *
 * @author V. Kozina-Kravchenko
 */
public class OrderManager {

    private static final Logger LOGGER = LogManager.getLogger(OrderManager.class);

    /**
     * SQL queries.
     */
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT * FROM orders WHERE user_id=?";
    private static final String SELECT_ALL_ORDERS_BY_STATE_ID = "SELECT * FROM orders WHERE order_state=?";
    private static final String SELECT_ALL_ACTIVE_ORDERS = "SELECT * FROM orders WHERE order_state<>3";
    private static final String SELECT_ALL_ACTIVE_ORDERS_BY_USER_ID = "SELECT * FROM orders WHERE user_id=? AND order_state<>3";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id=?";
    private static final String ADD_ORDER = "INSERT INTO orders VALUES (DEFAULT,?,?,?,?,?,?,?)";
    private static final String UPDATE_ORDER = "UPDATE orders SET user_id=?, room_number=?, bill=?, order_state=?, arrival_date=?, leaving_date=? WHERE id=?";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id=?";

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
     * Returns the list of all orders.
     *
     * @return the list of all orders
     * @throws DBException
     */
    public List<Order> findAllOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_ORDERS);
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ORDERS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ORDERS, e);
        } finally {
            close(conn, st, rs);
        }
        return orders;
    }

    /**
     * Returns the list of all specified user's orders.
     *
     * @param userId a user id
     * @return the list of all specified user's orders
     * @throws DBException
     */
    public List<Order> findOrdersByUserId(long userId) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_ALL_ORDERS_BY_USER_ID);
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ORDERS_BY_USER_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ORDERS_BY_USER_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return orders;
    }

    /**
     * Returns the list of all orders with the specified state id.
     *
     * @param stateId an order state id
     * @return the list of all orders with the specified state id
     * @throws DBException
     */
    public List<Order> findOrdersByState(int stateId) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_ALL_ORDERS_BY_STATE_ID);
            pst.setInt(1, stateId);
            rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ORDERS_BY_STATE_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ORDERS_BY_STATE_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return orders;
    }

    /**
     * Returns the list of all active orders.
     *
     * @return the list of all active orders
     * @throws DBException
     */
    public List<Order> findAllActiveOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_ACTIVE_ORDERS);
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ACTIVE_ORDERS, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ACTIVE_ORDERS, e);
        } finally {
            close(conn, st, rs);
        }
        return orders;
    }

    /**
     * Returns the list of all active user's orders.
     *
     * @param userId a user id to find all active orders
     * @return the list of all active user's orders
     * @throws DBException
     */
    public List<Order> findAllActiveOrdersByUserId(long userId) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_ALL_ACTIVE_ORDERS_BY_USER_ID);
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ALL_ACTIVE_ORDERS_BY_USER_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ALL_ACTIVE_ORDERS_BY_USER_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return orders;
    }

    /**
     * Returns an order by id.
     *
     * @param id an order's id
     * @return an order by id
     * @throws DBException
     */
    public Order getOrder(long id) throws DBException {
        Order order = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_ORDER_BY_ID);
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                order = getOrder(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_ORDER_BY_ID, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_GET_ORDER_BY_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return order;
    }

    /**
     * Adds a new order to database.
     *
     * @param order an order to add into the database
     * @return true if order added successfully
     * @throws DBException
     */
    public boolean makeOrder(Order order) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(ADD_ORDER, Statement.RETURN_GENERATED_KEYS);
            int j = 0;
            pst.setLong(++j, order.getUserId());
            pst.setInt(++j, order.getRoomNumber());
            pst.setDouble(++j, order.getBill());
            pst.setInt(++j, order.getState().ordinal());
            pst.setDate(++j, new Date(order.getDateCreation().getTime()));
            pst.setDate(++j, new Date(order.getArrivalDate().getTime()));
            pst.setDate(++j, new Date(order.getLeavingDate().getTime()));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                order.setId(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_ADD_ORDER, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_ADD_ORDER, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Updates a specified order.
     *
     * @param order an order to update
     * @throws DBException
     */
    public void updateOrder(Order order) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(UPDATE_ORDER);
            int j = 0;
            pst.setLong(++j, order.getUserId());
            pst.setInt(++j, order.getRoomNumber());
            pst.setDouble(++j, order.getBill());
            pst.setInt(++j, order.getState().ordinal());
            pst.setDate(++j, new Date(order.getArrivalDate().getTime()));
            pst.setDate(++j, new Date(order.getLeavingDate().getTime()));
            pst.setLong(++j, order.getId());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_UPDATE_ORDER, e);
            rollback(conn);
            throw new DBException(ErrorMessages.CANNOT_UPDATE_ORDER, e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Deletes the specified order from database.
     *
     * @param order an order to delete
     * @throws DBException
     */
    public void deleteOrder(Order order) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_ORDER);
            pst.setLong(1, order.getUserId());
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
     * Extracts an order from the result set.
     *
     * @param rs the result set to extract an order from
     * @return an order from the result set
     * @throws SQLException
     */
    private Order getOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(DBColumns.ID));
        order.setUserId(rs.getLong(DBColumns.USER_ID));
        order.setRoomNumber(rs.getInt(DBColumns.ORDER_ROOM_NUMBER));
        order.setState(OrderState.getStateById(rs.getInt(DBColumns.ORDER_STATE_ID)));
        order.setBill(rs.getDouble(DBColumns.BILL));
        order.setDateCreation(rs.getDate(DBColumns.ORDER_DATE_CREATION));
        order.setArrivalDate(rs.getDate(DBColumns.ARRIVAL_DATE));
        order.setLeavingDate(rs.getDate(DBColumns.LEAVING_DATE));
        return order;
    }
}
