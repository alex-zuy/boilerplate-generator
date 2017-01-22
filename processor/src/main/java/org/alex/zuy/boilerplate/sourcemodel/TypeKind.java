package org.alex.zuy.boilerplate.sourcemodel;

import java.util.Arrays;
import java.util.Objects;

public enum TypeKind {

    CLASS("class"),

    INTERFACE("interface"),

    ANNOTATION("@interface"),

    ENUM("enum");

    private String sourceDeclaration;

    TypeKind(String sourceDeclaration) {
        this.sourceDeclaration = sourceDeclaration;
    }

    @Override
    public String toString() {
        return sourceDeclaration;
    }

    public static TypeKind fromString(String string) {
        return Arrays.stream(values())
            .filter(typeKind -> Objects.equals(string, typeKind.sourceDeclaration))
            .findFirst()
            .orElse(null);
    }
}
