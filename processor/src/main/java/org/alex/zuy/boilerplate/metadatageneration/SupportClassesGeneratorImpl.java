package org.alex.zuy.boilerplate.metadatageneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import org.alex.zuy.boilerplate.codegeneration.ImmutableTemplateRenderingTask;
import org.alex.zuy.boilerplate.codegeneration.ImmutableTypeImplementation;
import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer;
import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer.TemplateRenderingTask;
import org.alex.zuy.boilerplate.codegeneration.TypeGenerator.TypeImplementation;

public class SupportClassesGeneratorImpl implements SupportClassesGenerator {

    private static final String TEMPLATE_PROPERTY_CHAIN_NODE_CLASS = "propertyChainNode.ftl";

    private static final String PROPERTY_CHAIN_NODE_CLASS_NAME = "PropertyChainNode";

    private interface ModelRefs {

        String PACKAGE_NAME = "packageName";

        String CLASS_NAME = "className";
    }

    private TemplateRenderer templateRenderer;

    @Inject
    public SupportClassesGeneratorImpl(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    /* We`ll fix this soon. */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Override
    public TypeImplementation generateSupportClasses(SupportClassesConfig config) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(ModelRefs.PACKAGE_NAME, config.getBasePackage());
            data.put(ModelRefs.CLASS_NAME, PROPERTY_CHAIN_NODE_CLASS_NAME);
            TemplateRenderingTask task = ImmutableTemplateRenderingTask.of(TEMPLATE_PROPERTY_CHAIN_NODE_CLASS, data);
            String sourceCode = templateRenderer.renderTemplate(task);

            return ImmutableTypeImplementation.builder()
                .sourceCode(sourceCode)
                .simpleName(PROPERTY_CHAIN_NODE_CLASS_NAME)
                .packageName(config.getBasePackage())
                .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
