package net.filtastisch.cubiCore.guis;

import net.filtastisch.cubiCore.utils.SerializerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Eine flexible GUI-Klasse für die Erstellung von Inventar-basierten Benutzeroberflächen.
 * <p>
 * Diese Klasse ermöglicht das Erstellen, Verwalten und Anzeigen von interaktiven
 * GUI-Inventaren für Spieler. Sie unterstützt:
 * <ul>
 *     <li>Dynamische Button-Platzierung</li>
 *     <li>Optionale Toolbars</li>
 *     <li>Verschiedene Titel-Formate (Plain, MiniMessage, Legacy)</li>
 *     <li>Event-Handling für Klicks und Schließen</li>
 * </ul>
 * </p>
 *
 * <p><b>Verwendung mit Builder:</b></p>
 * <pre>{@code
 * GUI gui = new GUI.Builder()
 *     .setTitle("Mein Menü")
 *     .setRows(3)
 *     .build();
 * gui.setButton(13, new GUIButton(itemStack));
 * gui.open(player);
 * }</pre>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see GUIButton
 * @see ToolbarConfig
 */
public class GUI implements Listener {

    /**
     * Eine Map, die alle aktuell geöffneten GUIs nach Spieler-UUID speichert.
     */
    protected static final Map<UUID, GUI> openGuis = new HashMap<>();

    /**
     * Der Titel der GUI als Adventure-Component.
     */
    protected final Component title;

    /**
     * Die Anzahl der Reihen in der GUI (1-6).
     */
    protected final int rows;

    /**
     * Eine Map der Buttons, indiziert nach Slot-Position.
     */
    protected final Map<Integer, GUIButton> buttons;

    /**
     * Das Bukkit-Inventar, das diese GUI repräsentiert.
     */
    protected Inventory inventory;

    /**
     * Die optionale Toolbar-Konfiguration.
     */
    protected ToolbarConfig toolbar;

    /**
     * Erstellt eine leere GUI-Instanz.
     * <p>
     * Diese Konstruktor wird hauptsächlich für die Listener-Registrierung verwendet.
     * Für die Erstellung funktionaler GUIs sollte der {@link Builder} verwendet werden.
     * </p>
     */
    public GUI(){
        this.title = Component.empty();
        this.rows = 0;
        this.buttons = new HashMap<>();
    }

    /**
     * Erstellt eine GUI mit dem angegebenen Titel und der Anzahl der Reihen.
     *
     * @param title der Titel der GUI als {@link Component}
     * @param rows  die Anzahl der Reihen (1-6)
     */
    private GUI(Component title, int rows){
        this.rows = rows;
        this.buttons = new HashMap<>();
        this.title = title;
    }

    /**
     * Fügt eine Toolbar zur GUI hinzu.
     *
     * @param toolbar die {@link ToolbarConfig} für die GUI
     * @return diese GUI-Instanz für Method-Chaining
     */
    public GUI withToolbar(ToolbarConfig toolbar) {
        this.toolbar = toolbar;
        return this;
    }

    /**
     * Platziert einen Button an der angegebenen Slot-Position.
     *
     * @param slot   die Slot-Position (0 bis rows*9-1)
     * @param button der zu platzierende {@link GUIButton}
     * @return diese GUI-Instanz für Method-Chaining
     * @throws IllegalArgumentException wenn der Slot außerhalb des gültigen Bereichs liegt
     *                                  oder für die Toolbar reserviert ist
     */
    public GUI setButton(int slot, GUIButton button) {
        if (slot < 0 || slot >= rows * 9) throw new IllegalArgumentException("Slot should be between 0 and " + (rows * 9 - 1));
        if (toolbar != null && slot >= toolbar.getToolbarRow() * 9 && slot < (toolbar.getToolbarRow() + 1) * 9){
            throw new IllegalArgumentException("The slot " + slot + " is reserved for the toolbar!");
        }
        buttons.put(slot, button);
        inventory.setItem(slot, button.getIcon());
        return this;
    }

    /**
     * Entfernt einen Button von der angegebenen Slot-Position.
     *
     * @param slot die Slot-Position, von der der Button entfernt werden soll
     * @return diese GUI-Instanz für Method-Chaining
     */
    public GUI removeButton(int slot) {
        buttons.remove(slot);
        inventory.setItem(slot, null);
        return this;
    }

    /**
     * Rendert die Toolbar in der GUI.
     * <p>
     * Diese Methode platziert alle Toolbar-Buttons in der konfigurierten Toolbar-Reihe.
     * </p>
     */
    protected void renderToolbar() {
        if (toolbar != null) {
            int toolbarRow = toolbar.getToolbarRow();
            for (int i = 0; i < 9; i++) {
                int slot = toolbarRow * 9 + i;
                GUIButton tbButton = toolbar.getButton(slot);
                if (tbButton != null) {
                    buttons.put(slot, tbButton);
                    inventory.setItem(slot, tbButton.getIcon());
                }
            }
        }
    }

