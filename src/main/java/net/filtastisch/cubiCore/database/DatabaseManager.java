package net.filtastisch.cubiCore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.filtastisch.cubiCore.CubiCore;
import net.filtastisch.cubiCore.utils.ConfigWrapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manager class for database connections.
 * <p>
 * Manages MySQL database connections using HikariCP as the connection pool.
 * Provides methods for establishing and closing database connections.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see HikariDataSource
 * @see ConfigWrapper
 */
public class DatabaseManager {

    /**
     * HikariCP DataSource for the connection pool.
     */
    private HikariDataSource dataSource;

    /**
     * ConfigWrapper containing database settings.
     */
    private final ConfigWrapper config = CubiCore.getInstance().getConfigWrapper();

    /**
     * Establishes a database connection.
     * <p>
     * Configures and initializes the HikariCP connection pool with settings
     * from the plugin configuration (JDBC URL, username, password, pool size).
     */
    public void connect() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getDbPoolSize());
        hikariConfig.setPoolName("cubicore");

        dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Returns a connection from the connection pool.
     *
     * @return a {@link Connection} from the pool
     * @throws SQLException if a connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    /**
     * Closes the connection pool and releases all resources.
     * <p>
     * Should be called when the plugin is disabled to properly close all connections.
     */
    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

}
