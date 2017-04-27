package com.github.alex.zuy.boilerplate.domain;

import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.utils.ObjectsUtil;

public class BeanProperty {

    private String name;

    private Type<?> type;

    private AccessModifier accessModifier;

    public BeanProperty(String name, Type<?> type, AccessModifier accessModifier) {
        this.name = name;
        this.type = type;
        this.accessModifier = accessModifier;
    }

    public String getName() {
        return name;
    }

    public Type<?> getType() {
        return type;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    @Override
    public boolean equals(Object other) {
        return ObjectsUtil.equals(this, other, (lhs, rhs) -> lhs.name.equals(rhs.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s(name=%s, type=%s)", getClass().getSimpleName(), name, type);
    }

    public enum AccessModifier {
        DEFAULT,
        PRIVATE,
        PROTECTED,
        PUBLIC
    }
}
