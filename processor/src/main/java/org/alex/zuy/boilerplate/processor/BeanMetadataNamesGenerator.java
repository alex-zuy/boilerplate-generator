package org.alex.zuy.boilerplate.processor;

import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;

public interface BeanMetadataNamesGenerator {

    String makeBeanPropertiesClassName(String simpleBeanClassName, MetadataGenerationStyle style);

    String makeBeanRelationshipsClassName(String simpleBeanClassName, MetadataGenerationStyle style);

    String makeBeanPropertyConstantName(String propertyName, MetadataGenerationStyle style);
}
