package ua.nure.kozina.SummaryTask4.DB;


import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.kozina.SummaryTask4.exception.DBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class TestDBManager {

    private static final Logger LOGGER = LogManager.getLogger(TestDBManager.class);

    @BeforeClass
    public static void setUp() {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            ClientConnectionPoolDataSource ds = new ClientConnectionPoolDataSource();
            ds.setDatabaseName("ST4DB");
            ds.setUser("admin");
            ds.setPassword("pass");
            ds.setDataSourceName("ST4DB");

            ic.bind("java:comp/env/jdbc/ST4DB", ds);
        } catch (NamingException ex) {
            LOGGER.error(ex);
        }
    }

    @Test
    public void testGetConnection() throws DBException {
        Connection conn = DBManager.getInstance().getConnection();
        assertNotNull(conn);
    }
}
