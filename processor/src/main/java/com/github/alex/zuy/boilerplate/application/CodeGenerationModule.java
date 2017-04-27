package com.github.alex.zuy.boilerplate.application;

import java.io.IOException;
import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.codegeneration.SourceCodeFormatter;
import com.github.alex.zuy.boilerplate.codegeneration.SourceCodeFormatterImpl;
import com.github.alex.zuy.boilerplate.codegeneration.SourceFilePublisher;
import com.github.alex.zuy.boilerplate.codegeneration.SourceFilePublisherImpl;
import com.github.alex.zuy.boilerplate.codegeneration.TemplateRenderer;
import com.github.alex.zuy.boilerplate.codegeneration.TemplateRendererImpl;
import com.github.alex.zuy.boilerplate.codegeneration.TypeDefinitionGenerator;
import com.github.alex.zuy.boilerplate.codegeneration.TypeDefinitionGeneratorImpl;
import dagger.Module;
import dagger.Provides;

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

    @Provides
    @Singleton
    SourceCodeFormatter provideSourceCodeFormatter() {
        return new SourceCodeFormatterImpl();
    }
}
