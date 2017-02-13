package org.alex.zuy.boilerplate.config;

public class ConfigValidationException extends ConfigException {

    private final ConfigurationLocation configurationLocation;

    public ConfigValidationException(String message, ConfigurationLocation configurationLocation) {
        super(message);
        this.configurationLocation = configurationLocation;
    }

    public ConfigValidationException(String message, Throwable throwable, ConfigurationLocation configurationLocation) {
        super(message, throwable);
        this.configurationLocation = configurationLocation;
    }

    public ConfigurationLocation getConfigurationLocation() {
        return configurationLocation;
    }
}
