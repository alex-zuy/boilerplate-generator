package org.alex.zuy.boilerplate.application;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.codegeneration.SourceFilePublisher;
import org.alex.zuy.boilerplate.codegeneration.SourceFilePublisherImpl;
import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer;
import org.alex.zuy.boilerplate.codegeneration.TemplateRendererImpl;
import org.alex.zuy.boilerplate.codegeneration.TypeDefinitionGenerator;
import org.alex.zuy.boilerplate.codegeneration.TypeDefinitionGeneratorImpl;

@Module
public class CodeGenerationModule {

    /* TODO: we will fix this later */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Provides
    @Singleton
    TemplateRenderer provideTemplateRenderer() {
        try {
            return new TemplateRendererImpl();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    TypeDefinitionGenerator provideTypeDefinitionGenerator(TypeDefinitionGeneratorImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    SourceFilePublisher provideSourceFilePublisher(SourceFilePublisherImpl impl) {
        return impl;
    }
}
