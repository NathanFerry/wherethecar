package groupe1.il3.app.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static volatile DatabaseConnectionManager instance;
    private volatile DatabaseConfig config;
    private final Object configLock = new Object();

    private DatabaseConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionManager();
                }
            }
        }
        return instance;
    }

    public void configure(DatabaseConfig config) {
        synchronized (configLock) {
            this.config = config;
        }
    }

    public DatabaseConfig getConfig() {
        return config;
    }

    public Connection getNewConnection() throws DatabaseException {
        DatabaseConfig localConfig = this.config;

        if (localConfig == null) {
            throw new DatabaseException("Database not configured. Call configure() first.");
        }

        try {
            Properties props = new Properties();
            props.setProperty("user", localConfig.getUsername());
            props.setProperty("password", localConfig.getPassword());

            Connection connection = DriverManager.getConnection(localConfig.getJdbcUrl(), props);

            return connection;

        } catch (SQLException e) {
            throw new DatabaseException(
                String.format("Failed to connect to database at %s:%d - %s",
                    localConfig.getHost(), localConfig.getPort(), e.getMessage()), e);
        }
    }

    public boolean testConnection() {
        try (Connection conn = getNewConnection()) {
            return conn != null && !conn.isClosed();
        } catch (DatabaseException | SQLException e) {
            return false;
        }
    }

    public boolean testConnection(DatabaseConfig config) {
        if (config == null) {
            return false;
        }

        try {
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword());

            Connection conn = DriverManager.getConnection(config.getJdbcUrl(), props);

            return conn != null && !conn.isClosed();

        } catch (SQLException e) {
            return false;
        }
    }

    public void shutdown() {
        synchronized (configLock) {
            config = null;
        }
    }
}
