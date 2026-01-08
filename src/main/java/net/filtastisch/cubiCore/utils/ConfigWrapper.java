package net.filtastisch.cubiCore.utils;

import lombok.Getter;
import net.filtastisch.cubiCore.CubiCore;

public class ConfigWrapper {

    private final CubiCore corePlugin;

    @Getter
    private final String dbHost, dbPort, dbDatabase, dbUser, dbPassword, dbTablePrefix;
    @Getter
    private final int dbPoolSize;

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


    public String getJdbcUrl() {
        return "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbDatabase;
    }

}
