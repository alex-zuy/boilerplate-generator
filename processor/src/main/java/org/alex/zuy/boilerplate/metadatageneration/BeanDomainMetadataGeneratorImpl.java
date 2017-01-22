package org.alex.zuy.boilerplate.metadatageneration;

import java.util.Set;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.analysis.BeanClassAnalyser;
import org.alex.zuy.boilerplate.analysis.BeanClassAnalyserImpl;
import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyserImpl;
import org.alex.zuy.boilerplate.analysis.TypeAnalyser;
import org.alex.zuy.boilerplate.analysis.TypeAnalyserImpl;
import org.alex.zuy.boilerplate.codegeneration.TypeGenerator;
import org.alex.zuy.boilerplate.collector.DomainClassesCollectorImpl;
import org.alex.zuy.boilerplate.collector.DomainConfig;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessorImpl;
import org.alex.zuy.boilerplate.processor.BeanMetadataNamesGenerator;
import org.alex.zuy.boilerplate.processor.BeanMetadataNamesGeneratorImpl;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public class BeanDomainMetadataGeneratorImpl implements BeanDomainMetadataGenerator {

    private ProcessorContext processorContext;

    private TypeGenerator typeGenerator;

    private BeanDomainAnalyser beanDomainAnalyser;

    private BeanDomainProcessor beanDomainProcessor;

    public BeanDomainMetadataGeneratorImpl(ProcessorContext processorContext, TypeGenerator typeGenerator) {
        this.processorContext = processorContext;
        this.typeGenerator = typeGenerator;

        TypeAnalyser typeAnalyser = new TypeAnalyserImpl(processorContext.getTypeUtils());
        BeanClassAnalyser beanClassAnalyser = new BeanClassAnalyserImpl(typeAnalyser);
        beanDomainAnalyser = new BeanDomainAnalyserImpl(beanClassAnalyser);
        BeanMetadataNamesGenerator beanMetadataNamesGenerator = new BeanMetadataNamesGeneratorImpl();
        beanDomainProcessor = new BeanDomainProcessorImpl(beanMetadataNamesGenerator);
    }

    @Override
    public void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        MetadataGenerationStyle style) {
        DomainClassesCollectorImpl domainClassesCollector = new DomainClassesCollectorImpl(processorContext);

        Set<TypeElement> typeElements = domainClassesCollector.collect(domainConfig,
            roundContext.getRoundEnvironment());
        BeanDomain beanDomain = beanDomainAnalyser.analyse(typeElements);
        TypeSetDeclaration typeSetDeclaration = beanDomainProcessor.processDomain(beanDomain);
        for (TypeDeclaration typeDeclaration : typeSetDeclaration.getTypes()) {
            typeGenerator.generateType(typeDeclaration);
        }
    }
}
