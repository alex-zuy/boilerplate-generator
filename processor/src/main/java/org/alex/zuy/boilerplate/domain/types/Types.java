package org.alex.zuy.boilerplate.domain.types;

import java.util.List;

public final class Types {

    private static final VoidType VOID_TYPE = new VoidTypeImpl();

    private Types() { }

    public static VoidType makeVoidType() {
        return VOID_TYPE;
    }

    public static ExactType makeExactType(String name) {
        return new ExactTypeImpl(name);
    }

    public static ArrayType makeArrayType(Type<?> elementType) {
        return new ArrayTypeImpl(elementType);
    }

    public static TypeInstance makeTypeInstance(String name, List<Type<?>> parameters) {
        return new TypeInstanceImpl(name, parameters);
    }

    public static TypeParameter makeTypeParameter(String name) {
        return new TypeParameterImpl(name);
    }

}
