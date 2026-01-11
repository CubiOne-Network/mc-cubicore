package net.filtastisch.cubiCore.database.dao;

import net.filtastisch.cubiCore.CubiCore;

import java.sql.*;

/**
 * Data Access Object (DAO) für globale Einstellungen.
 * <p>
 * Diese Klasse verwaltet den Datenbankzugriff für die globalen Einstellungen
 * des CubiCore-Plugins. Sie bietet Methoden zum Erstellen der Tabelle und
 * zum Laden von Einstellungen aus der Datenbank.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class GlobalSettingsDao {

    /**
     * Die Instanz des CubiCore-Plugins für den Zugriff auf Konfiguration und Datenbank.
     */
    private final CubiCore cubiCore = CubiCore.getInstance();

    /**
     * Erstellt die Tabelle für globale Einstellungen, falls sie nicht existiert.
     * <p>
     * Die Tabelle enthält folgende Spalten:
     * <ul>
     *     <li>{@code setting_name} - Der Name der Einstellung (Primärschlüssel)</li>
     *     <li>{@code value} - Der Wert der Einstellung</li>
     *     <li>{@code last_edited} - Zeitstempel der letzten Änderung (automatisch aktualisiert)</li>
     * </ul>
     * Zusätzlich wird ein Standardeintrag für das Chat-Präfix eingefügt.
     * </p>
     */
    public void createTable() {
        String prefix = cubiCore.getConfigWrapper().getDbTablePrefix();

        String sql = "CREATE TABLE IF NOT EXISTS " + prefix + "global_settings (" +
                "setting_name VARCHAR(255) PRIMARY KEY, " +
                "value VARCHAR(255) NOT NULL, " +
                "last_edited TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";

        String defaultRecords = "INSERT IGNORE INTO " + prefix + "global_settings (setting_name, value, last_edited) " +
                "VALUES ('chat_prefix', '&bCubi&6One', CURRENT_TIMESTAMP)";

        try (Connection c = cubiCore.getDatabaseManager().getConnection();
             Statement st = c.createStatement()) {

            st.execute(sql);
            st.execute(defaultRecords);

        } catch (SQLException e) {
            cubiCore.getLogger().severe("Fehler beim Setup der Datenbank: " + e.getMessage());
        }
    }

    /**
     * Lädt eine globale Einstellung aus der Datenbank.
     *
     * @param key der Schlüssel/Name der Einstellung, die geladen werden soll
     * @return der Wert der Einstellung oder {@code null}, wenn die Einstellung nicht existiert
     * @throws RuntimeException wenn ein Datenbankfehler auftritt
     */
    public String loadGlobalSetting(String key) {
        String sql = "SELECT * FROM " +
                cubiCore.getConfigWrapper().getDbTablePrefix() +
                "global_settings where setting_name=?";

        try (Connection c = cubiCore.getDatabaseManager().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
        ) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return rs.getString("value");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
