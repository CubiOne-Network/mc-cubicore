package net.filtastisch.cubiCore;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.filtastisch.cubiCore.commands.HelpCommands;
import net.filtastisch.cubiCore.database.DatabaseManager;
import net.filtastisch.cubiCore.database.dao.GlobalSettingsDao;
import net.filtastisch.cubiCore.listener.ChannelMessageListener;
import net.filtastisch.cubiCore.utils.CloudNetUtils;
import net.filtastisch.cubiCore.utils.ConfigWrapper;
import net.filtastisch.cubiCore.utils.CubiHashMap;
import net.filtastisch.cubiCore.utils.GlobalSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class CubiCore extends JavaPlugin {

    @Getter
    private static CubiCore instance;
    @Getter
    private ConfigWrapper configWrapper;
    @Getter
    private DatabaseManager databaseManager;
    @Getter
    private GlobalSettingsDao globalSettingsDao;
    @Getter
    private final Map<String, String> globalSettings = new CubiHashMap<>(() -> "Dieser Wert existiert nicht!");

    @Override
    public void onEnable() {
        instance = this;
        this.registerApis();
        this.registerCommands();
        this.saveDefaultConfig();
        this.registerUtils();
        this.doDatabaseStuff();
        this.loadOrUpdateSettings();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.databaseManager.shutdown();
    }

    private void registerUtils() {
        this.configWrapper = new ConfigWrapper(this);
        this.databaseManager = new DatabaseManager();
        this.databaseManager.connect();
        CloudNetUtils.getEventManager().registerListener(new ChannelMessageListener());
    }

    private void doDatabaseStuff() {
        this.globalSettingsDao = new GlobalSettingsDao();
        this.globalSettingsDao.createTable();
    }

    public void loadOrUpdateSettings() {
        this.globalSettings.put(GlobalSettings.CHAT_PREFIX, this.globalSettingsDao.loadGlobalSetting(GlobalSettings.CHAT_PREFIX));
    }

    private void registerApis() {
        CommandAPI.onEnable();
    }

    private void registerCommands() {
        new HelpCommands();
    }
}
