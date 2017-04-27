package com.github.alex.zuy.boilerplate.analysis;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import com.github.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import dagger.Component;

@Component(modules = {BeanDomainAnalysisModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface BeanDomainAnalysisComponent {

    BeanClassAnalyser beanClassAnalyser();

    TypeAnalyser typeAnalyser();
}
