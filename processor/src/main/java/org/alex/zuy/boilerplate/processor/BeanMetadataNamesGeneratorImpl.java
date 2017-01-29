package org.alex.zuy.boilerplate.processor;

import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;

import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRenderer;
import org.alex.zuy.boilerplate.utils.StringUtils;

public class BeanMetadataNamesGeneratorImpl implements BeanMetadataNamesGenerator {

    private static final String PLACEHOLDER_BEAN_CLASS_NAME = "beanClassName";

    private StringTemplateRenderer templateRenderer;

    @Inject
    public BeanMetadataNamesGeneratorImpl(StringTemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public String makeBeanPropertiesClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        Map<String, String> data = Collections.singletonMap(PLACEHOLDER_BEAN_CLASS_NAME, simpleBeanClassName);
        return templateRenderer.render(style.getPropertyClassNameTemplate(), data);
    }

    @Override
    public String makeBeanRelationshipsClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        Map<String, String> data = Collections.singletonMap(PLACEHOLDER_BEAN_CLASS_NAME, simpleBeanClassName);
        return templateRenderer.render(style.getRelationshipsClassNameTemplate(), data);
    }

    @Override
    public String makeBeanPropertyConstantName(String propertyName, MetadataGenerationStyle style) {
        switch (style.getStringConstantStyle()) {
            case UPPERCASE:
                return StringUtils.camelcaseToUpperSnakeCase(propertyName);
            case CAMELCASE:
                return propertyName;
            default:
                String message = String.format("Unsupported %s enumeration value %s",
                    MetadataGenerationStyle.StringConstantStyle.class.getName(), style.getStringConstantStyle());
                throw new IllegalArgumentException(message);
        }
    }
}
