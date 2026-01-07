package net.filtastisch.cubiCore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.filtastisch.cubiCore.CubiCore;
import net.filtastisch.cubiCore.utils.ConfigWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private HikariDataSource dataSource;
    private final ConfigWrapper config = CubiCore.getInstance().getConfigWrapper();

    public void connect() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getDbUser());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setMaximumPoolSize(config.getDbPoolSize());
        hikariConfig.setPoolName("cubicore");

        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

}
