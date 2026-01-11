package net.filtastisch.cubiCore.guis;

import lombok.Getter;

import java.util.Map;

public class ToolbarConfig {

    @Getter
    private final int toolbarRow;
    private final Map<Integer, GUIButton> toolbarButtons;

    public ToolbarConfig(int toolbarRow, Map<Integer, GUIButton> toolbarButtons) {
        this.toolbarRow = toolbarRow;
        this.toolbarButtons = toolbarButtons;
    }

    public ToolbarConfig setButtons(int pos, GUIButton button) {
        if (pos < 0 || pos > 8) {
            throw new IllegalArgumentException("The Button position should be between 0 and 9");
        }
        toolbarButtons.put(pos, button);
        return this;
    }

    public GUIButton getButton(int pos) {
        return toolbarButtons.get(pos);
    }

}
