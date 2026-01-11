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
 * Hauptklasse des CubiCore-Plugins.
 * <p>
 * Diese Klasse ist der zentrale Einstiegspunkt für das CubiCore-Plugin und erweitert {@link JavaPlugin}.
 * Sie verwaltet die Initialisierung aller Plugin-Komponenten, einschließlich Datenbankverbindungen,
 * Konfigurationen, Befehle und Event-Listener.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public final class CubiCore extends JavaPlugin {

    /**
     * Die Singleton-Instanz des CubiCore-Plugins.
     */
    @Getter
    private static CubiCore instance;

    /**
     * Der Wrapper für die Plugin-Konfiguration.
     */
    @Getter
    private ConfigWrapper configWrapper;

    /**
     * Der Manager für Datenbankverbindungen.
     */
    @Getter
    private DatabaseManager databaseManager;

    /**
     * Das Data Access Object für globale Einstellungen.
     */
    @Getter
    private GlobalSettingsDao globalSettingsDao;

    /**
     * Eine Map, die globale Einstellungen speichert.
     * Verwendet {@link CubiHashMap} mit einem Standard-Fallback-Wert.
     */
    @Getter
    private final Map<String, String> globalSettings = new CubiHashMap<>(() -> "Dieser Wert existiert nicht!");

    /**
     * Wird beim Aktivieren des Plugins aufgerufen.
     * <p>
     * Initialisiert alle Plugin-Komponenten in folgender Reihenfolge:
     * <ol>
     *     <li>Setzt die Singleton-Instanz</li>
     *     <li>Registriert APIs (CommandAPI)</li>
     *     <li>Registriert Befehle</li>
     *     <li>Speichert die Standard-Konfiguration</li>
     *     <li>Registriert Utility-Klassen</li>
     *     <li>Führt Datenbank-Setup durch</li>
     *     <li>Lädt globale Einstellungen</li>
     * </ol>
     * </p>
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
     * Wird beim Deaktivieren des Plugins aufgerufen.
     * <p>
     * Führt Aufräumarbeiten durch:
     * <ul>
     *     <li>Deaktiviert die CommandAPI</li>
     *     <li>Schließt die Datenbankverbindung</li>
     * </ul>
     * </p>
     */
    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.databaseManager.shutdown();
    }

    /**
     * Registriert alle Utility-Klassen und Manager.
     * <p>
     * Initialisiert den {@link ConfigWrapper}, den {@link DatabaseManager}
     * und registriert den {@link ChannelMessageListener} bei CloudNet.
     * </p>
     */
    private void registerUtils() {
        this.configWrapper = new ConfigWrapper(this);
        this.databaseManager = new DatabaseManager();
        this.databaseManager.connect();
        CloudNetUtils.getEventManager().registerListener(new ChannelMessageListener());
    }

    /**
     * Führt alle datenbankbezogenen Initialisierungen durch.
     * <p>
     * Erstellt das {@link GlobalSettingsDao} und legt die erforderlichen Tabellen an.
     * </p>
     */
    private void doDatabaseStuff() {
        this.globalSettingsDao = new GlobalSettingsDao();
        this.globalSettingsDao.createTable();
    }

    /**
     * Lädt oder aktualisiert die globalen Einstellungen aus der Datenbank.
     * <p>
     * Diese Methode wird beim Start aufgerufen und kann auch zur Laufzeit
     * verwendet werden, um Einstellungen neu zu laden (z.B. nach einer Änderung).
     * </p>
     */
    public void loadOrUpdateSettings() {
        this.globalSettings.put(GlobalSettings.CHAT_PREFIX, this.globalSettingsDao.loadGlobalSetting(GlobalSettings.CHAT_PREFIX));
    }

    /**
     * Registriert alle externen APIs, die vom Plugin verwendet werden.
     * <p>
     * Aktiviert die CommandAPI für die Befehlsregistrierung.
     * </p>
     */
    private void registerApis() {
        CommandAPI.onEnable();
    }

    /**
     * Registriert alle Plugin-Befehle.
     * <p>
     * Erstellt Instanzen aller Befehlsklassen wie {@link HelpCommands}.
     * </p>
     */
    private void registerCommands() {
        new HelpCommands();
    }
}
