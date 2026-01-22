package net.filtastisch.cubiCore.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Enumeration of supported serializer formats.
 */
public enum SerializerType {
    /**
     * MiniMessage format for modern text formatting.
     *
     * @see MiniMessage
     */
    MINI_MESSAGE,

    /**
     * Legacy format with '&' as color code character.
     */
    LEGACY_AMPERSAND,

    /**
     * Legacy format with '§' as color code character.
     */
    LEGACY_SECTION,

    /**
     * Plain text without formatting.
     */
    PLAIN
}
