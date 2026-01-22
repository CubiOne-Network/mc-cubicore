package net.filtastisch.cubiCore.listener;

import net.filtastisch.cubiCore.CubiCore;
import net.filtastisch.cubiCore.utils.CloudNetUtils;
import net.filtastisch.cubiCore.utils.GlobalSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerReadyListener implements Listener {

    @EventHandler
    public void onServerReadyEvent(ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.STARTUP) return;

        Component message = LegacyComponentSerializer.legacySection().deserialize(CubiCore.getInstance().getGlobalSettings().get(GlobalSettings.CHAT_PREFIX))
                .appendSpace()
                .append(MiniMessage.miniMessage().deserialize("Der Server %server_name% ist bereit zum joinen!")
                        .replaceText(repl -> {
                            repl.match("%server_name%");
                            repl.replacement("CloudNetUtils.getWrapperConfiguration().serviceConfiguration().serviceId().name()");
                        }));

        CloudNetUtils.sendMessageToAllWithPermission(message, "*");
    }

}
