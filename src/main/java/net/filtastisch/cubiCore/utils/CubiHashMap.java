package net.filtastisch.cubiCore.utils;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Extended HashMap with configurable default value.
 * <p>
 * Extends {@link HashMap} and overrides {@link #get(Object)} to return
 * a custom default value when a key doesn't exist. The default value
 * is provided by a {@link Supplier}.
 *
 * <p><b>Example:</b></p>
 * <pre>{@code
 * CubiHashMap<String, String> map = new CubiHashMap<>(() -> "Not found");
 * map.put("key", "value");
 * String result = map.get("key");        // Returns "value"
 * String missing = map.get("unknown");   // Returns "Not found"
 * }</pre>
 *
 * @param <K> key type
 * @param <V> value type
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see HashMap
 * @see Supplier
 */
public class CubiHashMap<K, V> extends HashMap<K, V> {

    /**
     * Supplier providing the default value when a key doesn't exist.
     */
    private final Supplier<V> defaultSupplier;

    /**
     * Creates a new CubiHashMap with the specified default supplier.
     *
     * @param defaultSupplier the {@link Supplier} providing the default value
     *                        when a key is not found
     */
    public CubiHashMap(Supplier<V> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
    }

    /**
     * Returns the value for the specified key, or the default value if the key doesn't exist.
     * <p>
     * Unlike standard {@link HashMap#get(Object)} which returns {@code null} for missing keys,
     * this method returns the value from {@link #defaultSupplier}.
     *
     * @param key the key whose value should be returned
     * @return the value for the key, or the default value if key doesn't exist
     */
    @Override
    public V get(Object key) {
        return super.getOrDefault(key, defaultSupplier.get());
    }
}
