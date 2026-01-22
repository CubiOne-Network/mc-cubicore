package net.filtastisch.cubiCore.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for Bukkit plugin messages.
 * <p>
 * Implements {@link PluginMessageListener} to enable communication between
 * the server and connected clients via plugin message channels.
 * <p>
 * Plugin messages are commonly used for proxy server communication
 * (BungeeCord/Velocity) or client-side modifications.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see PluginMessageListener
 */
public class PluginMessageListenerIntegration implements PluginMessageListener {

    /**
     * Called when a plugin message is received.
     * <p>
     * Processes incoming plugin messages for a specific channel and player.
     *
     * @param s      the channel name the message was received on
     * @param player the player associated with the message
     * @param bytes  the raw message data as byte array
     */
    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte @NotNull [] bytes) {



    }
}
