package org.alex.zuy.boilerplate.codegeneration;

import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.immutables.value.Value;

public interface TypeGenerator {

    void generateType(TypeDeclaration typeDeclaration);

    void generateType(TypeImplementation typeImplementation);

    @Value.Immutable
    interface TypeImplementation {

        String getSourceCode();

        String getPackageName();

        String getSimpleName();
    }

    final class TypeGenerationException extends RuntimeException {

        public TypeGenerationException(Throwable throwable) {
            super(throwable);
        }
    }
}
