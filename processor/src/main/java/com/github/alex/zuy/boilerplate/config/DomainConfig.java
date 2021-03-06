
package com.github.alex.zuy.boilerplate.config;

import java.util.List;
import java.util.regex.Pattern;

import org.immutables.value.Value;

@Value.Immutable
public interface DomainConfig {

    IncludesConfig includes();

    ExcludesConfig excludes();

    MetadataGenerationStyle generationStyle();

    @Value.Immutable
    interface IncludesConfig {

        List<String> basePackages();

        List<String> typeAnnotations();

        List<String> packageInfoAnnotations();
    }

    @Value.Immutable
    interface ExcludesConfig {

        List<String> typeAnnotations();

        List<Pattern> patterns();
    }
}
