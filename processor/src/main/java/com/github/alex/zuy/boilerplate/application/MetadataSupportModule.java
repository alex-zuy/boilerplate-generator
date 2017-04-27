package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.metadatasupport.SupportClassesGenerator;
import com.github.alex.zuy.boilerplate.metadatasupport.SupportClassesGeneratorImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class MetadataSupportModule {

    @Provides
    @Singleton
    SupportClassesGenerator provideSupportClassesGenerator() {
        return new SupportClassesGeneratorImpl();
    }
}
