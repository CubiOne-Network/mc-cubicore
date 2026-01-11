package net.filtastisch.cubiCore.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * Befehlsklasse für Hilfe-bezogene Befehle.
 * <p>
 * Diese Klasse registriert und verwaltet alle Hilfe-Befehle des CubiCore-Plugins.
 * Die Befehle werden über die CommandAPI registriert und bieten Spielern
 * Informationen zur Verwendung des Plugins.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class HelpCommands {

    /**
     * Erstellt eine neue Instanz der HelpCommands-Klasse.
     * <p>
     * Bei der Instanziierung werden automatisch alle Hilfe-Befehle registriert.
     * </p>
     */
    public HelpCommands(){
        this.registerHelpCommand();
    }

    /**
     * Registriert den Haupthilfebefehl.
     * <p>
     * Registriert den {@code /help}-Befehl mit folgenden Eigenschaften:
     * <ul>
     *     <li>Befehl: {@code /help}</li>
     *     <li>Permission: {@code cubione.command.help}</li>
     *     <li>Namespace: {@code cubione}</li>
     *     <li>Ausführbar von: Nur Spieler</li>
     * </ul>
     * </p>
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
