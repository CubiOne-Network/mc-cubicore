package net.filtastisch.cubiCore.guis;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * Repräsentiert einen klickbaren Button in einer {@link GUI}.
 * <p>
 * Ein GUIButton besteht aus einem {@link ItemStack} als visuelle Darstellung
 * und einem optionalen Click-Listener für die Behandlung von Klick-Events.
 * </p>
 *
 * <p><b>Beispiel:</b></p>
 * <pre>{@code
 * GUIButton button = new GUIButton(new ItemStack(Material.DIAMOND))
 *     .withListener((event, player) -> {
 *         player.sendMessage("Du hast den Diamant geklickt!");
 *     });
 * }</pre>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see GUI
 */
public class GUIButton {

    /**
     * Das ItemStack, das als Icon für diesen Button angezeigt wird.
     */
    @Getter
    private final ItemStack icon;

    /**
     * Der optionale Listener, der bei Klicks auf diesen Button ausgeführt wird.
     */
    @Getter
    private BiConsumer<InventoryClickEvent, Player> clickListener;

    /**
     * Erstellt einen neuen GUIButton mit dem angegebenen Icon.
     *
     * @param icon der {@link ItemStack}, der als Button-Icon angezeigt wird
     */
    public GUIButton(ItemStack icon){
        this.icon = icon;
    }

    /**
     * Fügt einen Click-Listener zum Button hinzu.
     * <p>
     * Der Listener erhält das {@link InventoryClickEvent} und den {@link Player},
     * der geklickt hat.
     * </p>
     *
     * @param clickListener der {@link BiConsumer}, der bei Klicks ausgeführt wird
     * @return diese GUIButton-Instanz für Method-Chaining
     */
    public GUIButton withListener(BiConsumer<InventoryClickEvent, Player> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * Führt den Click-Listener aus, wenn vorhanden.
     * <p>
     * Diese Methode wird von der {@link GUI} aufgerufen, wenn ein Spieler
     * auf diesen Button klickt.
     * </p>
     *
     * @param event  das {@link InventoryClickEvent}
     * @param player der {@link Player}, der geklickt hat
     */
    public void onClick(InventoryClickEvent event, Player player) {
        if (clickListener != null) {
            clickListener.accept(event, player);
        }
    }

    /**
     * Prüft, ob dieser Button einen Click-Listener hat.
     *
     * @return {@code true}, wenn ein Listener registriert ist, sonst {@code false}
     */
    public boolean hasListener() {
        return clickListener != null;
    }

}
