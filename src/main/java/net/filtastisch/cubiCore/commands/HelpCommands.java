package net.filtastisch.cubiCore.commands;

import dev.jorel.commandapi.CommandAPICommand;

public class HelpCommands {

    public HelpCommands(){
        this.registerHelpCommand();
    }

    private void registerHelpCommand() {
        new CommandAPICommand("help")
                .withPermission("cubione.command.help")
                .executesPlayer((player, args) -> {
                    player.sendMessage("Hier ist dann die Hilfe aufgelistet 8=====D");
                })
                .register("cubione");
    }

}
