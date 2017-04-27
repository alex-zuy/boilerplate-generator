package com.github.alex.zuy.boilerplate.processor;

import com.github.alex.zuy.boilerplate.config.MetadataGenerationStyle;

public interface BeanMetadataNamesGenerator {

    String makeBeanPropertiesClassName(String simpleBeanClassName, MetadataGenerationStyle style);

    String makeBeanRelationshipsClassName(String simpleBeanClassName, MetadataGenerationStyle style);

    String makeBeanRelationshipsTerminalMethodName(String propertyName, MetadataGenerationStyle style);

    String makeBeanPropertyConstantName(String propertyName, MetadataGenerationStyle style);
}
