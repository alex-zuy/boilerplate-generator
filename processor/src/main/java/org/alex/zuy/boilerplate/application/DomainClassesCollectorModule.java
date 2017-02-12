package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.collector.DomainClassesCollector;
import org.alex.zuy.boilerplate.collector.DomainClassesCollectorImpl;

@Module
public class DomainClassesCollectorModule {

    @Provides
    @Singleton
    DomainClassesCollector provideDomainClassesCollector(DomainClassesCollectorImpl impl) {
        return impl;
    }
}
