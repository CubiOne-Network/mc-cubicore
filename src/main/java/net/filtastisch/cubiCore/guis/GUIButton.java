package net.filtastisch.cubiCore.guis;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GUIButton {

    @Getter
    private final ItemStack icon;
    @Getter
    private BiConsumer<InventoryClickEvent, Player> clickListener;

    public GUIButton(ItemStack icon){
        this.icon = icon;
    }

    public GUIButton withListener(BiConsumer<InventoryClickEvent, Player> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public void onClick(InventoryClickEvent event, Player player) {
        if (clickListener != null) {
            clickListener.accept(event, player);
        }
    }

    public boolean hasListener() {
        return clickListener != null;
    }

}
