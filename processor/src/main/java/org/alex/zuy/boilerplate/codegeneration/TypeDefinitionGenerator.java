package org.alex.zuy.boilerplate.codegeneration;

import org.alex.zuy.boilerplate.sourcemodel.TypeDescription;
import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public interface TypeDefinitionGenerator {

    TypeDefinition generateType(TypeDescription typeDeclaration);

    final class TypeGenerationException extends RuntimeException {

        public TypeGenerationException(Throwable throwable) {
            super(throwable);
        }
    }
}
