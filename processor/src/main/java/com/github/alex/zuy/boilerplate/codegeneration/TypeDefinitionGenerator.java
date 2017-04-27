package com.github.alex.zuy.boilerplate.codegeneration;

import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDescription;

public interface TypeDefinitionGenerator {

    TypeDefinition generateType(TypeDescription typeDeclaration);

    final class TypeGenerationException extends RuntimeException {

        public TypeGenerationException(Throwable throwable) {
            super(throwable);
        }
    }
}
