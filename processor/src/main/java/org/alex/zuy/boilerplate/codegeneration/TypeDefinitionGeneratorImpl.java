package org.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer.TemplateRenderingTask;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableTypeDefinition;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public class TypeDefinitionGeneratorImpl implements TypeDefinitionGenerator {

    private static final String TEMPLATE_TYPE_DECLARATION = "typeDeclaration.ftl";

    private interface ModelRefs {

        String TYPE_DECLARATION = "type";

        String TYPE_FORMATTER = "typeFormatter";
    }

    private final TypeFormatter typeFormatter = new TypeFormatter();

    private TemplateRenderer templateRenderer;

    @Inject
    public TypeDefinitionGeneratorImpl(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public TypeDefinition generateType(TypeDeclaration typeDeclaration) {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelRefs.TYPE_DECLARATION, typeDeclaration);
        data.put(ModelRefs.TYPE_FORMATTER, typeFormatter);
        try {
            TemplateRenderingTask task = ImmutableTemplateRenderingTask.of(TEMPLATE_TYPE_DECLARATION, data);
            String generatedSource = templateRenderer.renderTemplate(task);
            return ImmutableTypeDefinition.builder()
                .sourceCode(generatedSource)
                .simpleName(typeDeclaration.getSimpleName())
                .packageName(typeDeclaration.getPackageName())
                .build();
        }
        catch (IOException e) {
            throw new TypeGenerationException(e);
        }
    }
}
