package org.alex.zuy.boilerplate.domain.types;

import java.util.Objects;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

final class ArrayTypeImpl extends AbstractType<Types.ArrayType> implements Types.ArrayType {

    private static final String ARRAY_TYPE_NAME = "array";

    private Types.Type<?> elementType;

    ArrayTypeImpl(Types.Type<?> elementType) {
        super(ARRAY_TYPE_NAME, Types.TypeKinds.ARRAY_TYPE);
        this.elementType = elementType;
    }

    @Override
    public Types.Type<?> getElementType() {
        return elementType;
    }

    @Override
    public boolean equals(Object other) {
        return ObjectsUtil.equals(this, other, (lhs, rhs) ->
            Objects.equals(lhs.getName(), rhs.getName())
                && Objects.equals(lhs.getKind(), rhs.getKind())
                && Objects.equals(lhs.elementType, rhs.elementType));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getKind(), elementType);
    }

    @Override
    public String toString() {
        return String.format("%s(name=%s, kind=%s, elementType=%s)", getClass().getSimpleName(), getName(),
            getKind(), elementType);
    }
}
