package net.filtastisch.cubiCore.utils;

import eu.cloudnetservice.modules.bridge.player.NetworkServiceInfo;

import java.util.UUID;

/**
 * Represents a player in the CubiCore system.
 * <p>
 * Wrapper for player data providing extended functionality
 * for player management in the CubiCore plugin.
 * <p>
 * <b>Note:</b> This class is still in development.
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 */
public class CubicPlayer {
    private final UUID uuid;

    public CubicPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isModProxy() {
        NetworkServiceInfo serviceInfo = CloudNetUtils.getOnlinePlayer(this.uuid).networkPlayerProxyInfo().networkService();
        return serviceInfo.taskName().equals("ModProxy");
    }

}
