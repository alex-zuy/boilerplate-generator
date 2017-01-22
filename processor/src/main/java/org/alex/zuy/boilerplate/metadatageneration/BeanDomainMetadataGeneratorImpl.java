package org.alex.zuy.boilerplate.metadatageneration;

import java.util.Set;
import javax.inject.Inject;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import org.alex.zuy.boilerplate.codegeneration.TypeGenerator;
import org.alex.zuy.boilerplate.collector.DomainClassesCollector;
import org.alex.zuy.boilerplate.collector.DomainConfig;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public class BeanDomainMetadataGeneratorImpl implements BeanDomainMetadataGenerator {

    private TypeGenerator typeGenerator;

    private BeanDomainAnalyser beanDomainAnalyser;

    private BeanDomainProcessor beanDomainProcessor;

    private DomainClassesCollector domainClassesCollector;

    @Inject
    public BeanDomainMetadataGeneratorImpl(TypeGenerator typeGenerator, BeanDomainAnalyser beanDomainAnalyser,
        BeanDomainProcessor beanDomainProcessor, DomainClassesCollector domainClassesCollector) {
        this.typeGenerator = typeGenerator;
        this.beanDomainAnalyser = beanDomainAnalyser;
        this.beanDomainProcessor = beanDomainProcessor;
        this.domainClassesCollector = domainClassesCollector;
    }

    @Override
    public void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        MetadataGenerationStyle style) {

        Set<TypeElement> typeElements = domainClassesCollector.collect(domainConfig,
            roundContext.getRoundEnvironment());
        BeanDomain beanDomain = beanDomainAnalyser.analyse(typeElements);
        TypeSetDeclaration typeSetDeclaration = beanDomainProcessor.processDomain(beanDomain);
        for (TypeDeclaration typeDeclaration : typeSetDeclaration.getTypes()) {
            typeGenerator.generateType(typeDeclaration);
        }
    }
}
