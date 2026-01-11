package net.filtastisch.cubiCore.guis;

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

public class GUI implements Listener {

    protected static final Map<UUID, GUI> openGuis = new HashMap<>();

    protected final Component title;
    protected final int rows;
    protected final Map<Integer, GUIButton> buttons;
    protected Inventory inventory;
    protected ToolbarConfig toolbar;

    public GUI(){
        this.title = Component.empty();
        this.rows = 0;
        this.buttons = new HashMap<>();
    }

    private GUI(Component title, int rows){
        this.rows = rows;
        this.buttons = new HashMap<>();
        this.title = title;
    }

    public GUI withToolbar(ToolbarConfig toolbar) {
        this.toolbar = toolbar;
        return this;
    }

    public GUI setButton(int slot, GUIButton button) {
        if (slot < 0 || slot >= rows * 9) throw new IllegalArgumentException("Slot should be between 0 and " + (rows * 9 - 1));
        if (toolbar != null && slot >= toolbar.getToolbarRow() * 9 && slot < (toolbar.getToolbarRow() + 1) * 9){
            throw new IllegalArgumentException("The slot " + slot + " is reserved for the toolbar!");
        }
        buttons.put(slot, button);
        inventory.setItem(slot, button.getIcon());
        return this;
    }

    public GUI removeButton(int slot) {
        buttons.remove(slot);
        inventory.setItem(slot, null);
        return this;
    }

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

    public void open(Player player) {
        renderToolbar();
        player.openInventory(inventory);
        openGuis.put(player.getUniqueId(), this);
    }

    public void refresh() {
        inventory.clear();
        buttons.forEach((slot, btn) -> inventory.setItem(slot, btn.getIcon()));
    }

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

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            openGuis.remove(player.getUniqueId());
        }
    }

    public static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new GUI(Component.empty(), 1), plugin);
    }

    public class Builder {

        protected Component title;
        protected int rows = 0;

        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = Component.text(title);
            return this;
        }

        public Builder setTitle(String title, TitleType titleType){
            switch (titleType) {
                case MINI_MESSAGE -> this.title = MiniMessage.miniMessage().deserialize(title);
                case PLAIN -> this.title = Component.text(title);
                case LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().deserialize(title);
            }
            return this;
        }

        public Builder setTitle(String title, char sectionChar) {
            this.title = LegacyComponentSerializer.legacy(sectionChar).deserialize(title);
            return this;
        }

        public Builder setTitle(Component title) {
            this.title = title;
            return this;
        }

        public GUI build() {
            if (title == null || rows == 0) return null;
            return new GUI(title, rows);
        }

        public enum TitleType {
            MINI_MESSAGE,
            LEGACY_AMPERSAND,
            PLAIN
        }

    }

}
