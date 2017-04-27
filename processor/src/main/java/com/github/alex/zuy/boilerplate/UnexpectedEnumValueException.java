package com.github.alex.zuy.boilerplate;

public class UnexpectedEnumValueException extends IllegalArgumentException {

    public <T extends Enum<T>> UnexpectedEnumValueException(T enumValue) {
        super(String.format("Unexpected value \"%s\" of enum \"%s\"", enumValue, enumValue.getClass().getName()));
    }
}
