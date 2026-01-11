package net.filtastisch.cubiCore.utils;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Eine erweiterte HashMap mit konfigurierbarem Standardwert.
 * <p>
 * Diese Klasse erweitert {@link HashMap} und überschreibt die {@link #get(Object)}-Methode,
 * um einen benutzerdefinierten Standardwert zurückzugeben, wenn ein Schlüssel nicht
 * in der Map vorhanden ist. Der Standardwert wird durch einen {@link Supplier} bereitgestellt.
 * </p>
 *
 * <p><b>Beispiel:</b></p>
 * <pre>{@code
 * CubiHashMap<String, String> map = new CubiHashMap<>(() -> "Nicht gefunden");
 * map.put("key", "value");
 * String result = map.get("key");        // Gibt "value" zurück
 * String missing = map.get("unknown");   // Gibt "Nicht gefunden" zurück
 * }</pre>
 *
 * @param <K> der Typ der Schlüssel in dieser Map
 * @param <V> der Typ der Werte in dieser Map
 * @author filtastisch
 * @version 1.0
 * @since 1.0
 * @see HashMap
 * @see Supplier
 */
public class CubiHashMap<K, V> extends HashMap<K, V> {

    /**
     * Der Supplier, der den Standardwert liefert, wenn ein Schlüssel nicht existiert.
     */
    private final Supplier<V> defaultSupplier;

    /**
     * Erstellt eine neue CubiHashMap mit dem angegebenen Standard-Supplier.
     *
     * @param defaultSupplier der {@link Supplier}, der den Standardwert liefert,
     *                        wenn ein Schlüssel nicht in der Map gefunden wird
     */
    public CubiHashMap(Supplier<V> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
    }

    /**
     * Gibt den Wert zurück, der dem angegebenen Schlüssel zugeordnet ist,
     * oder den Standardwert, wenn der Schlüssel nicht existiert.
     * <p>
     * Im Gegensatz zur Standard-{@link HashMap#get(Object)}-Methode, die {@code null}
     * zurückgibt, wenn ein Schlüssel nicht existiert, gibt diese Methode den Wert
     * zurück, der vom {@link #defaultSupplier} bereitgestellt wird.
     * </p>
     *
     * @param key der Schlüssel, dessen zugeordneter Wert zurückgegeben werden soll
     * @return den dem Schlüssel zugeordneten Wert oder den Standardwert,
     *         wenn der Schlüssel nicht existiert
     */
    @Override
    public V get(Object key) {
        return super.getOrDefault(key, defaultSupplier.get());
    }
}
