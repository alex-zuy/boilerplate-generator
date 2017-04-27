package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.stringtemplate.StringTemplateRenderer;
import com.github.alex.zuy.boilerplate.stringtemplate.StringTemplateRendererImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class StringTemplateModule {

    @Provides
    @Singleton
    StringTemplateRenderer provideStringTemplateRenderer() {
        return new StringTemplateRendererImpl();
    }
}
