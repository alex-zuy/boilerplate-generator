package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.metadatasupport.SupportClassesGenerator;
import org.alex.zuy.boilerplate.metadatasupport.SupportClassesGeneratorImpl;

@Module
public class MetadataSupportModule {

    @Provides
    SupportClassesGenerator provideSupportClassesGenerator() {
        return new SupportClassesGeneratorImpl();
    }
}
