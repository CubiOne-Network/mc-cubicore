package net.filtastisch.cubiCore.utils;

/**
 * Constants class for global setting keys.
 * <p>
 * Contains static constants used as keys for accessing global settings
 * in the database and the {@code globalSettings} map.
 *
 * <p><b>Usage:</b></p>
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
     * Key for the chat prefix.
     * <p>
     * Used to retrieve the prefix displayed before plugin chat messages.
     */
    public static String CHAT_PREFIX = "chat_prefix";

}
