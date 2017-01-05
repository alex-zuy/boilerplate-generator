package org.alex.zuy.boilerplate.domain.types;

import java.util.Objects;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

abstract class AbstractType<T extends Types.Type<T>> implements Types.Type<T> {

    private final String name;

    private final Types.TypeKinds<T> kind;

    AbstractType(String name, Types.TypeKinds<T> kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Types.TypeKinds<T> getKind() {
        return kind;
    }

    @Override
    public boolean isOfKind(Types.TypeKinds<?> otherKind) {
        return this.kind.equals(otherKind);
    }

    @Override
    public <S extends Types.Type<S>> S getAs(Types.TypeKinds<S> otherKind) {
        return (S) this;
    }

    @Override
    public boolean equals(Object other) {
        return ObjectsUtil.equals(this, other,
            (lhs, rhs) -> Objects.equals(lhs.kind, rhs.kind) && Objects.equals(lhs.name, rhs.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, name);
    }

    @Override
    public String toString() {
        return String.format("%s(name=%s, kind=%s)", getClass().getSimpleName(), name, kind);
    }
}
