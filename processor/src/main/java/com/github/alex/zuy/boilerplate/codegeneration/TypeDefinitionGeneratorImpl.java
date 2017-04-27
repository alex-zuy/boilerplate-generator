package com.github.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import com.github.alex.zuy.boilerplate.codegeneration.TemplateRenderer.TemplateRenderingTask;
import com.github.alex.zuy.boilerplate.sourcemodel.ImmutableTypeDefinition;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDescription;

public class TypeDefinitionGeneratorImpl implements TypeDefinitionGenerator {

    private static final String TEMPLATE_TYPE_DECLARATION = "typeDeclaration.ftl";

    private static final String TEMPLATE_TYPE_DECLARATION_HEADER = "typeDeclarationHeader.ftl";

    private interface ModelRefs {

        String TYPE_DECLARATION = "type";

        String TYPE_FORMATTER = "typeFormatter";

        String TYPES_TO_IMPORT = "typesToImport";
    }

    private TemplateRenderer templateRenderer;

    private SourceCodeFormatter sourceCodeFormatter;

    @Inject
    public TypeDefinitionGeneratorImpl(TemplateRenderer templateRenderer,
        SourceCodeFormatter sourceCodeFormatter) {
        this.templateRenderer = templateRenderer;
        this.sourceCodeFormatter = sourceCodeFormatter;
    }

    @Override
    public TypeDefinition generateType(TypeDescription typeDeclaration) {
        try {
            TypeImportResolver importResolver = new TypeImportResolver();
            TypeFormatter typeFormatter = new TypeFormatter(importResolver);

            Map<String, Object> data = new HashMap<>();
            data.put(ModelRefs.TYPE_DECLARATION, typeDeclaration);
            data.put(ModelRefs.TYPE_FORMATTER, typeFormatter);
            TemplateRenderingTask task = ImmutableTemplateRenderingTask.of(TEMPLATE_TYPE_DECLARATION, data);
            String typeDeclarationSource = templateRenderer.renderTemplate(task);

            Map<String, Object> headerData = new HashMap<>();
            headerData.put(ModelRefs.TYPES_TO_IMPORT, importResolver.getImportedTypes());
            headerData.put(ModelRefs.TYPE_DECLARATION, typeDeclaration);
            String typeHeaderSource = templateRenderer.renderTemplate(
                ImmutableTemplateRenderingTask.of(TEMPLATE_TYPE_DECLARATION_HEADER, headerData));

            String completeSource = typeHeaderSource.concat(typeDeclarationSource);

            String formattedSource = sourceCodeFormatter.formatSource(completeSource);

            return ImmutableTypeDefinition.builder()
                .sourceCode(formattedSource)
                .simpleName(typeDeclaration.getSimpleName())
                .packageName(typeDeclaration.getPackageName())
                .build();
        }
        catch (IOException e) {
            throw new TypeGenerationException(e);
        }
    }
}
