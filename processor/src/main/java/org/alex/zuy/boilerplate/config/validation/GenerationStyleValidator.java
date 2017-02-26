package org.alex.zuy.boilerplate.config.validation;

import org.alex.zuy.boilerplate.config.ConfigException;
import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.config.MetadataGenerationStyleStringPlaceholders;

public class GenerationStyleValidator implements ConfigValidator<MetadataGenerationStyle> {

    private static final String PLACEHOLDER_PATTERN = "${%s}";

    private static final String PLACEHOLDER_CLASS_NAME = String.format(PLACEHOLDER_PATTERN,
        MetadataGenerationStyleStringPlaceholders.PLACEHOLDER_BEAN_CLASS_NAME);

    private static final String PLACEHOLDER_METHOD_NAME = String.format(PLACEHOLDER_PATTERN,
        MetadataGenerationStyleStringPlaceholders.PLACEHOLDER_BEAN_PROPERTY_NAME);

    @Override
    public void validate(MetadataGenerationStyle config) throws ConfigException {
        validateClassNamesTemplates(config);
        validateMethodNamesTemplates(config);
    }

    private void validateClassNamesTemplates(MetadataGenerationStyle config) {
        String[] classNameTemplates = {
            config.getPropertyClassNameTemplate(),
            config.getRelationshipsClassNameTemplate(),
        };
        for (String classNameTemplate : classNameTemplates) {
            if (!classNameTemplate.contains(classNameTemplate)) {
                throw new ConfigException(String.format("Invalid generation style configuration! "
                        + "Class name template '%s' does not contains class name placeholder('%s')",
                    classNameTemplate, PLACEHOLDER_CLASS_NAME));
            }
        }
    }

    private void validateMethodNamesTemplates(MetadataGenerationStyle config) {
        String[] methodNamesTemplates = {
            config.getRelationshipsClassTerminalMethodNameTemplate(),
        };
        for (String methodNameTemplate : methodNamesTemplates) {
            if (!methodNameTemplate.contains(PLACEHOLDER_METHOD_NAME)) {
                throw new ConfigException(String.format("Invalid generation style configuration!"
                        + " Method name template '%s' does not contains property name placeholder('%s')",
                    methodNameTemplate, PLACEHOLDER_METHOD_NAME));
            }
        }
    }
}
