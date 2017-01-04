package org.alex.zuy.boilerplate.utils;

import java.util.stream.IntStream;

public final class StringUtils {

    private StringUtils() {

    }

    public static String capitalizeLowerCamelcaseName(String name) {
        if (name.isEmpty()) {
            return name;
        }
        else {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }

    public static String decapitalizeUpperCamelcaseName(String name) {
        if (name.isEmpty()) {
            return name;
        }
        else if (name.length() == 1) {
            return name.toLowerCase();
        }
        else {
            return IntStream.range(0, name.length())
                .filter(idx -> Character.isLowerCase(name.charAt(idx)))
                .boxed()
                .findFirst()
                .map(firstLowercaseLetterIdx -> {
                    if (firstLowercaseLetterIdx > 1) {
                        return name;
                    }
                    else {
                        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
                    }
                })
                .orElse(name);
        }
    }
}
