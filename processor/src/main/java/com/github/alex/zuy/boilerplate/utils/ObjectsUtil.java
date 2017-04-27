package com.github.alex.zuy.boilerplate.utils;

import java.util.function.BiPredicate;

public final class ObjectsUtil {

    private ObjectsUtil() { }

    public static <T> boolean equals(T thisObject, Object otherObject, BiPredicate<T, T> equator) {
        return otherObject != null && (thisObject == otherObject || thisObject.getClass().equals(otherObject.getClass())
            && equator.test(thisObject, (T) otherObject));
    }
}
