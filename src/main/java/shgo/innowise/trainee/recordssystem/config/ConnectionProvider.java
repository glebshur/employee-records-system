package shgo.innowise.trainee.recordssystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;


/**
 * Provides connections to database.
 */
@Slf4j
public class ConnectionProvider {
    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/records_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres11";

    private static volatile ConnectionProvider connectionProvider;

    private ConnectionProvider() {
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
        ConnectionProvider localInstance = connectionProvider;
        if (localInstance == null) {
            synchronized (ConnectionProvider.class) {
                localInstance = connectionProvider;
                if (localInstance == null) {
                    connectionProvider = new ConnectionProvider();
                    localInstance = connectionProvider;
                }
            }
        }
        return localInstance;
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
