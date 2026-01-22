package net.filtastisch.cubiCore.utils;

import lombok.Getter;
import net.filtastisch.cubiCore.CubiCore;

/**
 * Wrapper class for plugin configuration.
 * <p>
 * Encapsulates access to the plugin's configuration file and provides
 * typed getter methods for all configuration values.
 * Primarily manages database settings.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class ConfigWrapper {

    /**
     * CubiCore plugin instance.
     */
    private final CubiCore corePlugin;

    /**
     * Database hostname.
     */
    @Getter
    private final String dbHost;

    /**
     * Database port.
     */
    @Getter
    private final String dbPort;

    /**
     * Database name.
     */
    @Getter
    private final String dbDatabase;

    /**
     * Database username.
     */
    @Getter
    private final String dbUser;

    /**
     * Database password.
     */
    @Getter
    private final String dbPassword;

    /**
     * Database table prefix.
     */
    @Getter
    private final String dbTablePrefix;

    /**
     * Database connection pool size.
     */
    @Getter
    private final int dbPoolSize;

    /**
     * Creates a new ConfigWrapper and loads all configuration values.
     * <p>
     * Reads the following values from config.yml:
     * <ul>
     *     <li>{@code database.host} - Database host</li>
     *     <li>{@code database.port} - Database port</li>
     *     <li>{@code database.database} - Database name</li>
     *     <li>{@code database.user} - Username</li>
     *     <li>{@code database.password} - Password</li>
     *     <li>{@code database.table-prefix} - Table prefix</li>
     *     <li>{@code database.pool-size} - Pool size</li>
     * </ul>
     *
     * @param corePlugin the CubiCore plugin instance
     */
    public ConfigWrapper(CubiCore corePlugin){
        this.corePlugin = corePlugin;

        this.dbHost = this.corePlugin.getConfig().getString("database.host");
        this.dbPort = this.corePlugin.getConfig().getString("database.port");
        this.dbDatabase = this.corePlugin.getConfig().getString("database.database");
        this.dbUser = this.corePlugin.getConfig().getString("database.user");
        this.dbPassword = this.corePlugin.getConfig().getString("database.password");
        this.dbTablePrefix = this.corePlugin.getConfig().getString("database.table-prefix");
        this.dbPoolSize = this.corePlugin.getConfig().getInt("database.pool-size");
    }


    /**
     * Builds the JDBC URL for MySQL database connection.
     *
     * @return the full JDBC URL in format {@code jdbc:mysql://host:port/database}
     */
    public String getJdbcUrl() {
        return "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbDatabase;
    }

}
