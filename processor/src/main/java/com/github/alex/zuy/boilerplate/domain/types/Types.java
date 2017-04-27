package com.github.alex.zuy.boilerplate.domain.types;

import java.util.List;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;

public final class Types {

    private static final VoidType VOID_TYPE = new VoidTypeImpl();

    private Types() { }

    public static VoidType makeVoidType() {
        return VOID_TYPE;
    }

    public static ExactType makeExactType(QualifiedName name) {
        return new ExactTypeImpl(name);
    }

    public static ArrayType makeArrayType(Type<?> elementType) {
        return new ArrayTypeImpl(elementType);
    }

    public static TypeInstance makeTypeInstance(QualifiedName name, List<Type<?>> parameters) {
        return new TypeInstanceImpl(name, parameters);
    }

    public static TypeParameter makeTypeParameter(QualifiedName name) {
        return new TypeParameterImpl(name);
    }

}
