package net.filtastisch.cubiCore.guis;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * Represents a clickable button in a {@link GUI}.
 * <p>
 * A GUIButton consists of an {@link ItemStack} as visual representation,
 * an optional click listener for handling click events, and a flag
 * determining whether clicks on this button should be cancelled.
 *
 * <p><b>Example:</b></p>
 * <pre>{@code
 * // Button with click cancel (default)
 * GUIButton button1 = new GUIButton(new ItemStack(Material.DIAMOND))
 *     .withListener((event, player) -> {
 *         player.sendMessage("You clicked the diamond!");
 *     });
 *
 * // Button without click cancel
 * GUIButton button2 = new GUIButton(new ItemStack(Material.CHEST))
 *     .withListener((event, player) -> {
 *         player.sendMessage("Items can be moved!");
 *     }, false);
 * }</pre>
 *
 * @author filtastisch
 * @version 2.0
 * @since 1.0
 * @see GUI
 */
public class GUIButton {

    /**
     * ItemStack displayed as the button icon.
     */
    @Getter
    private final ItemStack icon;

    /**
     * Optional listener executed when this button is clicked.
     */
    @Getter
    private BiConsumer<InventoryClickEvent, Player> clickListener;

    /**
     * Whether clicks on this button should be cancelled. Default is {@code true}.
     */
    @Getter
    private boolean cancelClick = true;

    /**
     * Whether this button should persist during GUI updates or page changes. Default is {@code false}.
     */
    @Getter
    private boolean sticky = false;

    /**
     * Creates a new GUIButton with the specified icon.
     *
     * @param icon the {@link ItemStack} displayed as button icon
     */
    public GUIButton(ItemStack icon){
        this.icon = icon;
    }

    /**
     * Adds a click listener to the button.
     * <p>
     * The listener receives the {@link InventoryClickEvent} and the {@link Player}
     * who clicked. Clicks are cancelled by default.
     *
     * @param clickListener the {@link BiConsumer} executed on click
     * @return this GUIButton instance for method chaining
     */
    public GUIButton withListener(BiConsumer<InventoryClickEvent, Player> clickListener) {
        this.clickListener = clickListener;
        this.cancelClick = true;
        return this;
    }

    /**
     * Adds a click listener with configurable cancel behavior.
     * <p>
     * The listener receives the {@link InventoryClickEvent} and the {@link Player}
     * who clicked.
     *
     * @param clickListener the {@link BiConsumer} executed on click
     * @param cancelClick {@code true} to cancel the click, {@code false} otherwise
     * @return this GUIButton instance for method chaining
     */
    public GUIButton withListener(BiConsumer<InventoryClickEvent, Player> clickListener, boolean cancelClick) {
        this.clickListener = clickListener;
        this.cancelClick = cancelClick;
        return this;
    }

    /**
     * Sets whether clicks on this button should be cancelled.
     *
     * @param cancelClick {@code true} to cancel clicks, {@code false} otherwise
     * @return this GUIButton instance for method chaining
     */
    public GUIButton setCancelClick(boolean cancelClick) {
        this.cancelClick = cancelClick;
        return this;
    }

    /**
     * Sets whether this button should persist during GUI updates or page changes.
     * <p>
     * Sticky buttons are not removed or overwritten during {@link PageableGUI#nextPage()},
     * {@link PageableGUI#previousPage()}, and {@link GUI#updateGui}.
     *
     * @param sticky {@code true} to persist the button, {@code false} otherwise
     * @return this GUIButton instance for method chaining
     */
    public GUIButton setSticky(boolean sticky) {
        this.sticky = sticky;
        return this;
    }

    /**
     * Executes the click listener if present.
     * <p>
     * Called by {@link GUI} when a player clicks on this button.
     *
     * @param event  the {@link InventoryClickEvent}
     * @param player the {@link Player} who clicked
     */
    public void onClick(InventoryClickEvent event, Player player) {
        if (clickListener != null) {
            clickListener.accept(event, player);
        }
    }

    /**
     * Checks if this button has a click listener.
     *
     * @return {@code true} if a listener is registered, {@code false} otherwise
     */
    public boolean hasListener() {
        return clickListener != null;
    }
}
