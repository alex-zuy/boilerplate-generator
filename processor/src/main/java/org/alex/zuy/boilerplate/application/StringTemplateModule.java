package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRenderer;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRendererImpl;

@Module
public class StringTemplateModule {

    @Provides
    @Singleton
    StringTemplateRenderer provideStringTemplateRenderer() {
        return new StringTemplateRendererImpl();
    }
}
