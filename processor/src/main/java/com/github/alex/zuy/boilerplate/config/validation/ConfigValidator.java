package com.github.alex.zuy.boilerplate.config.validation;

import com.github.alex.zuy.boilerplate.config.ConfigException;

public interface ConfigValidator<T> {

    void validate(T config) throws ConfigException;
}
