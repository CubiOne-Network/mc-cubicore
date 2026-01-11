package net.filtastisch.cubiCore.utils;

/**
 * Konstanten-Klasse für globale Einstellungsschlüssel.
 * <p>
 * Diese Klasse enthält statische Konstanten, die als Schlüssel für
 * den Zugriff auf globale Einstellungen in der Datenbank und der
 * {@code globalSettings}-Map verwendet werden.
 * </p>
 *
 * <p><b>Verwendung:</b></p>
 * <pre>{@code
 * String prefix = cubiCore.getGlobalSettings().get(GlobalSettings.CHAT_PREFIX);
 * }</pre>
 *
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see net.filtastisch.cubiCore.CubiCore#getGlobalSettings()
 */
public class GlobalSettings {

    /**
     * Der Schlüssel für das Chat-Präfix.
     * <p>
     * Dieser Wert wird verwendet, um das Präfix abzurufen, das vor
     * Chat-Nachrichten des Plugins angezeigt wird.
     * </p>
     */
    public static String CHAT_PREFIX = "chat_prefix";

}
