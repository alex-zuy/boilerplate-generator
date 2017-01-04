package org.alex.zuy.boilerplate.utils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public final class CollectionUtils {

    private CollectionUtils() { }

    public static <T> T getFirst(Collection<T> collection) {
        return collection.isEmpty() ? null : collection.iterator().next();
    }

    public static <K, V> MapBuilder<K, V> getMapBuilder(Supplier<Map<K, V>> mapSupplier) {
        return new MapBuilder<K, V>() {

            private Map<K, V> map = mapSupplier.get();

            @Override
            public MapBuilder<K, V> put(K key, V value) {
                map.put(key, value);
                return this;
            }

            @Override
            public Map<K, V> build() {
                return map;
            }
        };
    }

    public interface MapBuilder<K, V> {

        MapBuilder<K, V> put(K key, V value);

        Map<K, V> build();
    }
}
