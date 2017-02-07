package org.alex.zuy.boilerplate.config;

import org.immutables.value.Value;

@Value.Immutable
public interface MetadataGenerationStyle {

    String getPropertyClassNameTemplate();

    String getRelationshipsClassNameTemplate();

    String getRelationshipsClassTerminalMethodName();

    StringConstantStyle getStringConstantStyle();

    enum StringConstantStyle {

        CAMELCASE,

        UPPERCASE
    }
}
