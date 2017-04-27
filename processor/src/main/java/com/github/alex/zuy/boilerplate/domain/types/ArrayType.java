package com.github.alex.zuy.boilerplate.domain.types;

public interface ArrayType extends Type<ArrayType> {

    Type<?> getElementType();
}
