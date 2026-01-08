package net.filtastisch.cubiCore.listener;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import net.filtastisch.cubiCore.CubiCore;

public class ChannelMessageListener {

    @EventListener
    public void onChannelMessage(ChannelMessageReceiveEvent event) {
        if (event.channel().equals("cubicore_global_settings")) {
            if (event.message().equals("update_prefix")) {
                CubiCore.getInstance().loadOrUpdateSettings();
            }
        }
    }

}
