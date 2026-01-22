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

/**
 * Main class of the CubiCore plugin.
 * <p>
 * Entry point for the CubiCore plugin, extending {@link JavaPlugin}.
 * Handles initialization of all plugin components including database connections,
 * configuration, commands, and event listeners.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public final class CubiCore extends JavaPlugin {

    /**
     * Singleton instance of the CubiCore plugin.
     */
    @Getter
    private static CubiCore instance;

    /**
     * Wrapper for plugin configuration.
     */
    @Getter
    private ConfigWrapper configWrapper;

    /**
     * Manager for database connections.
     */
    @Getter
    private DatabaseManager databaseManager;

    /**
     * DAO for global settings.
     */
    @Getter
    private GlobalSettingsDao globalSettingsDao;

    /**
     * Map storing global settings with a default fallback value.
     */
    @Getter
    private final Map<String, String> globalSettings = new CubiHashMap<>(() -> "Dieser Wert existiert nicht!");

    /**
     * Called when the plugin is enabled.
     * <p>
     * Initializes all plugin components in the following order:
     * <ol>
     *     <li>Sets the singleton instance</li>
     *     <li>Registers APIs (CommandAPI)</li>
     *     <li>Registers commands</li>
     *     <li>Saves default configuration</li>
     *     <li>Registers utility classes</li>
     *     <li>Performs database setup</li>
     *     <li>Loads global settings</li>
     * </ol>
     */
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

    /**
     * Called when the plugin is disabled.
     * <p>
     * Performs cleanup by disabling CommandAPI and closing database connections.
     */
    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.databaseManager.shutdown();
    }

    /**
     * Registers all utility classes and managers.
     * <p>
     * Initializes {@link ConfigWrapper}, {@link DatabaseManager},
     * and registers {@link ChannelMessageListener} with CloudNet.
     */
    private void registerUtils() {
        this.configWrapper = new ConfigWrapper(this);
        this.databaseManager = new DatabaseManager();
        this.databaseManager.connect();
        CloudNetUtils.getEventManager().registerListener(new ChannelMessageListener());
    }

    /**
     * Performs database-related initialization.
     * <p>
     * Creates {@link GlobalSettingsDao} and sets up required tables.
     */
    private void doDatabaseStuff() {
        this.globalSettingsDao = new GlobalSettingsDao();
        this.globalSettingsDao.createTable();
    }

    /**
     * Loads or refreshes global settings from the database.
     * <p>
     * Called at startup and can be used at runtime to reload settings after changes.
     */
    public void loadOrUpdateSettings() {
        this.globalSettings.put(GlobalSettings.CHAT_PREFIX, this.globalSettingsDao.loadGlobalSetting(GlobalSettings.CHAT_PREFIX));
    }

    /**
     * Registers external APIs used by the plugin.
     * <p>
     * Enables CommandAPI for command registration.
     */
    private void registerApis() {
        CommandAPI.onEnable();
    }

    /**
     * Registers all plugin commands.
     * <p>
     * Creates instances of all command classes like {@link HelpCommands}.
     */
    private void registerCommands() {
        new HelpCommands();
    }
}
