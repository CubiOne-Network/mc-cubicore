package net.filtastisch.cubiCore.utils;

import java.util.HashMap;
import java.util.function.Supplier;

public class CubiHashMap<K, V> extends HashMap<K, V> {

    private final Supplier<V> defaultSupplier;

    public CubiHashMap(Supplier<V> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
    }

    @Override
    public V get(Object key) {
        return super.getOrDefault(key, defaultSupplier.get());
    }
}
