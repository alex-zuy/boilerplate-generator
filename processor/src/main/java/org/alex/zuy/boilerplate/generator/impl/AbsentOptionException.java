package org.alex.zuy.boilerplate.generator.impl;

public class AbsentOptionException extends RuntimeException {

    private final String optionName;

    public AbsentOptionException(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
