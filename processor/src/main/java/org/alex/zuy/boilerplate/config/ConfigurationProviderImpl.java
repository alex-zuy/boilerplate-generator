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
import org.alex.zuy.boilerplate.config.generated.Includes;
import org.alex.zuy.boilerplate.config.generated.MetadataClasses;
import org.alex.zuy.boilerplate.config.generated.StringConstantNameStyle;

public class ConfigurationProviderImpl implements ConfigurationProvider {

    private Configuration configuration;

    public ConfigurationProviderImpl(URL configurationUrl) {
        ConfigLoader configLoader = new ConfigLoader();
        configuration = configLoader.loadConfig(configurationUrl);
    }

    @Override
    @SuppressWarnings("PMD.NPathComplexity")
    public DomainConfig getDomainConfig() {
        Domain domain = configuration.getBeanProcessing().getDomains().getDomains().get(0);

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
        MetadataClasses metadataClasses = configuration.getBeanProcessing().getGenerationStyles().getStyles()
            .get(0).getMetadataClasses();
        return ImmutableMetadataGenerationStyle.builder()
            .stringConstantStyle(toStingConstantStyle(metadataClasses.getStringConstantNameStyle()))
            .propertyClassNameTemplate(metadataClasses.getPropertyClassName())
            .relationshipsClassNameTemplate(metadataClasses.getRelationshipClassName())
            /* TODO: stub */
            .relationshipsClassTerminalMethodNameTemplate("${beanPropertyName}Property")
            .build();
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
