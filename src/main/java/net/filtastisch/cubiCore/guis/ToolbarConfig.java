package net.filtastisch.cubiCore.guis;

import lombok.Getter;

import java.util.Map;

/**
 * Konfigurationsklasse für GUI-Toolbars.
 * <p>
 * Eine ToolbarConfig definiert eine Reihe in einer {@link GUI}, die als
 * Toolbar verwendet wird. Die Toolbar enthält Buttons, die typischerweise
 * für Navigation, Aktionen oder andere häufig verwendete Funktionen genutzt werden.
 * </p>
 *
 * <p><b>Beispiel:</b></p>
 * <pre>{@code
 * Map<Integer, GUIButton> buttons = new HashMap<>();
 * buttons.put(4, new GUIButton(closeIcon).withListener((e, p) -> p.closeInventory()));
 *
 * ToolbarConfig toolbar = new ToolbarConfig(5, buttons); // Toolbar in Reihe 6 (0-basiert)
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
     * Die Reihe, in der die Toolbar angezeigt wird (0-basiert).
     */
    @Getter
    private final int toolbarRow;

    /**
     * Eine Map der Toolbar-Buttons, indiziert nach Position (0-8).
     */
    private final Map<Integer, GUIButton> toolbarButtons;

    /**
     * Erstellt eine neue ToolbarConfig.
     *
     * @param toolbarRow     die Reihe, in der die Toolbar platziert wird (0-basiert)
     * @param toolbarButtons eine Map der initialen Toolbar-Buttons
     */
    public ToolbarConfig(int toolbarRow, Map<Integer, GUIButton> toolbarButtons) {
        this.toolbarRow = toolbarRow;
        this.toolbarButtons = toolbarButtons;
    }

    /**
     * Setzt einen Button an der angegebenen Position in der Toolbar.
     *
     * @param pos    die Position in der Toolbar (0-8)
     * @param button der zu platzierende {@link GUIButton}
     * @return diese ToolbarConfig-Instanz für Method-Chaining
     * @throws IllegalArgumentException wenn die Position außerhalb des Bereichs 0-8 liegt
     */
    public ToolbarConfig setButtons(int pos, GUIButton button) {
        if (pos < 0 || pos > 8) {
            throw new IllegalArgumentException("The Button position should be between 0 and 9");
        }
        toolbarButtons.put(pos, button);
        return this;
    }

    /**
     * Gibt den Button an der angegebenen Position zurück.
     *
     * @param pos die Position in der Toolbar (0-8)
     * @return der {@link GUIButton} an der Position oder {@code null}, wenn kein Button vorhanden ist
     */
    public GUIButton getButton(int pos) {
        return toolbarButtons.get(pos);
    }

}
