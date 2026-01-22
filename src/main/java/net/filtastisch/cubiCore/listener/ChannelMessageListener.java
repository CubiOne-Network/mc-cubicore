package net.filtastisch.cubiCore.listener;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import net.filtastisch.cubiCore.CubiCore;

/**
 * Listener for CloudNet channel messages.
 * <p>
 * Receives and processes messages sent via CloudNet's channel messaging service,
 * enabling network-wide communication between server instances.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent
 */
public class ChannelMessageListener {

    /**
     * Processes incoming CloudNet channel messages.
     * <p>
     * Handles messages on channel {@code cubicore_global_settings}:
     * <ul>
     *     <li>{@code update_prefix} - Reloads global settings</li>
     * </ul>
     *
     * @param event the event containing the channel message
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
