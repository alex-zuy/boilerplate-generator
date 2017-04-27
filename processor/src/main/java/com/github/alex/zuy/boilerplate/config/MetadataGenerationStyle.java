package com.github.alex.zuy.boilerplate.config;

import org.immutables.value.Value;

@Value.Immutable
public interface MetadataGenerationStyle {

    String getPropertyClassNameTemplate();

    String getRelationshipsClassNameTemplate();

    String getRelationshipsClassTerminalMethodNameTemplate();

    StringConstantStyle getStringConstantStyle();

    enum StringConstantStyle {

        CAMELCASE,

        UPPERCASE
    }
}
