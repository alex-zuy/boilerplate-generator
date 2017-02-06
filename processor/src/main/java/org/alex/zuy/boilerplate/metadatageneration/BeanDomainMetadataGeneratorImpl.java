package org.alex.zuy.boilerplate.metadatageneration;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import org.alex.zuy.boilerplate.codegeneration.SourceFilePublisher;
import org.alex.zuy.boilerplate.codegeneration.TypeDefinitionGenerator;
import org.alex.zuy.boilerplate.collector.DomainClassesCollector;
import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGenerator.SupportClassesConfig;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.alex.zuy.boilerplate.sourcemodel.TypeDescription;
import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

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