    /**
     * Öffnet die GUI für den angegebenen Spieler.
     * <p>
     * Rendert zuerst die Toolbar (falls vorhanden), öffnet dann das Inventar
     * und registriert die GUI als geöffnet für den Spieler.
     * </p>
     *
     * @param player der Spieler, für den die GUI geöffnet werden soll
     */
    public void open(Player player) {
        renderToolbar();
        player.openInventory(inventory);
        openGuis.put(player.getUniqueId(), this);
    }

    /**
     * Aktualisiert die GUI durch erneutes Rendern aller Buttons.
     * <p>
     * Leert das Inventar und platziert alle registrierten Buttons neu.
     * </p>
     */
    public void refresh() {
        inventory.clear();
        buttons.forEach((slot, btn) -> inventory.setItem(slot, btn.getIcon()));
    }

    /**
     * Behandelt Klick-Events im Inventar.
     * <p>
     * Verhindert Standard-Inventar-Interaktionen und führt Button-Listener aus,
     * wenn ein Button angeklickt wurde.
     * </p>
     *
     * @param event das {@link InventoryClickEvent}
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        GUI gui = openGuis.get(player.getUniqueId());
        if (gui != this) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot < 0 || slot >= inventory.getSize()) return;

        GUIButton button = buttons.get(slot);

        if (button != null && button.hasListener()) {
            button.onClick(event, player);
        }
    }

    /**
     * Behandelt das Schließen des Inventars.
     * <p>
     * Entfernt die GUI aus der Map der geöffneten GUIs.
     * </p>
     *
     * @param event das {@link InventoryCloseEvent}
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            openGuis.remove(player.getUniqueId());
        }
    }

    /**
     * Registriert den GUI-Listener beim angegebenen Plugin.
     * <p>
     * Diese Methode muss einmal beim Plugin-Start aufgerufen werden,
     * um die Event-Behandlung für alle GUIs zu aktivieren.
     * </p>
     *
     * @param plugin das Plugin, bei dem der Listener registriert werden soll
     */
    public static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new GUI(Component.empty(), 1), plugin);
    }

    /**
     * Builder-Klasse für die Erstellung von GUI-Instanzen.
     * <p>
     * Ermöglicht die fluide Konfiguration von GUIs mit verschiedenen
     * Titel-Formaten und Größen.
     * </p>
     *
     * @see GUI
     */
    public static class Builder {

        /**
         * Der Titel der GUI als Component.
         */
        protected Component title;

        /**
         * Die Anzahl der Reihen (Standard: 0).
         */
        protected int rows = 0;

        /**
         * Setzt die Anzahl der Reihen für die GUI.
         *
         * @param rows die Anzahl der Reihen (1-6)
         * @return diese Builder-Instanz für Method-Chaining
         */
        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        /**
         * Setzt den Titel der GUI als einfachen Text.
         *
         * @param title der Titel als String
         * @return diese Builder-Instanz für Method-Chaining
         */
        public Builder setTitle(String title) {
            this.title = Component.text(title);
            return this;
        }

        /**
         * Setzt den Titel der GUI mit einem bestimmten Format-Typ.
         *
         * @param title     der Titel als String
         * @param titleType der {@link SerializerType} für die Formatierung
         * @return diese Builder-Instanz für Method-Chaining
         */
        public Builder setTitle(String title, SerializerType titleType){
            switch (titleType) {
                case MINI_MESSAGE -> this.title = MiniMessage.miniMessage().deserialize(title);
                case PLAIN -> this.title = Component.text(title);
                case LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().deserialize(title);
            }
            return this;
        }

        /**
         * Setzt den Titel der GUI mit einem benutzerdefinierten Legacy-Zeichen.
         *
         * @param title       der Titel als String mit Farbcodes
         * @param sectionChar das Zeichen für Farbcodes (z.B. '§' oder '&')
         * @return diese Builder-Instanz für Method-Chaining
         */
        public Builder setTitle(String title, char sectionChar) {
            this.title = LegacyComponentSerializer.legacy(sectionChar).deserialize(title);
            return this;
        }

        /**
         * Setzt den Titel der GUI als Component.
         *
         * @param title der Titel als {@link Component}
         * @return diese Builder-Instanz für Method-Chaining
         */
        public Builder setTitle(Component title) {
            this.title = title;
            return this;
        }

        /**
         * Erstellt die GUI mit den konfigurierten Einstellungen.
         *
         * @return die erstellte {@link GUI} oder {@code null}, wenn Titel oder Reihen nicht gesetzt wurden
         */
        public GUI build() {
            if (title == null || rows == 0) return null;
            return new GUI(title, rows);
        }
            }

}
