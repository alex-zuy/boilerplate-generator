package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRenderer;
import org.alex.zuy.boilerplate.stringtemplate.StringTemplateRendererImpl;

@Module
public class StringTemplateModule {

    @Provides
    StringTemplateRenderer provideStringTemplateRenderer() {
        return new StringTemplateRendererImpl();
    }
}
