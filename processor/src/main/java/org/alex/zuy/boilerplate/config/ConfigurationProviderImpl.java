package org.alex.zuy.boilerplate.config;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.alex.zuy.boilerplate.config.MetadataGenerationStyle.StringConstantStyle;
import org.alex.zuy.boilerplate.config.generated.Configuration;
import org.alex.zuy.boilerplate.config.generated.Domain;
import org.alex.zuy.boilerplate.config.generated.GenerationStyle;
import org.alex.zuy.boilerplate.config.generated.Includes;
import org.alex.zuy.boilerplate.config.generated.StringConstantNameStyle;

public class ConfigurationProviderImpl implements ConfigurationProvider {

    private interface Defaults {

        interface Style {

            String PROPERTY_CLASS_NAME = "${beanClassName}Properties";

            String RELATIONSHIP_CLASS_NAME = "${beanClassName}Relationships";

            MetadataGenerationStyle.StringConstantStyle STRING_CONSTANT_STYLE = StringConstantStyle.UPPERCASE;

            String RELATIONSHIP_CLASS_TERMINAL_METHOD_NAME = "${beanPropertyName}Property";
        }
    }

    private Configuration configuration;

    public ConfigurationProviderImpl(URL configurationUrl) {
        ConfigLoader configLoader = new ConfigLoader();
        configuration = configLoader.loadConfig(configurationUrl);
    }

    @Override
    @SuppressWarnings("PMD.NPathComplexity")
    public DomainConfig getDomainConfig() {
        Domain domain = configuration.getBeanProcessing().getDomain();

        Includes includes = domain.getIncludes();
        DomainConfig.IncludesConfig includesConfig = ImmutableIncludesConfig.builder()
            .addAllBasePackages(
                includes.getBasePackages() != null
                    ? includes.getBasePackages().getPackages()
                    : Collections.emptyList())
            .addAllTypeAnnotations(
                includes.getTypeAnnotations() != null
                    ? includes.getTypeAnnotations().getAnnotations()
                    : Collections.emptyList())
            .addAllPackageInfoAnnotations(
                includes.getPackageInfoAnnotations() != null
                    ? includes.getPackageInfoAnnotations().getAnnotations()
                    : Collections.emptyList())
            .build();

        DomainConfig.ExcludesConfig excludesConfig = Optional.ofNullable(domain.getExcludes())
            .map(excludes -> {
                return ImmutableExcludesConfig.builder()
                    .addAllTypeAnnotations(
                        excludes.getTypeAnnotations() != null
                            ? excludes.getTypeAnnotations().getAnnotations()
                            : Collections.emptyList())
                    .addAllPatterns(
                        excludes.getPatterns() != null
                            ? compilePatterns(excludes.getPatterns().getPatterns())
                            : Collections.emptyList())
                    .build();
            })
            .orElseGet(() -> ImmutableExcludesConfig.builder().build());

        return ImmutableDomainConfig.builder()
            .includes(includesConfig)
            .excludes(excludesConfig)
            .generationStyle(getMetadataGenerationStyle())
            .build();
    }

    @Override
    public MetadataGenerationStyle getMetadataGenerationStyle() {
        return Optional.ofNullable(configuration.getBeanProcessing().getGenerationStyle())
            .map(GenerationStyle::getMetadataClasses)
            .map(metadataClasses -> {
                return ImmutableMetadataGenerationStyle.builder()
                    .stringConstantStyle(
                        Optional.ofNullable(metadataClasses.getStringConstantNameStyle())
                            .map(ConfigurationProviderImpl::toStingConstantStyle)
                            .orElse(Defaults.Style.STRING_CONSTANT_STYLE))
                    .propertyClassNameTemplate(metadataClasses.getPropertyClassName() != null
                        ? metadataClasses.getPropertyClassName()
                        : Defaults.Style.PROPERTY_CLASS_NAME)
                    .relationshipsClassNameTemplate(metadataClasses.getRelationshipClassName() != null
                        ? metadataClasses.getRelationshipClassName()
                        : Defaults.Style.RELATIONSHIP_CLASS_NAME)
                    .relationshipsClassTerminalMethodNameTemplate(
                        metadataClasses.getRelationshipsClassTerminalMethodNameTemplate() != null
                            ? metadataClasses.getRelationshipsClassTerminalMethodNameTemplate()
                            : Defaults.Style.RELATIONSHIP_CLASS_TERMINAL_METHOD_NAME)
                    .build();
            })
            .orElseGet(() -> {
                return ImmutableMetadataGenerationStyle.builder()
                    .stringConstantStyle(Defaults.Style.STRING_CONSTANT_STYLE)
                    .propertyClassNameTemplate(Defaults.Style.PROPERTY_CLASS_NAME)
                    .relationshipsClassNameTemplate(Defaults.Style.RELATIONSHIP_CLASS_NAME)
                    .relationshipsClassTerminalMethodNameTemplate(
                        Defaults.Style.RELATIONSHIP_CLASS_TERMINAL_METHOD_NAME)
                    .build();
            });
    }

    @Override
    public SupportClassesConfig getSupportClassesConfig() {
        return ImmutableSupportClassesConfig.builder()
            .basePackage(configuration.getBeanProcessing().getSupportClasses().getBasePackage())
            .build();
    }

    private static StringConstantStyle toStingConstantStyle(StringConstantNameStyle nameStyle) {
        switch (nameStyle) {
            case UPPERCASE:
                return StringConstantStyle.UPPERCASE;
            case CAMELCASE:
                return StringConstantStyle.CAMELCASE;
            default:
                throw new IllegalArgumentException(
                    String.format("Unexpected value '%s' for enum '%s'", nameStyle.toString(),
                        StringConstantStyle.class));
        }
    }

    private static List<Pattern> compilePatterns(Collection<String> patterns) {
        return patterns.stream().map(Pattern::compile).collect(Collectors.toList());
    }
}
