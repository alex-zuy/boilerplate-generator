package org.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.tools.JavaFileObject;

import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer.TemplateRenderingTask;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;

public class TypeGeneratorImpl implements TypeGenerator {

    private static final String TEMPLATE_TYPE_DECLARATION = "typeDeclaration.ftl";

    private interface ModelRefs {

        String TYPE_DECLARATION = "type";

        String TYPE_FORMATTER = "typeFormatter";
    }

    private final TypeFormatter typeFormatter = new TypeFormatter();

    private ProcessorContext processorContext;

    private TemplateRenderer templateRenderer;

    @Inject
    public TypeGeneratorImpl(ProcessorContext processorContext,
        TemplateRenderer templateRenderer) {
        this.processorContext = processorContext;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public void generateType(TypeDeclaration typeDeclaration) {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelRefs.TYPE_DECLARATION, typeDeclaration);
        data.put(ModelRefs.TYPE_FORMATTER, typeFormatter);
        try {
            TemplateRenderingTask task = ImmutableTemplateRenderingTask.of(TEMPLATE_TYPE_DECLARATION, data);
            String generatedSource = templateRenderer.renderTemplate(task);
            TypeImplementation implementation = ImmutableTypeImplementation.builder()
                .sourceCode(generatedSource)
                .simpleName(typeDeclaration.getSimpleName())
                .packageName(typeDeclaration.getPackageName())
                .build();
            generateType(implementation);
        }
        catch (IOException e) {
            throw new TypeGenerationException(e);
        }
    }

    @Override
    public void generateType(TypeImplementation typeImplementation) {
        try {
            String fileName = makeTypeSourceFileName(typeImplementation.getSimpleName(),
                typeImplementation.getPackageName());
            JavaFileObject fileObject = processorContext.getFiler().createSourceFile(fileName);
            try (Writer writer = fileObject.openWriter()) {
                writer.append(typeImplementation.getSourceCode());
            }
        }
        catch (IOException e) {
            throw new TypeGenerationException(e);
        }
    }

    private String makeTypeSourceFileName(String simpleName, String packageName) {
        return packageName != null
            ? String.format("%s.%s", packageName, simpleName)
            : simpleName;
    }
}
