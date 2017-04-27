package com.github.alex.zuy.boilerplate.metadatageneration;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.lang.model.element.TypeElement;

import com.github.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import com.github.alex.zuy.boilerplate.codegeneration.SourceFilePublisher;
import com.github.alex.zuy.boilerplate.codegeneration.TypeDefinitionGenerator;
import com.github.alex.zuy.boilerplate.collector.DomainClassesCollector;
import com.github.alex.zuy.boilerplate.config.DomainConfig;
import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.domain.BeanDomain;
import com.github.alex.zuy.boilerplate.metadatasupport.SupportClassesGenerator;
import com.github.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import com.github.alex.zuy.boilerplate.services.RoundContext;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDescription;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public class BeanDomainMetadataGeneratorImpl implements BeanDomainMetadataGenerator {

    private TypeDefinitionGenerator typeDefinitionGenerator;

    private BeanDomainAnalyser beanDomainAnalyser;

    private BeanDomainProcessor beanDomainProcessor;

    private DomainClassesCollector domainClassesCollector;

    private SupportClassesGenerator supportClassesGenerator;

    private SourceFilePublisher sourceFilePublisher;

    private boolean wasSupportClassesGenerated;

    @Inject
    public BeanDomainMetadataGeneratorImpl(TypeDefinitionGenerator typeDefinitionGenerator,
        BeanDomainAnalyser beanDomainAnalyser,
        BeanDomainProcessor beanDomainProcessor, DomainClassesCollector domainClassesCollector,
        SupportClassesGenerator supportClassesGenerator, SourceFilePublisher sourceFilePublisher) {
        this.typeDefinitionGenerator = typeDefinitionGenerator;
        this.beanDomainAnalyser = beanDomainAnalyser;
        this.beanDomainProcessor = beanDomainProcessor;
        this.domainClassesCollector = domainClassesCollector;
        this.supportClassesGenerator = supportClassesGenerator;
        this.sourceFilePublisher = sourceFilePublisher;
    }

    @Override
    public void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        SupportClassesConfig supportClassesConfig) {

        Set<TypeElement> typeElements = domainClassesCollector.collect(domainConfig,
            roundContext.getRoundEnvironment());
        BeanDomain beanDomain = beanDomainAnalyser.analyse(typeElements);
        TypeSetDeclaration typeSetDeclaration = beanDomainProcessor.processDomain(beanDomain, supportClassesConfig,
            domainConfig.generationStyle());

        if (!wasSupportClassesGenerated) {
            List<TypeDefinition> typeDefinitions = supportClassesGenerator.generateSupportClasses(supportClassesConfig);
            typeDefinitions.forEach(sourceFilePublisher::publish);
            wasSupportClassesGenerated = true;
        }

        for (TypeDescription typeDeclaration : typeSetDeclaration.getTypes()) {
            TypeDefinition typeDefinition = typeDefinitionGenerator.generateType(typeDeclaration);
            sourceFilePublisher.publish(typeDefinition);
        }
    }
}
