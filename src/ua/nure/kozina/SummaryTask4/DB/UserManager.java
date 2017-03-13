package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.constants.DBColumns;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.entity.ForgotPasswordQuery;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.Role;
import static ua.nure.kozina.SummaryTask4.DB.DBManager.*;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserManager class contains method to manage users.
 *
 * @author V. Kozina-Kravchenko
 */
public class UserManager {

    private static final Logger LOGGER = LogManager.getLogger(UserManager.class);

    /**
     * SQL queries.
     */
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    private static final String SELECT_ALL_FEEDBACKS = "SELECT f.*, u.id user_id, u.* FROM feedbacks f INNER JOIN users u ON u.id = f.user_id";
    private static final String SELECT_FEEDBACK_BY_USER_ID = "SELECT f.*, u.id user_id, u.* FROM feedbacks f INNER JOIN users u ON u.id = f.user_id WHERE f.user_id=?";
    private static final String SELECT_FORGOT_PASSWORD_QUERY = "SELECT * FROM forgot_password WHERE token=?";
    private static final String ADD_USER = "INSERT INTO users VALUES (DEFAULT,?,?,?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE users SET password=?, first_name=?, last_name=?, email=? WHERE id=?";
    private static final String LEAVE_FEEDBACK = "INSERT INTO feedbacks VALUES (DEFAULT, ?,?,? )";
    private static final String CREATE_FORGOT_PASSWORD_QUERY = "INSERT INTO forgot_password VALUES (DEFAULT, ?,?,?,?)";
    private static final String UPDATE_FORGOT_PASSWORD_QUERY = "UPDATE forgot_password SET reset=? WHERE id=?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id=?";
    private static final String DELETE_FEEDBACK = "DELETE FROM feedbacks WHERE id=?";
    private static final String DELETE_FORGOT_PASSWORD_QUERY = "DELETE FROM FORGOT_PASSWORD WHERE id=?";

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
     * Returns the list of all users.
     *
     * @return the list of all users
     * @throws DBException
     */
    public List<User> findAllUsers() throws DBException {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_USERS);
            while (rs.next()) {
                users.add(getUser(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_ALL_USERS, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_ALL_USERS, e);
        } finally {
            close(conn, st, rs);
        }
        return users;
    }

