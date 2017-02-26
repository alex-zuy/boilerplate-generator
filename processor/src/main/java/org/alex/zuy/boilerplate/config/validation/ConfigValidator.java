package org.alex.zuy.boilerplate.config.validation;

import org.alex.zuy.boilerplate.config.ConfigException;

public interface ConfigValidator<T> {

    void validate(T config) throws ConfigException;
}
