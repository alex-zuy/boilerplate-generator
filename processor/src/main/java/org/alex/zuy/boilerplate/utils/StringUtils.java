package org.alex.zuy.boilerplate.utils;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringUtils {

    private interface RegexCamelcaseBorders {

        Pattern LOWERCASE_AND_UPPERCASE_OR_DIGIT = Pattern.compile("([a-z])([A-Z0-9])");

        Pattern DIGIT_AND_LETTER = Pattern.compile("([0-9])([a-zA-Z])");

        Pattern MULTIPLE_UPPERCASE_AND_NEW_WORD = Pattern.compile("([A-Z]{2,})([A-Z0-9][a-z0-9])");
    }

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

    public static String camelcaseToUpperSnakeCase(String stringInCamelcase) {
        return Stream.of(
            RegexCamelcaseBorders.LOWERCASE_AND_UPPERCASE_OR_DIGIT,
            RegexCamelcaseBorders.DIGIT_AND_LETTER,
            RegexCamelcaseBorders.MULTIPLE_UPPERCASE_AND_NEW_WORD)
            .map(regex -> (Function<String, String>) (String string) -> regex.matcher(string).replaceAll("$1_$2"))
            .reduce(Function::andThen)
            .map(converter -> converter.apply(stringInCamelcase).toUpperCase())
            .orElse(stringInCamelcase);
    }
}
