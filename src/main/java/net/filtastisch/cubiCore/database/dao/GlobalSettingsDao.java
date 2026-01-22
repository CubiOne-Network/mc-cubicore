package net.filtastisch.cubiCore.database.dao;

import net.filtastisch.cubiCore.CubiCore;

import java.sql.*;

/**
 * Data Access Object for global settings.
 * <p>
 * Handles database access for global settings of the CubiCore plugin.
 * Provides methods for table creation and loading settings from the database.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class GlobalSettingsDao {

    /**
     * CubiCore plugin instance for accessing configuration and database.
     */
    private final CubiCore cubiCore = CubiCore.getInstance();

    /**
     * Creates the global settings table if it doesn't exist.
     * <p>
     * Table columns:
     * <ul>
     *     <li>{@code setting_name} - Setting name (primary key)</li>
     *     <li>{@code value} - Setting value</li>
     *     <li>{@code last_edited} - Timestamp of last modification (auto-updated)</li>
     * </ul>
     * Also inserts a default entry for the chat prefix.
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
     * Loads a global setting from the database.
     *
     * @param key the key/name of the setting to load
     * @return the setting value, or {@code null} if the setting doesn't exist
     * @throws RuntimeException if a database error occurs
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
