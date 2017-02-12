package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.metadatasupport.SupportClassesGenerator;
import org.alex.zuy.boilerplate.metadatasupport.SupportClassesGeneratorImpl;

@Module
public class MetadataSupportModule {

    @Provides
    @Singleton
    SupportClassesGenerator provideSupportClassesGenerator() {
        return new SupportClassesGeneratorImpl();
    }
}
