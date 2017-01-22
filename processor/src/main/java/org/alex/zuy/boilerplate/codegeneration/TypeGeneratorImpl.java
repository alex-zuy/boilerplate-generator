package org.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;

public class TypeGeneratorImpl implements TypeGenerator {

    private static final String TEMPLATE_TYPE_DECLARATION = "typeDeclaration.ftl";

    private interface ModelRefs {

        String TYPE_DECLARATION = "type";

        String TYPE_FORMATTER = "typeFormatter";
    }

    private TypeFormatter typeFormatter = new TypeFormatter();

    private final Filer filer;

    private TemplateRenderer templateRenderer;

    public TypeGeneratorImpl(TemplateRenderer templateRenderer, ProcessorContext processorContext) {
        this.templateRenderer = templateRenderer;
        filer = processorContext.getFiler();
    }

    @Override
    public void generateType(TypeDeclaration typeDeclaration) {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelRefs.TYPE_DECLARATION, typeDeclaration);
        data.put(ModelRefs.TYPE_FORMATTER, typeFormatter);
        try {
            String generatedSource = templateRenderer.renderTemplate(TEMPLATE_TYPE_DECLARATION, data);
            String fileName = makeTypeSourceFileName(typeDeclaration.getSimpleName(), typeDeclaration.getPackageName());
            JavaFileObject fileObject = filer.createSourceFile(fileName);
            try (Writer writer = fileObject.openWriter()) {
                writer.append(generatedSource);
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
