package org.alex.zuy.boilerplate.domain.types;

import java.util.Arrays;
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

    public interface Type<T extends Type<T>> {

        String getName();

        TypeKinds<T> getKind();

        boolean isOfKind(TypeKinds<?> kind);

        <S extends Type<S>> S getAs(TypeKinds<S> kind);
    }

    public interface VoidType extends Type<VoidType> {

    }

    public interface ExactType extends Type<ExactType> {

    }

    public interface ArrayType extends Type<ArrayType> {

        Type<?> getElementType();
    }

    public interface TypeInstance extends Type<TypeInstance> {

        List<Type<?>> getParameters();
    }

    public interface TypeParameter extends Type<TypeParameter> {

    }

    public static final class TypeKinds<T extends Type<T>> {

        public static final TypeKinds<VoidType> VOID_TYPE = new TypeKinds<>("VOID_TYPE");

        public static final TypeKinds<ExactType> EXACT_TYPE = new TypeKinds<>("EXACT_TYPE");

        public static final TypeKinds<ArrayType> ARRAY_TYPE = new TypeKinds<>("ARRAY_TYPE");

        public static final TypeKinds<TypeInstance> TYPE_INSTANCE = new TypeKinds<>("TYPE_INSTANCE");

        public static final TypeKinds<TypeParameter> TYPE_PARAMETER = new TypeKinds<>("TYPE_PARAMETER");

        private String typeName;

        private TypeKinds(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", getClass().getSimpleName(), typeName);
        }

        public static List<TypeKinds<? extends Type>> kinds() {
            return Arrays.asList(EXACT_TYPE, ARRAY_TYPE, TYPE_INSTANCE, TYPE_PARAMETER);
        }
    }
}
