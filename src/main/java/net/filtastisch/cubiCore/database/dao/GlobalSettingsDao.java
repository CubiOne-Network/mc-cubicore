package net.filtastisch.cubiCore.database.dao;

import net.filtastisch.cubiCore.CubiCore;

import java.sql.*;

public class GlobalSettingsDao {

    private final CubiCore cubiCore = CubiCore.getInstance();

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
