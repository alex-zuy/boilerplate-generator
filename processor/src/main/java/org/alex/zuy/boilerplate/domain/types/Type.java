package org.alex.zuy.boilerplate.domain.types;

import java.util.Arrays;
import java.util.List;

public interface Type<T extends Type<T>> {

    String getName();

    TypeKinds<T> getKind();

    boolean isOfKind(TypeKinds<?> kind);

    <S extends Type<S>> S getAs(TypeKinds<S> kind);

    final class TypeKinds<T extends Type<T>> {

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
