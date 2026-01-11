package net.filtastisch.cubiCore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.filtastisch.cubiCore.CubiCore;
import net.filtastisch.cubiCore.utils.ConfigWrapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manager-Klasse für die Datenbankverwaltung.
 * <p>
 * Diese Klasse verwaltet die Verbindung zur MySQL-Datenbank unter Verwendung
 * von HikariCP als Connection-Pool. Sie bietet Methoden zum Herstellen und
 * Schließen der Datenbankverbindung sowie zum Abrufen von Connections.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see HikariDataSource
 * @see ConfigWrapper
 */
public class DatabaseManager {

    /**
     * Die HikariCP-DataSource für den Connection-Pool.
     */
    private HikariDataSource dataSource;

    /**
     * Der ConfigWrapper mit den Datenbankeinstellungen.
     */
    private final ConfigWrapper config = CubiCore.getInstance().getConfigWrapper();

    /**
     * Stellt eine Verbindung zur Datenbank her.
     * <p>
     * Konfiguriert und initialisiert den HikariCP Connection-Pool mit den
     * Einstellungen aus der Plugin-Konfiguration:
     * <ul>
     *     <li>JDBC-URL</li>
     *     <li>Benutzername</li>
     *     <li>Passwort</li>
     *     <li>Pool-Größe</li>
     * </ul>
     * </p>
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
     * Gibt eine Verbindung aus dem Connection-Pool zurück.
     *
     * @return eine {@link Connection} aus dem Pool
     * @throws SQLException wenn keine Verbindung hergestellt werden kann
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    /**
     * Schließt den Connection-Pool und gibt alle Ressourcen frei.
     * <p>
     * Diese Methode sollte beim Herunterfahren des Plugins aufgerufen werden,
     * um alle Datenbankverbindungen ordnungsgemäß zu schließen.
     * </p>
     */
    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

}
