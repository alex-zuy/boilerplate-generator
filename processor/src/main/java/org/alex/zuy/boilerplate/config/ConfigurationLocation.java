package org.alex.zuy.boilerplate.config;

import java.util.Optional;
import java.util.OptionalInt;

import org.immutables.value.Value;

@Value.Immutable
public interface ConfigurationLocation {

    OptionalInt getColumnNumber();

    OptionalInt getLineNumber();

    Optional<String> getConfigurationElement();
}
