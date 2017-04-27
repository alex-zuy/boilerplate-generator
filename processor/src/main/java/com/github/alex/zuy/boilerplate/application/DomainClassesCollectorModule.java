package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.collector.DomainClassesCollector;
import com.github.alex.zuy.boilerplate.collector.DomainClassesCollectorImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class DomainClassesCollectorModule {

    @Provides
    @Singleton
    DomainClassesCollector provideDomainClassesCollector(DomainClassesCollectorImpl impl) {
        return impl;
    }
}
