package net.filtastisch.cubiCore.listener;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import net.filtastisch.cubiCore.CubiCore;

/**
 * Listener für CloudNet Channel-Nachrichten.
 * <p>
 * Diese Klasse empfängt und verarbeitet Nachrichten, die über den CloudNet
 * Channel-Messaging-Dienst gesendet werden. Sie ermöglicht die netzwerkweite
 * Kommunikation zwischen verschiedenen Server-Instanzen.
 * </p>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent
 */
public class ChannelMessageListener {

    /**
     * Verarbeitet eingehende Channel-Nachrichten von CloudNet.
     * <p>
     * Reagiert auf Nachrichten im Channel {@code cubicore_global_settings}:
     * <ul>
     *     <li>{@code update_prefix} - Lädt die globalen Einstellungen neu</li>
     * </ul>
     * </p>
     *
     * @param event das Event, das die Channel-Nachricht enthält
     */
    @EventListener
    public void onChannelMessage(ChannelMessageReceiveEvent event) {
        if (event.channel().equals("cubicore_global_settings")) {
            if (event.message().equals("update_prefix")) {
                CubiCore.getInstance().loadOrUpdateSettings();
            }
        }
    }

}
