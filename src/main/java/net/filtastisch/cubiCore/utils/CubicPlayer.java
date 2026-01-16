package net.filtastisch.cubiCore.utils;

import eu.cloudnetservice.modules.bridge.player.NetworkServiceInfo;

import java.util.UUID;

/**
 * Repräsentiert einen Spieler im CubiCore-System.
 * <p>
 * Diese Klasse dient als Wrapper für Spieler-Daten und bietet
 * erweiterte Funktionalitäten für die Spielerverwaltung im
 * CubiCore-Plugin.
 * </p>
 * <p>
 * <b>Hinweis:</b> Diese Klasse befindet sich noch in der Entwicklung.
 * </p>
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
