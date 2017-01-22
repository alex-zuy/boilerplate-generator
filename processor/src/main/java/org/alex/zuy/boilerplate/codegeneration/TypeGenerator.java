package org.alex.zuy.boilerplate.codegeneration;

import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;

public interface TypeGenerator {

    void generateType(TypeDeclaration typeDeclaration);

    final class TypeGenerationException extends RuntimeException {

        public TypeGenerationException(Throwable throwable) {
            super(throwable);
        }
    }
}