    /**
     * Returns a user with the specified login.
     *
     * @param login a login to find a user with
     * @return a user with the specified login
     * @throws DBException
     */
    public User getUserByLogin(String login) throws DBException {
        User user = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_USER_BY_LOGIN);
            pst.setString(1, login.toLowerCase());
            rs = pst.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_USER_BY_LOGIN, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_USER_BY_LOGIN, e);
        } finally {
            close(conn, pst, rs);
        }
        return user;
    }

    /**
     * Returns a user with the specified user id.
     *
     * @param id a user id
     * @return a user with the specified user id
     * @throws DBException
     */
    public User getUser(long id) throws DBException {
        User user = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_USER_BY_ID);
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_USER_BY_ID, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_USER_BY_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return user;
    }

    /**
     * Returns a user with the specified email.
     *
     * @param email a user's email address
     * @return a user with the specified email
     * @throws DBException
     */
    public User getUserByEMail(String email) throws DBException {
        User user = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_USER_BY_EMAIL);
            pst.setString(1, email);
            rs = pst.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_USER_BY_EMAIL, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_USER_BY_EMAIL, e);
        } finally {
            close(conn, pst, rs);
        }
        return user;
    }

    /**
     * Returns the list of all feedbacks.
     *
     * @return the list of all feedbacks
     * @throws DBException
     */
    public List<Feedback> findAllFeedbacks() throws DBException {
        List<Feedback> feedbacks = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(SELECT_ALL_FEEDBACKS);
            while (rs.next()) {
                feedbacks.add(getFeedback(rs));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_ALL_ACTIVE_REQUESTS, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_ALL_FEEDBACKS, e);
        } finally {
            close(conn, st, rs);
        }
        return feedbacks;
    }

    /**
     * Returns a feedback with the specified user id.
     *
     * @param userId a user id to find feedback
     * @return a feedback with the specified user id
     * @throws DBException
     */
    public Feedback getFeedbackByUserId(long userId) throws DBException {
        Feedback feedback = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_FEEDBACK_BY_USER_ID);
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                feedback = getFeedback(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_FEEDBACK_BY_USER_ID, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_FEEDBACK_BY_USER_ID, e);
        } finally {
            close(conn, pst, rs);
        }
        return feedback;
    }

    /**
     * Returns a forgot password query with the specified user email and string token.
     *
     * @param token a generated token to identify user
     * @return a forgot password query with the specified user email and string token
     * @throws DBException
     */
    public ForgotPasswordQuery getQuery(String token) throws DBException {
        ForgotPasswordQuery query = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(SELECT_FORGOT_PASSWORD_QUERY);
            pst.setString(1, token);
            rs = pst.executeQuery();
            while (rs.next()) {
                query = getForgotPasswordQuery(rs);
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_GET_FORGOT_PASSWORD_QUERY, e);
            rollback(conn);
            throw new DBException(CANNOT_GET_FORGOT_PASSWORD_QUERY, e);
        } finally {
            close(conn, pst, rs);
        }
        return query;
    }


    /**
     * Adds a new user into the database.
     *
     * @param user a user to add into the database
     * @return true if user added into the database successfully
     * @throws DBException
     */
    public boolean addUser(User user) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
            int j = 0;
            pst.setString(++j, user.getLogin().toLowerCase());
            pst.setString(++j, user.getPassword());
            pst.setInt(++j, Role.CLIENT.ordinal());
            pst.setString(++j, user.getFirstName());
            pst.setString(++j, user.getLastName());
            pst.setString(++j, user.getEmail());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                user.setId(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_ADD_USER, e);
            rollback(conn);
            throw new DBException(CANNOT_ADD_USER, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Adds a new feedback into the database.
     *
     * @param feedback a feedback to add into the database
     * @return true if a feedback added into the database successfully
     * @throws DBException
     */
    public boolean leaveFeedback(Feedback feedback) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(LEAVE_FEEDBACK, Statement.RETURN_GENERATED_KEYS);
            int j = 0;
            pst.setLong(++j, feedback.getUser().getId());
            pst.setDate(++j, new Date(feedback.getDateCreated().getTime()));
            pst.setString(++j, feedback.getText());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                feedback.setId(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_LEAVE_FEEDBACK, e);
            rollback(conn);
            throw new DBException(CANNOT_LEAVE_FEEDBACK, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Adds a new forgot password query into the database.
     *
     * @param query the query to add into the database
     * @return true if query added successfully
     * @throws DBException
     */
    public boolean addForgotPasswordQuery(ForgotPasswordQuery query) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(CREATE_FORGOT_PASSWORD_QUERY, Statement.RETURN_GENERATED_KEYS);
            int j = 0;
            pst.setString(++j, query.getEmail());
            pst.setString(++j, query.getToken());
            pst.setTimestamp(++j, new Timestamp(query.getDateExpire().getTime()));
            pst.setBoolean(++j, query.isReset());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            while (rs.next()) {
                query.setId(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_ADD_FORGOT_PASSWORD_QUERY, e);
            rollback(conn);
            throw new DBException(CANNOT_ADD_FORGOT_PASSWORD_QUERY, e);
        } finally {
            close(conn, pst, rs);
        }
        return true;
    }

    /**
     * Updates a specified user.
     *
     * @param user a user to update
     * @throws DBException
     */
    public void updateUser(User user) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(UPDATE_USER);
            int j = 0;
            pst.setString(++j, user.getPassword());
            pst.setString(++j, user.getFirstName());
            pst.setString(++j, user.getLastName());
            pst.setString(++j, user.getEmail());
            pst.setLong(++j, user.getId());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_UPDATE_USER, e);
            rollback(conn);
            throw new DBException(CANNOT_UPDATE_USER, e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Updates a specified forgot password query.
     *
     * @param query a forgot password query to update
     * @throws ua.nure.kozina.SummaryTask4.exception.DBException
     */
    public void updateForgotPasswordQuery(ForgotPasswordQuery query) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(UPDATE_FORGOT_PASSWORD_QUERY);
            pst.setBoolean(1, query.isReset());
            pst.setLong(2, query.getId());
            pst.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(CANNOT_UPDATE_FORGOT_PASSWORD_QUERY, e);
            rollback(conn);
            throw new DBException(CANNOT_UPDATE_FORGOT_PASSWORD_QUERY, e);
        } finally {
            close(pst);
            close(conn);
        }
    }

    /**
     * Deletes the specified user from database.
     *
     * @param user the user to delete
     * @throws DBException
     */
    public void deleteUser(User user) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_USER);
            pst.setLong(1, user.getId());
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
     * Deletes the specified user's feedback from the database.
     *
     * @param feedback a feedback to delete
     * @throws DBException
     */
    public void deleteFeedback(Feedback feedback) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_FEEDBACK);
            pst.setLong(1, feedback.getId());
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
     * Deletes the specified forgot password query from the database.
     *
     * @param query a forgot password query to delete
     * @throws DBException
     */
    public void deleteForgotPasswordQuery(ForgotPasswordQuery query) throws DBException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement(DELETE_FORGOT_PASSWORD_QUERY);
            pst.setLong(1, query.getId());
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
     * Extracts a user from the result set.
     *
     * @param rs the result set to extract user from
     * @return a user from the result set
     * @throws SQLException
     * @throws DBException
     */
    private User getUser(ResultSet rs) throws SQLException, DBException {
        User user = new User();
        user.setId(rs.getLong(DBColumns.ID));
        user.setLogin(rs.getString(DBColumns.LOGIN));
        user.setPassword(rs.getString(DBColumns.PASSWORD));
        user.setUserRole(Role.getRoleById(rs.getInt(DBColumns.USER_ROLE_ID)));
        user.setFirstName(rs.getString(DBColumns.FIRST_NAME));
        user.setLastName(rs.getString(DBColumns.LAST_NAME));
        user.setEmail(rs.getString(DBColumns.EMAIL));
        return user;
    }

    /**
     * Extracts a feedback from the result set.
     *
     * @param rs the result set to extract a feedback from
     * @return a feedback from the result set
     * @throws SQLException
     * @throws DBException
     */
    private Feedback getFeedback(ResultSet rs) throws SQLException, DBException {
        Feedback feedback = new Feedback();
        feedback.setId(rs.getLong(DBColumns.ID));
        feedback.setUser(getUserForFeedback(rs));
        feedback.setDateCreated(rs.getDate(DBColumns.FEEDBACK_DATE_CREATION));
        feedback.setText(rs.getString(DBColumns.FEEDBACK_TEXT));
        return feedback;
    }

    /**
     * Extracts a user from the result set.
     *
     * @param rs the result set to extract user from
     * @return a user from the result set
     * @throws SQLException
     * @throws DBException
     */
    private User getUserForFeedback(ResultSet rs) throws SQLException, DBException {
        User user = new User();
        user.setId(rs.getLong(DBColumns.USER_ID));
        user.setLogin(rs.getString(DBColumns.LOGIN));
        user.setPassword(rs.getString(DBColumns.PASSWORD));
        user.setUserRole(Role.getRoleById(rs.getInt(DBColumns.USER_ROLE_ID)));
        user.setFirstName(rs.getString(DBColumns.FIRST_NAME));
        user.setLastName(rs.getString(DBColumns.LAST_NAME));
        user.setEmail(rs.getString(DBColumns.EMAIL));
        return user;
    }

    /**
     * Extracts a forgot password query from the specified result set.
     *
     * @param rs the result set to get query from
     * @return a forgot password query from the specified result set
     * @throws SQLException
     */
    private ForgotPasswordQuery getForgotPasswordQuery(ResultSet rs) throws SQLException {
        ForgotPasswordQuery query = new ForgotPasswordQuery();
        query.setId(rs.getLong(DBColumns.ID));
        query.setEmail(rs.getString(DBColumns.EMAIL));
        query.setToken(rs.getString(DBColumns.TOKEN));
        query.setDateExpire(rs.getTimestamp(DBColumns.DATE_EXP));
        query.setReset(rs.getBoolean(DBColumns.RESET));
        return query;
    }
}