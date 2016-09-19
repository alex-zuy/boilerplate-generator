
package org.alex.zuy.boilerplate.collector;

public class DomainClassesCollectorException extends RuntimeException {

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
