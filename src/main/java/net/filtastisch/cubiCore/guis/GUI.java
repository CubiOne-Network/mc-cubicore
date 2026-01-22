package net.filtastisch.cubiCore.guis;

import lombok.Getter;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Flexible GUI class for creating inventory-based user interfaces.
 * <p>
 * Allows creating, managing, and displaying interactive GUI inventories for players.
 * Features include:
 * <ul>
 *     <li>Dynamic button placement</li>
 *     <li>Optional toolbars</li>
 *     <li>Various title formats (Plain, MiniMessage, Legacy)</li>
 *     <li>Event handling for clicks and closing</li>
 *     <li>Configurable click cancel types</li>
 * </ul>
 *
 * <p><b>Builder usage:</b></p>
 * <pre>{@code
 * GUI gui = new GUI.Builder()
 *     .setTitle("My Menu")
 *     .setRows(3)
 *     .setClickCancelType(ClickCancelType.TOP_INV)
 *     .build();
 * gui.setButton(13, new GUIButton(itemStack));
 * gui.open(player);
 * }</pre>
 *
 * @author filtastisch
 * @version 2.0
 * @since 1.0
 * @see GUIButton
 * @see ToolbarConfig
 */
public class GUI implements Listener {

    /**
     * Defines which inventory clicks should be cancelled.
     */
    public enum ClickCancelType {
        /**
         * Only the clicked slot is cancelled (default).
         */
        SLOT,

        /**
         * All clicks in the top inventory (GUI) are cancelled.
         */
        TOP_INV,

        /**
         * All clicks in the bottom inventory (player inventory) are cancelled.
         */
        BOTTOM_INV,

        /**
         * All clicks in both inventories are cancelled.
         */
        FULL_INV
    }

    /**
     * Map storing all currently open GUIs by player UUID.
     */
    protected static final Map<UUID, GUI> openGuis = new HashMap<>();

    /**
     * GUI title as Adventure Component.
     */
    protected final Component title;

    /**
     * Number of rows in the GUI (1-6).
     */
    protected final int rows;

    /**
     * Map of buttons indexed by slot position.
     */
    protected final Map<Integer, GUIButton> buttons;

    /**
     * Bukkit inventory representing this GUI.
     */
    protected Inventory inventory;

    /**
     * Optional toolbar configuration.
     */
    protected ToolbarConfig toolbar;

    /**
     * Click cancel type for this GUI.
     */
    @Getter
    protected ClickCancelType clickCancelType;

    /**
     * Creates an empty GUI instance.
     * <p>
     * Mainly used for listener registration.
     * Use {@link Builder} for creating functional GUIs.
     */
    GUI(){
        this.title = Component.empty();
        this.rows = 0;
        this.buttons = new HashMap<>();
        this.clickCancelType = ClickCancelType.SLOT;
    }

    /**
     * Creates a GUI with the specified title and row count.
     *
     * @param title the GUI title as {@link Component}
     * @param rows  the number of rows (1-6)
     */
    protected GUI(Component title, int rows){
        this.rows = rows;
        this.buttons = new HashMap<>();
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.clickCancelType = ClickCancelType.SLOT;
    }

    /**
     * Adds a toolbar to the GUI.
     *
     * @param toolbar the {@link ToolbarConfig} for the GUI
     * @return this GUI instance for method chaining
     */
    public GUI withToolbar(ToolbarConfig toolbar) {
        this.toolbar = toolbar;
        return this;
    }

    /**
     * Sets the click cancel type for this GUI.
     *
     * @param clickCancelType the {@link ClickCancelType}
     * @return this GUI instance for method chaining
     */
    public GUI setClickCancelType(ClickCancelType clickCancelType) {
        this.clickCancelType = clickCancelType;
        return this;
    }

    /**
     * Places a button at the specified slot position.
     *
     * @param slot   the slot position (0 to rows*9-1)
     * @param button the {@link GUIButton} to place
     * @return this GUI instance for method chaining
     * @throws IllegalArgumentException if the slot is out of range or reserved for toolbar
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
     * Returns all buttons using the specified ItemStack as icon.
     *
     * @param itemStack the ItemStack to search for
     * @return array of {@link GUIButton}s using that ItemStack
     */
    public GUIButton[] getButtons(ItemStack itemStack) {
        return buttons.values().stream()
                .filter(button -> button.getIcon().equals(itemStack))
                .toArray(GUIButton[]::new);
    }

    /**
     * Returns the button at the specified slot position.
     *
     * @param slot the slot position
     * @return the {@link GUIButton} at that position, or {@code null} if empty
     */
    public GUIButton getButton(int slot) {
        return buttons.get(slot);
    }

    /**
     * Removes a button from the specified slot position.
     *
     * @param slot the slot position to remove the button from
     * @return this GUI instance for method chaining
     */
    public GUI removeButton(int slot) {
        buttons.remove(slot);
        inventory.setItem(slot, null);
        return this;
    }

    /**
     * Renders the toolbar in the GUI.
     * <p>
     * Places all toolbar buttons in the configured toolbar row.
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
     * Opens the GUI for the specified player.
     * <p>
     * Renders the toolbar (if present), opens the inventory, and registers
     * this GUI as open for the player.
     *
     * @param player the player to open the GUI for
     */
    public void open(Player player) {
        renderToolbar();
        player.openInventory(inventory);
        openGuis.put(player.getUniqueId(), this);
    }

    /**
     * Refreshes the GUI by re-rendering all buttons.
     * <p>
     * Clears the inventory and places all registered buttons again.
     */
    public void refresh() {
        inventory.clear();
        buttons.forEach((slot, btn) -> inventory.setItem(slot, btn.getIcon()));
    }

