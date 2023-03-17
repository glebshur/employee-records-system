package shgo.innowise.trainee.recordssystem.config;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides connections to database.
 */
@Slf4j
public class ConnectionProvider {
    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/records_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres11";

    private static ConnectionProvider connectionProvider;

    public ConnectionProvider() {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gives instance of ConnectionProvider object.
     *
     * @return instance of ConnectionProvider
     */
    public static ConnectionProvider getInstance() {
        if (connectionProvider == null) {
            connectionProvider = new ConnectionProvider();
        }
        return connectionProvider;
    }

    /**
     * Gives connection to database.
     *
     * @return connection
     * @throws SQLException sql exception
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
