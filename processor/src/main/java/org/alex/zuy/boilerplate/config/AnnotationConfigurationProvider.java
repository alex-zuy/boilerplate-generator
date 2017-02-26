package org.alex.zuy.boilerplate.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;

import org.alex.zuy.boilerplate.UnexpectedEnumValueException;
import org.alex.zuy.boilerplate.api.annotations.BeanMetadataConfiguration;
import org.alex.zuy.boilerplate.api.annotations.DomainConfiguration;
import org.alex.zuy.boilerplate.api.annotations.GenerationStyle;
import org.alex.zuy.boilerplate.api.annotations.GenerationStyle.StringConstantStyle;
import org.alex.zuy.boilerplate.api.annotations.SupportClassesConfiguration;
import org.alex.zuy.boilerplate.config.validation.ConfigValidator;
import org.alex.zuy.boilerplate.config.validation.DomainConfigValidator;
import org.alex.zuy.boilerplate.config.validation.GenerationStyleValidator;
import org.alex.zuy.boilerplate.config.validation.SupportClassesConfigValidator;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.alex.zuy.boilerplate.utils.CollectionUtils;

//CSOFF: ClassFanOutComplexity
public class AnnotationConfigurationProvider implements ConfigurationProvider {

    private static final Class<BeanMetadataConfiguration> CONFIGURATION_ANNOTATION = BeanMetadataConfiguration.class;

    private DomainConfig domainConfig;

    private SupportClassesConfig supportClassesConfig;

    private MetadataGenerationStyle generationStyle;

    private final ConfigValidator<DomainConfig> domainConfigValidator = new DomainConfigValidator();

    private final ConfigValidator<SupportClassesConfig> supportClassesConfigValidator =
        new SupportClassesConfigValidator();

    private final ConfigValidator<MetadataGenerationStyle> generationStyleConfigValidator =
        new GenerationStyleValidator();

    public AnnotationConfigurationProvider(RoundContext roundContext) {
        loadConfiguration(roundContext);
    }

    @Override
    public DomainConfig getDomainConfig() {
        return domainConfig;
    }

    @Override
    public MetadataGenerationStyle getMetadataGenerationStyle() {
        return generationStyle;
    }

    @Override
    public SupportClassesConfig getSupportClassesConfig() {
        return supportClassesConfig;
    }

    private void loadConfiguration(RoundContext roundContext) {
        Set<? extends Element> annotatedElements = roundContext.getRoundEnvironment()
            .getElementsAnnotatedWith(CONFIGURATION_ANNOTATION);
        if (annotatedElements.isEmpty()) {
            throw new ConfigException(
                String.format("No elements annotated with '%s' present.", CONFIGURATION_ANNOTATION.getName()));
        }
        else if (annotatedElements.size() > 1) {
            throw new ConfigException(String.format("Exactly one element must be annotated with '%s'.",
                CONFIGURATION_ANNOTATION.getName()));
        }
        else {
            Element annotatedElement = CollectionUtils.getFirst(annotatedElements);
            BeanMetadataConfiguration annotationValue = annotatedElement.getAnnotation(CONFIGURATION_ANNOTATION);
            processConfigurationAnnotation(annotationValue);
        }
    }

    private void processConfigurationAnnotation(BeanMetadataConfiguration configuration) {
        generationStyle = processGenerationStyleConfig(configuration.generationStyle());
        supportClassesConfig = processSupportClassesConfig(configuration.supportClassesConfiguration());
        domainConfig = processDomainConfig(configuration.domainConfiguration());
    }

    private MetadataGenerationStyle processGenerationStyleConfig(GenerationStyle styleConfig) {
        ImmutableMetadataGenerationStyle style = ImmutableMetadataGenerationStyle.builder()
            .propertyClassNameTemplate(styleConfig.propertiesClassName())
            .relationshipsClassNameTemplate(styleConfig.relationshipsClassName())
            .relationshipsClassTerminalMethodNameTemplate(styleConfig.relationshipsClassTerminalMethodName())
            .stringConstantStyle(toStringConstantStyle(styleConfig.stringConstantStyle()))
            .build();
        generationStyleConfigValidator.validate(style);
        return style;
    }

    private SupportClassesConfig processSupportClassesConfig(SupportClassesConfiguration configuration) {
        ImmutableSupportClassesConfig config = ImmutableSupportClassesConfig.builder()
            .basePackage(configuration.basePackage())
            .build();
        supportClassesConfigValidator.validate(config);
        return config;
    }

    private DomainConfig processDomainConfig(DomainConfiguration domainConfiguration) {
        DomainConfig.IncludesConfig includesConfig = processIncludesConfig(domainConfiguration.includes());
        DomainConfig.ExcludesConfig excludesConfig = processExcludesConfig(domainConfiguration.excludes());
        ImmutableDomainConfig config = ImmutableDomainConfig.builder()
            .includes(includesConfig)
            .excludes(excludesConfig)
            .generationStyle(generationStyle)
            .build();
        domainConfigValidator.validate(config);
        return config;
    }

    private static DomainConfig.IncludesConfig processIncludesConfig(DomainConfiguration.Includes includes) {
        return ImmutableIncludesConfig.builder()
            .addBasePackages(includes.basePackages())
            .addTypeAnnotations(includes.typeAnnotations())
            .addPackageInfoAnnotations(includes.packageInfoAnnotations())
            .build();
    }

    private static DomainConfig.ExcludesConfig processExcludesConfig(DomainConfiguration.Excludes excludes) {
        return ImmutableExcludesConfig.builder()
            .addTypeAnnotations(excludes.typeAnnotations())
            .addAllPatterns(compilePatterns(excludes.patterns()))
            .build();
    }

    private static Collection<Pattern> compilePatterns(String... patterns) {
        try {
            return Arrays.stream(patterns).map(Pattern::compile).collect(Collectors.toList());
        }
        catch (PatternSyntaxException e) {
            throw new ConfigException(String.format("Invalid pattern provided: \"%s\"", e.getPattern()), e);
        }
    }

    private static MetadataGenerationStyle.StringConstantStyle toStringConstantStyle(StringConstantStyle style) {
        switch (style) {
            case UPPERCASE:
                return MetadataGenerationStyle.StringConstantStyle.UPPERCASE;
            case CAMELCASE:
                return MetadataGenerationStyle.StringConstantStyle.CAMELCASE;
            default:
                throw new UnexpectedEnumValueException(style);
        }
    }
}
//CSON: ClassFanOutComplexity
