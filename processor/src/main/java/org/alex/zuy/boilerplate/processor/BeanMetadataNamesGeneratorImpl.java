package org.alex.zuy.boilerplate.processor;

import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;

import org.alex.zuy.boilerplate.UnexpectedEnumValueException;
import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.config.MetadataGenerationStyleStringPlaceholders;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRenderer;
import org.alex.zuy.boilerplate.utils.StringUtils;

public class BeanMetadataNamesGeneratorImpl implements BeanMetadataNamesGenerator {

    private StringTemplateRenderer templateRenderer;

    @Inject
    public BeanMetadataNamesGeneratorImpl(StringTemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public String makeBeanPropertiesClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        Map<String, String> data = Collections.singletonMap(
            MetadataGenerationStyleStringPlaceholders.PLACEHOLDER_BEAN_CLASS_NAME, simpleBeanClassName);
        return templateRenderer.render(style.getPropertyClassNameTemplate(), data);
    }

    @Override
    public String makeBeanRelationshipsClassName(String simpleBeanClassName, MetadataGenerationStyle style) {
        Map<String, String> data = Collections.singletonMap(
            MetadataGenerationStyleStringPlaceholders.PLACEHOLDER_BEAN_CLASS_NAME, simpleBeanClassName);
        return templateRenderer.render(style.getRelationshipsClassNameTemplate(), data);
    }

    @Override
    public String makeBeanRelationshipsTerminalMethodName(String propertyName, MetadataGenerationStyle style) {
        Map<String, String> data = Collections.singletonMap(
            MetadataGenerationStyleStringPlaceholders.PLACEHOLDER_BEAN_PROPERTY_NAME, propertyName);
        return templateRenderer.render(style.getRelationshipsClassTerminalMethodNameTemplate(), data);
    }

    @Override
    public String makeBeanPropertyConstantName(String propertyName, MetadataGenerationStyle style) {
        switch (style.getStringConstantStyle()) {
            case UPPERCASE:
                return StringUtils.camelcaseToUpperSnakeCase(propertyName);
            case CAMELCASE:
                return propertyName;
            default:
                throw new UnexpectedEnumValueException(style.getStringConstantStyle());
        }
    }
}
