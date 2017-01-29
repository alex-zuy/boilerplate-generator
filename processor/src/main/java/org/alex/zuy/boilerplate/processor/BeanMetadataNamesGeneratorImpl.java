package org.alex.zuy.boilerplate.processor;

import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.utils.StringUtils;

public class BeanMetadataNamesGeneratorImpl implements BeanMetadataNamesGenerator {

    private static final String SUFFIX_CLASS_NAME_PROPERTIES = "Properties";

    private static final String SUFFIX_CLASS_NAME_RELATIONSHIPS = "Relationships";

    @Override
    public String makeBeanPropertiesClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        return simpleBeanClassName + SUFFIX_CLASS_NAME_PROPERTIES;
    }

    @Override
    public String makeBeanRelationshipsClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        return simpleBeanClassName + SUFFIX_CLASS_NAME_RELATIONSHIPS;
    }

    @Override
    public String makeBeanPropertyConstantName(String propertyName, MetadataGenerationStyle style) {
        return StringUtils.camelcaseToUpperSnakeCase(propertyName);
    }
}
