package org.alex.zuy.boilerplate.analysis;

import javax.inject.Singleton;

import dagger.Component;
import org.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;

@Component(modules = {BeanDomainAnalysisModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface BeanDomainAnalysisComponent {

    BeanClassAnalyser beanClassAnalyser();

    TypeAnalyser typeAnalyser();
}
