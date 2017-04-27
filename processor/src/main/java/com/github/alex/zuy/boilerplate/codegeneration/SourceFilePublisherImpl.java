package com.github.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.io.Writer;
import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.JavaFileObject;

import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public class SourceFilePublisherImpl implements SourceFilePublisher {

    private Filer filer;

    @Inject
    public SourceFilePublisherImpl(ProcessorContext context) {
        filer = context.getFiler();
    }

    @Override
    public void publish(TypeDefinition typeDefinition) {
        try {
            String fileName = makeSourceFileName(typeDefinition.getSimpleName(), typeDefinition.getPackageName());
            JavaFileObject sourceFile = filer.createSourceFile(fileName);
            try (Writer writer = sourceFile.openWriter()) {
                writer.append(typeDefinition.getSourceCode());
            }
        }
        catch (IOException e) {
            throw new SourceFilePublishingException(e);
        }
    }

    private String makeSourceFileName(String simpleName, String packageName) {
        return packageName != null ? String.format("%s.%s", packageName, simpleName) : simpleName;
    }
}
