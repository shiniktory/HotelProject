package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;


/**
 * The DBManager class contains general methods to work with database such as obtaining connection and
 * closing it.
 *
 * @author V. Kozina-Kravchenko
 */
public class DBManager {

    private static final Logger LOGGER = LogManager.getLogger(DBManager.class);

    /**
     * An instance of database manager.
     */
    private static DBManager instance;

    /**
     * The data source instance.
     */
    private DataSource ds;

    /**
     * Constructs a new DBManager instance and obtains DataSource.
     *
     * @throws ua.nure.kozina.SummaryTask4.exception.DBException if exception encountered while obtaining DataSource
     */
    private DBManager() throws DBException {
        try {
            Context context = new InitialContext();
            Context envContext = (Context) context.lookup("java:comp/env");
            ds = (DataSource) envContext.lookup("jdbc/ST4DB");
            LOGGER.trace("DataSource: " + ds);
        } catch (NamingException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_DATA_SOURCE_CONNECTION, e);
            throw new DBException(ErrorMessages.CANNOT_GET_DATA_SOURCE_CONNECTION, e);
        }
    }

    /**
     * Returns a DBManager instance with configured DataSource.
     *
     * @return a DBManager instance with configured DataSource
     * @throws DBException if exception encountered while obtaining DataSource
     */
    public static synchronized DBManager getInstance() throws DBException {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * Returns a connection with DataSource.
     *
     * @return connection with DataSource
     * @throws DBException if exception encountered while connecting the DataSource
     */
    public Connection getConnection() throws DBException {
        Connection conn;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            LOGGER.error(ErrorMessages.CANNOT_GET_CONNECTION, e);
            throw new DBException(ErrorMessages.CANNOT_GET_CONNECTION, e);
        }
        return conn;
    }

    /**
     * Rolls back all changes made in the current transaction on the specified connection.
     *
     * @param conn a connection with data source
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LOGGER.error(ErrorMessages.CANNOT_ROLLBACK);
            }
        }
    }

    /**
     * Closes the specified resources.
     *
     * @param conn a connection with data source
     * @param st a statement
     * @param rs a result set
     */
    public static void close(Connection conn, Statement st, ResultSet rs) {
        close(rs);
        close(st);
        close(conn);
    }

    /**
     * Closes the specified resources.
     *
     * @param conn a connection with data source
     * @param pst a prepared statement
     * @param rs a result set
     */
    public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        close(rs);
        close(pst);
        close(conn);
    }

    /**
     * Closes the specified connection.
     *
     * @param conn a connection with data source to close
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error(ErrorMessages.CANNOT_CLOSE_CONNECTION);
            }
        }
    }

    /**
     * Closes the specified statement.
     *
     * @param st a statement to close
     */
    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LOGGER.error(ErrorMessages.CANNOT_CLOSE_STATEMENT);
            }
        }
    }

    /**
     * Closes the specified prepared statement.
     *
     * @param pst a prepared statement to close
     */
    public static void close(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                LOGGER.error(ErrorMessages.CANNOT_CLOSE_PREPARED_STATEMENT);
            }
        }
    }

    /**
     * Closes the specified result set.
     *
     * @param rs a result set to close
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error(ErrorMessages.CANNOT_CLOSE_RESULT_SET);
            }
        }
    }
}
