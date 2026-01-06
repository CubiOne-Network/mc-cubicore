package net.filtastisch.cubiCore;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.filtastisch.cubiCore.commands.HelpCommands;
import org.bukkit.plugin.java.JavaPlugin;

public final class CubiCore extends JavaPlugin {

    @Getter
    private static CubiCore instance;

    @Override
    public void onEnable() {
        instance = this;
        this.registerApis();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    private void registerApis() {
        CommandAPI.onEnable();
    }

    private void registerCommands() {
        new HelpCommands();
    }

}
