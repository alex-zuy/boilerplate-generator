package org.alex.zuy.boilerplate.domain.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

final class TypeInstanceImpl extends AbstractType<TypeInstance> implements TypeInstance {

    private List<Type<?>> parameters;

    TypeInstanceImpl(String name, List<Type<?>> parameters) {
        super(name, TypeKinds.TYPE_INSTANCE);
        this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
    }

    @Override
    public List<Type<?>> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object other) {
        return ObjectsUtil.equals(this, other, (lhs, rhs) ->
            Objects.equals(lhs.getName(), rhs.getName())
                && Objects.equals(lhs.getKind(), rhs.getKind())
                && Objects.equals(lhs.parameters, rhs.parameters));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getKind(), parameters);
    }

    @Override
    public String toString() {
        return String.format("%s(name=%s, kind=%s, parameters=%s)", getClass().getSimpleName(), getName(),
            getKind(), parameters);
    }
}
