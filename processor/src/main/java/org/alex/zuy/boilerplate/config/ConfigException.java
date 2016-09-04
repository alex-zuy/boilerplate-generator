
package org.alex.zuy.boilerplate.config;

public class ConfigException extends RuntimeException {

    public ConfigException(final String message) {
        super(message);
    }

    public ConfigException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ConfigException(final Throwable throwable) {
        super(throwable);
    }
}
