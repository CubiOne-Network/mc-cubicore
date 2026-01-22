package net.filtastisch.cubiCore.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
 * ItemBuilder for Minecraft 1.21.1 with Kyori Adventure API.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
 *     .setDisplayName("§6Legendary Sword")
 *     .setLore("§7A powerful weapon", "§7for brave warriors")
 *     .addEnchantment(Enchantment.SHARPNESS, 5)
 *     .setUnbreakable(true)
 *     .build();
 *
 * // Or with MiniMessage:
 * ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
 *     .setDisplayName("<gradient:#FF0000:#00FF00>Rainbow Sword</gradient>", SerializerType.MINI_MESSAGE)
 *     .build();
 * }</pre>
 */
public class ItemBuilder {

    private ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates a new ItemBuilder.
     * @param material the item material
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Creates a new ItemBuilder with amount.
     * @param material the item material
     * @param amount the item amount
     */
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    /**
     * Creates an ItemBuilder from an existing ItemStack.
     * @param item the ItemStack to copy
     */
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder withType(Material material) {
        this.item = this.item.withType(material);
        return this;
    }

    /**
     * Sets the display name (default: Legacy with §).
     * @param name the name (supports § color codes)
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setDisplayName(String name) {
        return setDisplayName(name, SerializerType.LEGACY_SECTION);
    }

    /**
     * Sets the display name with a specific format type.
     * @param name the name string
     * @param SerializerType the {@link SerializerType} for formatting
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setDisplayName(String name, SerializerType SerializerType) {
        if (meta != null && name != null) {
            Component component = parseComponent(name, SerializerType);
            meta.displayName(component);
        }
        return this;
    }

    /**
     * Sets the display name with a custom legacy character.
     * @param name the name with color codes
     * @param sectionChar the color code character (e.g. '§' or '&amp;')
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setDisplayName(String name, char sectionChar) {
        if (meta != null && name != null) {
            Component component = LegacyComponentSerializer.legacy(sectionChar).deserialize(name);
            meta.displayName(component);
        }
        return this;
    }

    /**
     * Sets the display name as Component.
     * @param name the name as {@link Component}
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setDisplayName(Component name) {
        if (meta != null && name != null) {
            meta.displayName(name);
        }
        return this;
    }

    /**
     * Sets the lore (default: Legacy with §).
     * @param lore the lore lines
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setLore(String... lore) {
        return setLore(SerializerType.LEGACY_SECTION, lore);
    }

    /**
     * Sets the lore with a specific format type.
     * @param SerializerType the {@link SerializerType} for formatting
     * @param lore the lore lines
     * @return this ItemBuilder for method chaining
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
     * Sets the lore as string list (default: Legacy with §).
     * @param lore the lore list
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setLore(List<String> lore) {
        return setLore(lore, SerializerType.LEGACY_SECTION);
    }

    /**
     * Sets the lore as string list with a specific format type.
     * @param lore the lore list
     * @param SerializerType the {@link SerializerType} for formatting
     * @return this ItemBuilder for method chaining
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
     * Sets the lore as Component list.
     * @param lore the lore as {@link Component} list
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setLoreComponents(List<Component> lore) {
        if (meta != null && lore != null) {
            meta.lore(lore);
        }
        return this;
    }

    /**
     * Adds lore lines (default: Legacy with §).
     * @param lore the lines to add
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder addLore(String... lore) {
        return addLore(SerializerType.LEGACY_SECTION, lore);
    }

    /**
     * Adds lore lines with a specific format type.
     * @param SerializerType the {@link SerializerType} for formatting
     * @param lore the lines to add
     * @return this ItemBuilder for method chaining
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
     * Adds a lore line as Component.
     * @param loreLine the line to add as {@link Component}
     * @return this ItemBuilder for method chaining
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

    private Component parseComponent(String text, SerializerType SerializerType) {
        return switch (SerializerType) {
            case MINI_MESSAGE -> MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
            case LEGACY_AMPERSAND -> LegacyComponentSerializer.legacyAmpersand().deserialize(text).decoration(TextDecoration.ITALIC, false);
            case LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false);
            case PLAIN -> Component.text(text).decoration(TextDecoration.ITALIC, false);
        };
    }

    /**
     * Sets the item amount.
     * @param amount the amount
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Adds an enchantment.
     * @param enchantment the enchantment
     * @param level the level
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (meta != null) {
            meta.addEnchant(enchantment, level, true);
        }
        return this;
    }

    /**
     * Removes an enchantment.
     * @param enchantment the enchantment to remove
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        if (meta != null) {
            meta.removeEnchant(enchantment);
        }
        return this;
    }

    /**
     * Adds ItemFlags (hides e.g. enchantments).
     * @param flags the ItemFlags
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        if (meta != null) {
            meta.addItemFlags(flags);
        }
        return this;
    }

    /**
     * Removes ItemFlags.
     * @param flags the ItemFlags to remove
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        if (meta != null) {
            meta.removeItemFlags(flags);
        }
        return this;
    }

    /**
     * Sets the item as unbreakable.
     * @param unbreakable whether the item should be unbreakable
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (meta != null) {
            meta.setUnbreakable(unbreakable);
        }
        return this;
    }

    /**
     * Sets the Custom Model Data value (for resource packs).
     * @param data the Custom Model Data value
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setCustomModelData(int data) {
        if (meta != null) {
            meta.setCustomModelData(data);
        }
        return this;
    }

    /**
     * Adds an attribute modifier (e.g. +5 Attack Damage).
     * @param attribute the attribute
     * @param modifier the modifier
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        if (meta != null) {
            meta.addAttributeModifier(attribute, modifier);
        }
        return this;
    }

    /**
     * Adds an attribute modifier with simple parameters (1.21+ compatible).
     * @param attribute the attribute
     * @param name the modifier name
     * @param amount the value
     * @param operation the operation (ADD_NUMBER, ADD_SCALAR, MULTIPLY_SCALAR_1)
     * @param slot the equipment slot
     * @return this ItemBuilder for method chaining
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
     * Sets the item damage (for Damageable items).
     * @param damage the damage value
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setDamage(int damage) {
        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(damage);
        }
        return this;
    }

    /**
     * Adds Armor Trim (1.20+).
     * @param pattern the trim pattern
     * @param material the trim material
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setArmorTrim(TrimPattern pattern, TrimMaterial material) {
        if (meta instanceof org.bukkit.inventory.meta.ArmorMeta) {
            org.bukkit.inventory.meta.ArmorMeta armorMeta = (org.bukkit.inventory.meta.ArmorMeta) meta;
            armorMeta.setTrim(new ArmorTrim(material, pattern));
        }
        return this;
    }

    /**
     * Stores custom data in PersistentDataContainer.
     * @param key the NamespacedKey
     * @param type the PersistentDataType
     * @param value the value
     * @return this ItemBuilder for method chaining
     */
    public <T, Z> ItemBuilder setPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, type, value);
        }
        return this;
    }

    /**
     * Sets the max stack size (1.21+).
     * @param maxStackSize the max stack size
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setMaxStackSize(int maxStackSize) {
        if (meta != null) {
            meta.setMaxStackSize(maxStackSize);
        }
        return this;
    }

    /**
     * Makes the item glow (glowing effect without enchantment).
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder setGlowing() {
        if (meta != null) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    /**
     * Hides all item information flags.
     * @return this ItemBuilder for method chaining
     */
    public ItemBuilder hideAllFlags() {
        if (meta != null) {
            meta.addItemFlags(ItemFlag.values());
        }
        return this;
    }

    /**
     * Sets the skull owner by player name (PLAYER_HEAD only).
     * @param ownerName the player name
     * @return this ItemBuilder for method chaining
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
     * Sets the skull owner by UUID (PLAYER_HEAD only).
     * @param uuid the player UUID
     * @return this ItemBuilder for method chaining
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
     * Sets the skull texture with Base64 (PLAYER_HEAD only).
     * Uses Paper API for custom textures.
     * @param base64Texture the Base64-encoded texture
     * @return this ItemBuilder for method chaining
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
     * Sets the skull texture with Base64 and custom UUID (PLAYER_HEAD only).
     * @param uuid the profile UUID
     * @param base64Texture the Base64-encoded texture
     * @return this ItemBuilder for method chaining
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
     * Builds the final ItemStack.
     * @return the built ItemStack
     */
    public ItemStack build() {
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Returns the ItemMeta (for advanced customization).
     * @return the ItemMeta
     */
    public ItemMeta getMeta() {
        return meta;
    }

    /**
     * Returns the current ItemStack (without building).
     * @return the ItemStack
     */
    public ItemStack getItem() {
        return item;
    }
}
