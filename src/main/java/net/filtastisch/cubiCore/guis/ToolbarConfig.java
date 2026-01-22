package net.filtastisch.cubiCore.guis;

import lombok.Getter;

import java.util.Map;

/**
 * Configuration class for GUI toolbars.
 * <p>
 * Defines a row in a {@link GUI} to be used as toolbar. The toolbar contains
 * buttons typically used for navigation, actions, or other common functions.
 *
 * <p><b>Example:</b></p>
 * <pre>{@code
 * Map<Integer, GUIButton> buttons = new HashMap<>();
 * buttons.put(4, new GUIButton(closeIcon).withListener((e, p) -> p.closeInventory()));
 *
 * ToolbarConfig toolbar = new ToolbarConfig(5, buttons); // Toolbar in row 6 (0-indexed)
 * gui.withToolbar(toolbar);
 * }</pre>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see GUI
 * @see GUIButton
 */
public class ToolbarConfig {

    /**
     * Row where the toolbar is displayed (0-indexed).
     */
    @Getter
    private final int toolbarRow;

    /**
     * Map of toolbar buttons indexed by position (0-8).
     */
    private final Map<Integer, GUIButton> toolbarButtons;

    /**
     * Creates a new ToolbarConfig.
     *
     * @param toolbarRow     the row where the toolbar is placed (0-indexed)
     * @param toolbarButtons map of initial toolbar buttons
     */
    public ToolbarConfig(int toolbarRow, Map<Integer, GUIButton> toolbarButtons) {
        this.toolbarRow = toolbarRow;
        this.toolbarButtons = toolbarButtons;
    }

    /**
     * Sets a button at the specified toolbar position.
     *
     * @param pos    the position in the toolbar (0-8)
     * @param button the {@link GUIButton} to place
     * @return this ToolbarConfig instance for method chaining
     * @throws IllegalArgumentException if position is outside range 0-8
     */
    public ToolbarConfig setButton(int pos, GUIButton button) {
        if (pos < 0 || pos > 8) {
            throw new IllegalArgumentException("The Button position should be between 0 and 9");
        }
        toolbarButtons.put(pos, button);
        return this;
    }

    /**
     * Returns the button at the specified position.
     *
     * @param pos the position in the toolbar (0-8)
     * @return the {@link GUIButton} at that position, or {@code null} if empty
     */
    public GUIButton getButton(int pos) {
        return toolbarButtons.get(pos);
    }

}
