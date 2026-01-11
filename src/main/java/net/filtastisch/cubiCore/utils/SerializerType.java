package net.filtastisch.cubiCore.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Enumeration der unterstützten Serializer-Formate.
 */
public enum SerializerType {
    /**
     * MiniMessage-Format für moderne Text-Formatierung.
     *
     * @see MiniMessage
     */
    MINI_MESSAGE,

    /**
     * Legacy-Format mit '&' als Farbcode-Zeichen.
     */
    LEGACY_AMPERSAND,

    /**
     * Legacy-Format mit '§' als Farbcode-Zeichen.
     */
    LEGACY_SECTION,
    /**
     * Einfacher Text ohne Formatierung.
     */
    PLAIN
}
