package org.alex.zuy.boilerplate.utils;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() { }

    public static <T> T getFirst(Collection<T> collection) {
        return collection.isEmpty() ? null : collection.iterator().next();
    }
}
