package org.alex.zuy.boilerplate.collector;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public interface DomainClassesCollector {

    Set<TypeElement> collect(DomainConfig domainConfig, RoundEnvironment environment);

    final class DomainClassesCollectorException extends RuntimeException {

        public DomainClassesCollectorException(final String str) {
            super(str);
        }

        public DomainClassesCollectorException(final String str, final Throwable throwable) {
            super(str, throwable);
        }

        public DomainClassesCollectorException(final Throwable throwable) {
            super(throwable);
        }
    }
}