    /**
     * Updates the GUI with new buttons without reopening the inventory.
     * <p>
     * Replaces all existing buttons with the specified new buttons.
     * Slots not in the map will be cleared.
     * Sticky buttons ({@link GUIButton#isSticky()}) are preserved
     * unless the new button is also sticky.
     *
     * @param newButtons map of new buttons (slot -> GUIButton)
     */
    public void updateGui(Map<Integer, GUIButton> newButtons) {
        Map<Integer, GUIButton> stickyButtons = new HashMap<>();
        buttons.forEach((slot, btn) -> {
            if (btn.isSticky()) {
                stickyButtons.put(slot, btn);
            }
        });

        for (int slot : buttons.keySet()) {
            if (!newButtons.containsKey(slot) && !stickyButtons.containsKey(slot)) {
                inventory.setItem(slot, null);
            }
        }

        buttons.clear();
        buttons.putAll(stickyButtons);

        newButtons.forEach((slot, button) -> {
            if (slot >= 0 && slot < rows * 9) {
                if (stickyButtons.containsKey(slot) && !button.isSticky()) {
                    return;
                }
                buttons.put(slot, button);
                inventory.setItem(slot, button.getIcon());
            }
        });
    }

    /**
     * Handles inventory click events.
     * <p>
     * Prevents inventory interactions based on the configured
     * {@link ClickCancelType} and button settings.
     *
     * @param event the {@link InventoryClickEvent}
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        GUI gui = openGuis.get(player.getUniqueId());
        if (gui == null) return;

        if (event.getInventory() != gui.inventory) return;

        boolean isTopInventory = event.getClickedInventory() == gui.inventory;
        int slot = event.getRawSlot();

        boolean shouldCancel = switch (gui.clickCancelType) {
            case SLOT -> {
                if (isTopInventory && slot >= 0 && slot < gui.inventory.getSize()) {
                    GUIButton button = gui.buttons.get(slot);
                    yield button != null && button.isCancelClick();
                }
                yield false;
            }
            case TOP_INV -> isTopInventory;
            case BOTTOM_INV -> !isTopInventory;
            case FULL_INV -> true;
        };

        if (shouldCancel) {
            event.setCancelled(true);
        }

        if (isTopInventory && slot >= 0 && slot < gui.inventory.getSize()) {
            GUIButton button = gui.buttons.get(slot);
            if (button != null && button.hasListener()) {
                button.onClick(event, player);
            }
        }
    }

    /**
     * Handles inventory close events.
     * <p>
     * Removes the GUI from the open GUIs map.
     *
     * @param event the {@link InventoryCloseEvent}
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            openGuis.remove(player.getUniqueId());
        }
    }

    /**
     * Registers the GUI listener with the specified plugin.
     * <p>
     * Must be called once at plugin startup to enable event handling for all GUIs.
     *
     * @param plugin the plugin to register the listener with
     */
    public static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new GUI(), plugin);
    }

    /**
     * Builder class for creating GUI instances.
     * <p>
     * Allows fluent configuration of GUIs with various title formats and sizes.
     *
     * @see GUI
     */
    public static class Builder {

        /**
         * GUI title as Component.
         */
        protected Component title;

        /**
         * Number of rows (default: 0).
         */
        protected int rows = 0;

        /**
         * Click cancel type (default: SLOT).
         */
        protected ClickCancelType clickCancelType = ClickCancelType.SLOT;

        /**
         * Sets the number of rows for the GUI.
         *
         * @param rows the number of rows (1-6)
         * @return this Builder instance for method chaining
         */
        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        /**
         * Sets the click cancel type for the GUI.
         *
         * @param clickCancelType the {@link ClickCancelType}
         * @return this Builder instance for method chaining
         */
        public Builder setClickCancelType(ClickCancelType clickCancelType) {
            this.clickCancelType = clickCancelType;
            return this;
        }

        /**
         * Sets the GUI title as plain text.
         *
         * @param title the title string
         * @return this Builder instance for method chaining
         */
        public Builder setTitle(String title) {
            this.title = Component.text(title);
            return this;
        }

        /**
         * Sets the GUI title with a specific format type.
         *
         * @param title     the title string
         * @param titleType the {@link SerializerType} for formatting
         * @return this Builder instance for method chaining
         */
        public Builder setTitle(String title, SerializerType titleType){
            switch (titleType) {
                case MINI_MESSAGE -> this.title = MiniMessage.miniMessage().deserialize(title);
                case PLAIN -> this.title = Component.text(title);
                case LEGACY_AMPERSAND -> this.title = LegacyComponentSerializer.legacyAmpersand().deserialize(title);
            }
            return this;
        }

        /**
         * Sets the GUI title with a custom legacy character.
         *
         * @param title       the title string with color codes
         * @param sectionChar the character for color codes (e.g. '§' or '&')
         * @return this Builder instance for method chaining
         */
        public Builder setTitle(String title, char sectionChar) {
            this.title = LegacyComponentSerializer.legacy(sectionChar).deserialize(title);
            return this;
        }

        /**
         * Sets the GUI title as Component.
         *
         * @param title the title as {@link Component}
         * @return this Builder instance for method chaining
         */
        public Builder setTitle(Component title) {
            this.title = title;
            return this;
        }

        /**
         * Builds the GUI with the configured settings.
         *
         * @return the created {@link GUI}, or {@code null} if title or rows are not set
         */
        public GUI build() {
            if (title == null || rows == 0) return null;
            return new GUI(title, rows).setClickCancelType(clickCancelType);
        }
    }
}
