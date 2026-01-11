package net.filtastisch.cubiCore.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/**
 * Listener für Bukkit Plugin-Nachrichten.
 * <p>
 * Diese Klasse implementiert den {@link PluginMessageListener} und ermöglicht
 * die Kommunikation zwischen dem Server und verbundenen Clients über
 * Plugin-Message-Channels.
 * </p>
 * <p>
 * Plugin-Nachrichten werden häufig für die Kommunikation mit Proxy-Servern
 * (wie BungeeCord/Velocity) oder für clientseitige Modifikationen verwendet.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see PluginMessageListener
 */
public class PluginMessageListenerIntegration implements PluginMessageListener {

    /**
     * Wird aufgerufen, wenn eine Plugin-Nachricht empfangen wird.
     * <p>
     * Diese Methode verarbeitet eingehende Plugin-Nachrichten für einen bestimmten
     * Channel und Spieler.
     * </p>
     *
     * @param s      der Name des Channels, über den die Nachricht empfangen wurde
     * @param player der Spieler, der mit der Nachricht verknüpft ist
     * @param bytes  die Rohdaten der Nachricht als Byte-Array
     */
    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte @NotNull [] bytes) {



    }
}
