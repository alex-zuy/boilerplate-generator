package org.alex.zuy.boilerplate.codegeneration;

import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public interface SourceFilePublisher {

    void publish(TypeDefinition typeDefinition);

    final class SourceFilePublishingException extends RuntimeException {

        public SourceFilePublishingException(Throwable throwable) {
            super(throwable);
        }
    }
}
