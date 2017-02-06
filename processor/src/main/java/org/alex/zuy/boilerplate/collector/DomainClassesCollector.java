package org.alex.zuy.boilerplate.collector;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.config.DomainConfig;

public interface DomainClassesCollector {

    Set<TypeElement> collect(DomainConfig domainConfig, RoundEnvironment environment);

}
