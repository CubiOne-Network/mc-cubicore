package net.filtastisch.cubiCore.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ItemBuilder für Minecraft 1.21.1 mit Kyori Adventure API
 *
 * Beispiel-Verwendung:
 * ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
 *     .setDisplayName("§6Legendäres Schwert")
 *     .setLore("§7Eine mächtige Waffe", "§7für tapfere Krieger")
 *     .addEnchantment(Enchantment.SHARPNESS, 5)
 *     .setUnbreakable(true)
 *     .build();
 *
 * Oder mit MiniMessage:
 * ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
 *     .setDisplayName("<gradient:#FF0000:#00FF00>Regenbogen Schwert</gradient>", SerializerType.MINI_MESSAGE)
 *     .build();
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Erstellt einen neuen ItemBuilder
     * @param material Das Material des Items
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Erstellt einen neuen ItemBuilder mit Anzahl
     * @param material Das Material des Items
     * @param amount Die Anzahl der Items
     */
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    /**
     * Erstellt einen ItemBuilder von einem existierenden ItemStack
     * @param item Das ItemStack zum Kopieren
     */
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }

    /**
     * Setzt den Anzeigenamen des Items (Standard: Legacy mit §)
     * @param name Der Name (unterstützt § Farbcodes)
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setDisplayName(String name) {
        return setDisplayName(name, SerializerType.LEGACY_SECTION);
    }

    /**
     * Setzt den Anzeigenamen des Items mit bestimmtem Format-Typ
     * @param name Der Name als String
     * @param SerializerType Der {@link SerializerType} für die Formatierung
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setDisplayName(String name, SerializerType SerializerType) {
        if (meta != null && name != null) {
            Component component = parseComponent(name, SerializerType);
            meta.displayName(component);
        }
        return this;
    }

    /**
     * Setzt den Anzeigenamen des Items mit benutzerdefiniertem Legacy-Zeichen
     * @param name Der Name mit Farbcodes
     * @param sectionChar Das Zeichen für Farbcodes (z.B. '§' oder '&')
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setDisplayName(String name, char sectionChar) {
        if (meta != null && name != null) {
            Component component = LegacyComponentSerializer.legacy(sectionChar).deserialize(name);
            meta.displayName(component);
        }
        return this;
    }

    /**
     * Setzt den Anzeigenamen des Items als Component
     * @param name Der Name als {@link Component}
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setDisplayName(Component name) {
        if (meta != null && name != null) {
            meta.displayName(name);
        }
        return this;
    }

    /**
     * Setzt die Lore (Beschreibung) des Items (Standard: Legacy mit §)
     * @param lore Die Lore-Zeilen
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setLore(String... lore) {
        return setLore(SerializerType.LEGACY_SECTION, lore);
    }

    /**
     * Setzt die Lore mit bestimmtem Format-Typ
     * @param SerializerType Der {@link SerializerType} für die Formatierung
     * @param lore Die Lore-Zeilen
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setLore(SerializerType SerializerType, String... lore) {
        if (meta != null && lore != null) {
            List<Component> components = Arrays.stream(lore)
                    .map(line -> parseComponent(line, SerializerType))
                    .collect(Collectors.toList());
            meta.lore(components);
        }
        return this;
    }

    /**
     * Setzt die Lore als String-Liste (Standard: Legacy mit §)
     * @param lore Die Lore-Liste
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setLore(List<String> lore) {
        return setLore(lore, SerializerType.LEGACY_SECTION);
    }

    /**
     * Setzt die Lore als String-Liste mit bestimmtem Format-Typ
     * @param lore Die Lore-Liste
     * @param SerializerType Der {@link SerializerType} für die Formatierung
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setLore(List<String> lore, SerializerType SerializerType) {
        if (meta != null && lore != null) {
            List<Component> components = lore.stream()
                    .map(line -> parseComponent(line, SerializerType))
                    .collect(Collectors.toList());
            meta.lore(components);
        }
        return this;
    }

    /**
     * Setzt die Lore als Component-Liste
     * @param lore Die Lore als {@link Component} Liste
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setLoreComponents(List<Component> lore) {
        if (meta != null && lore != null) {
            meta.lore(lore);
        }
        return this;
    }

    /**
     * Fügt Lore-Zeilen hinzu (Standard: Legacy mit §)
     * @param lore Die hinzuzufügenden Zeilen
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addLore(String... lore) {
        return addLore(SerializerType.LEGACY_SECTION, lore);
    }

    /**
     * Fügt Lore-Zeilen mit bestimmtem Format-Typ hinzu
     * @param SerializerType Der {@link SerializerType} für die Formatierung
     * @param lore Die hinzuzufügenden Zeilen
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addLore(SerializerType SerializerType, String... lore) {
        if (meta != null && lore != null) {
            List<Component> currentLore = meta.hasLore() ? meta.lore() : new ArrayList<>();
            if (currentLore == null) currentLore = new ArrayList<>();

            for (String line : lore) {
                currentLore.add(parseComponent(line, SerializerType));
            }
            meta.lore(currentLore);
        }
        return this;
    }

    /**
     * Fügt eine Lore-Zeile als Component hinzu
     * @param loreLine Die hinzuzufügende Zeile als {@link Component}
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addLore(Component loreLine) {
        if (meta != null && loreLine != null) {
            List<Component> currentLore = meta.hasLore() ? meta.lore() : new ArrayList<>();
            if (currentLore == null) currentLore = new ArrayList<>();
            currentLore.add(loreLine);
            meta.lore(currentLore);
        }
        return this;
    }

    /**
     * Hilfsmethode zum Parsen von Strings zu Components
     * @param text Der zu parsende Text
     * @param SerializerType Der Text-Typ
     * @return Die geparste Component
     */
    private Component parseComponent(String text, SerializerType SerializerType) {
        return switch (SerializerType) {
            case MINI_MESSAGE -> MiniMessage.miniMessage().deserialize(text);
            case LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().deserialize(text);
            case LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserialize(text);
            case PLAIN -> Component.text(text);
        };
    }

    /**
     * Setzt die Anzahl der Items
     * @param amount Die Anzahl
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Fügt eine Verzauberung hinzu
     * @param enchantment Die Verzauberung
     * @param level Das Level
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (meta != null) {
            meta.addEnchant(enchantment, level, true);
        }
        return this;
    }

    /**
     * Entfernt eine Verzauberung
     * @param enchantment Die zu entfernende Verzauberung
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        if (meta != null) {
            meta.removeEnchant(enchantment);
        }
        return this;
    }

    /**
     * Fügt ItemFlags hinzu (versteckt z.B. Verzauberungen)
     * @param flags Die ItemFlags
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        if (meta != null) {
            meta.addItemFlags(flags);
        }
        return this;
    }

    /**
     * Entfernt ItemFlags
     * @param flags Die zu entfernenden ItemFlags
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        if (meta != null) {
            meta.removeItemFlags(flags);
        }
        return this;
    }

    /**
     * Setzt das Item als unzerstörbar
     * @param unbreakable Ob das Item unzerstörbar sein soll
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (meta != null) {
            meta.setUnbreakable(unbreakable);
        }
        return this;
    }

    /**
     * Setzt den Custom Model Data Wert (für Resource Packs)
     * @param data Der Custom Model Data Wert
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setCustomModelData(int data) {
        if (meta != null) {
            meta.setCustomModelData(data);
        }
        return this;
    }

    /**
     * Fügt einen Attribut-Modifier hinzu (z.B. +5 Attack Damage)
     * @param attribute Das Attribut
     * @param modifier Der Modifier
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        if (meta != null) {
            meta.addAttributeModifier(attribute, modifier);
        }
        return this;
    }

    /**
     * Fügt einen Attribut-Modifier mit einfachen Parametern hinzu (1.21+ kompatibel)
     * @param attribute Das Attribut
     * @param name Der Name des Modifiers
     * @param amount Der Wert
     * @param operation Die Operation (ADD_NUMBER, ADD_SCALAR, MULTIPLY_SCALAR_1)
     * @param slot Der Equipment Slot
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder addAttributeModifier(Attribute attribute, String name, double amount,
                                            AttributeModifier.Operation operation, EquipmentSlot slot) {
        if (meta != null) {
            NamespacedKey key = new NamespacedKey("itembuilder", name.toLowerCase().replace(" ", "_"));
            AttributeModifier modifier = new AttributeModifier(
                    key, amount, operation, slot.getGroup()
            );
            meta.addAttributeModifier(attribute, modifier);
        }
        return this;
    }

    /**
     * Setzt den Schaden des Items (für Damageable Items)
     * @param damage Der Schadenswert
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setDamage(int damage) {
        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(damage);
        }
        return this;
    }

    /**
     * Fügt Armor Trim hinzu (1.20+)
     * @param pattern Das Trim Pattern
     * @param material Das Trim Material
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setArmorTrim(TrimPattern pattern, TrimMaterial material) {
        if (meta instanceof org.bukkit.inventory.meta.ArmorMeta) {
            org.bukkit.inventory.meta.ArmorMeta armorMeta = (org.bukkit.inventory.meta.ArmorMeta) meta;
            armorMeta.setTrim(new ArmorTrim(material, pattern));
        }
        return this;
    }

    /**
     * Speichert Custom Data im PersistentDataContainer
     * @param key Der NamespacedKey
     * @param type Der PersistentDataType
     * @param value Der Wert
     * @return Der ItemBuilder für Method Chaining
     */
    public <T, Z> ItemBuilder setPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, type, value);
        }
        return this;
    }

    /**
     * Setzt die maximale Stack-Größe (1.21+)
     * @param maxStackSize Die maximale Stack-Größe
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setMaxStackSize(int maxStackSize) {
        if (meta != null) {
            meta.setMaxStackSize(maxStackSize);
        }
        return this;
    }

    /**
     * Macht das Item zu einem Glowing Item (leuchtender Effekt ohne Verzauberung)
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setGlowing() {
        if (meta != null) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    /**
     * Versteckt alle Item-Informationen
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder hideAllFlags() {
        if (meta != null) {
            meta.addItemFlags(ItemFlag.values());
        }
        return this;
    }

    /**
     * Setzt den Skull-Owner nach Spielername (nur für PLAYER_HEAD)
     * @param ownerName Der Spielername
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setSkullOwner(String ownerName) {
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            OfflinePlayer player = Bukkit.getOfflinePlayer(ownerName);
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    /**
     * Setzt den Skull-Owner nach UUID (nur für PLAYER_HEAD)
     * @param uuid Die UUID des Spielers
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setSkullOwner(UUID uuid) {
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    /**
     * Setzt die Skull-Textur mit Base64 (nur für PLAYER_HEAD)
     * Verwendet das Paper API für Custom Textures
     * @param base64Texture Die Base64-kodierte Textur
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setSkullTexture(String base64Texture) {
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.getProperties().add(new ProfileProperty("textures", base64Texture));
            skullMeta.setPlayerProfile(profile);
        }
        return this;
    }

    /**
     * Setzt die Skull-Textur mit Base64 und einer Custom UUID (nur für PLAYER_HEAD)
     * @param uuid Die UUID für das Profil
     * @param base64Texture Die Base64-kodierte Textur
     * @return Der ItemBuilder für Method Chaining
     */
    public ItemBuilder setSkullTexture(UUID uuid, String base64Texture) {
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            PlayerProfile profile = Bukkit.createProfile(uuid);
            profile.getProperties().add(new ProfileProperty("textures", base64Texture));
            skullMeta.setPlayerProfile(profile);
        }
        return this;
    }

    /**
     * Baut das finale ItemStack
     * @return Das fertige ItemStack
     */
    public ItemStack build() {
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Gibt das ItemMeta zurück (für erweiterte Anpassungen)
     * @return Das ItemMeta
     */
    public ItemMeta getMeta() {
        return meta;
    }

    /**
     * Gibt das aktuelle ItemStack zurück (ohne zu bauen)
     * @return Das ItemStack
     */
    public ItemStack getItem() {
        return item;
    }
}