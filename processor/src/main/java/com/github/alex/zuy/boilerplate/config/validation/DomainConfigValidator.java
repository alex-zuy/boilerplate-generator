package com.github.alex.zuy.boilerplate.config.validation;

import java.util.Collection;
import java.util.stream.Stream;

import com.github.alex.zuy.boilerplate.api.annotations.DomainConfiguration;
import com.github.alex.zuy.boilerplate.config.ConfigException;
import com.github.alex.zuy.boilerplate.config.DomainConfig;

public class DomainConfigValidator implements ConfigValidator<DomainConfig> {

    @Override
    public void validate(DomainConfig config) throws ConfigException {
        validateIncludes(config.includes());
    }

    private void validateIncludes(DomainConfig.IncludesConfig includes) {
        boolean allEmpty = Stream.of(
            includes.basePackages(),
            includes.packageInfoAnnotations(),
            includes.typeAnnotations())
            .allMatch(Collection::isEmpty);
        if (allEmpty) {
            String msg = String.format(
                "Invalid domain configuration! No include rules specified - use '%s' annotation to provide one",
                DomainConfiguration.Includes.class.getName());
            throw new ConfigException(msg);
        }
    }
}
