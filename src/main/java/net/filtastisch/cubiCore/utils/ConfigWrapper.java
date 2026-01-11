package net.filtastisch.cubiCore.utils;

import lombok.Getter;
import net.filtastisch.cubiCore.CubiCore;

/**
 * Wrapper-Klasse für die Plugin-Konfiguration.
 * <p>
 * Diese Klasse kapselt den Zugriff auf die Konfigurationsdatei des Plugins
 * und stellt typisierte Getter-Methoden für alle Konfigurationswerte bereit.
 * Hauptsächlich werden Datenbankeinstellungen verwaltet.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class ConfigWrapper {

    /**
     * Die Instanz des CubiCore-Plugins.
     */
    private final CubiCore corePlugin;

    /**
     * Der Hostname der Datenbank.
     */
    @Getter
    private final String dbHost;

    /**
     * Der Port der Datenbank.
     */
    @Getter
    private final String dbPort;

    /**
     * Der Name der Datenbank.
     */
    @Getter
    private final String dbDatabase;

    /**
     * Der Benutzername für die Datenbankverbindung.
     */
    @Getter
    private final String dbUser;

    /**
     * Das Passwort für die Datenbankverbindung.
     */
    @Getter
    private final String dbPassword;

    /**
     * Das Präfix für Datenbanktabellen.
     */
    @Getter
    private final String dbTablePrefix;

    /**
     * Die Größe des Datenbank-Connection-Pools.
     */
    @Getter
    private final int dbPoolSize;

    /**
     * Erstellt einen neuen ConfigWrapper und lädt alle Konfigurationswerte.
     * <p>
     * Liest folgende Werte aus der config.yml:
     * <ul>
     *     <li>{@code database.host} - Datenbank-Host</li>
     *     <li>{@code database.port} - Datenbank-Port</li>
     *     <li>{@code database.database} - Datenbankname</li>
     *     <li>{@code database.user} - Benutzername</li>
     *     <li>{@code database.password} - Passwort</li>
     *     <li>{@code database.table-prefix} - Tabellenpräfix</li>
     *     <li>{@code database.pool-size} - Pool-Größe</li>
     * </ul>
     * </p>
     *
     * @param corePlugin die Instanz des CubiCore-Plugins
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
     * Erstellt die JDBC-URL für die MySQL-Datenbankverbindung.
     *
     * @return die vollständige JDBC-URL im Format {@code jdbc:mysql://host:port/database}
     */
    public String getJdbcUrl() {
        return "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbDatabase;
    }

}
