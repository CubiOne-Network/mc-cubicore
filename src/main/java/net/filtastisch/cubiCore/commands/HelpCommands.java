package net.filtastisch.cubiCore.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * Command class for help-related commands.
 * <p>
 * Registers and manages all help commands for the CubiCore plugin.
 * Commands are registered via CommandAPI and provide players with
 * information about plugin usage.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class HelpCommands {

    /**
     * Creates a new HelpCommands instance.
     * <p>
     * Automatically registers all help commands upon instantiation.
     */
    public HelpCommands(){
        this.registerHelpCommand();
    }

    /**
     * Registers the main help command.
     * <p>
     * Registers {@code /help} with the following properties:
     * <ul>
     *     <li>Command: {@code /help}</li>
     *     <li>Permission: {@code cubione.command.help}</li>
     *     <li>Namespace: {@code cubione}</li>
     *     <li>Executable by: Players only</li>
     * </ul>
     */
    private void registerHelpCommand() {
        new CommandAPICommand("help")
                .withPermission("cubione.command.help")
                .executesPlayer((player, args) -> {
                    player.sendMessage("Hier ist dann die Hilfe aufgelistet 8=====D");
                })
                .register("cubione");
    }

}
