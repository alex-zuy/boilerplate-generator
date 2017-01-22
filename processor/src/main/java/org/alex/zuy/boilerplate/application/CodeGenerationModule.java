package org.alex.zuy.boilerplate.application;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.codegeneration.TemplateRenderer;
import org.alex.zuy.boilerplate.codegeneration.TemplateRendererImpl;
import org.alex.zuy.boilerplate.codegeneration.TypeGenerator;
import org.alex.zuy.boilerplate.codegeneration.TypeGeneratorImpl;

@Module
public class CodeGenerationModule {

    /* TODO: we will fix this later */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Provides
    TemplateRenderer provideTemplateRenderer() {
        try {
            return new TemplateRendererImpl();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    TypeGenerator provideTypeGenerator(TypeGeneratorImpl impl) {
        return impl;
    }
}
