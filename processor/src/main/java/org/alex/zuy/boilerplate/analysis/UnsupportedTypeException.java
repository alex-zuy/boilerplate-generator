package org.alex.zuy.boilerplate.analysis;

import javax.lang.model.type.TypeMirror;

public final class UnsupportedTypeException extends Exception {

    private final TypeMirror type;

    public UnsupportedTypeException(TypeMirror type) {
        this.type = type;
    }

    public UnsupportedTypeException(String message, TypeMirror type) {
        super(message);
        this.type = type;
    }

    public TypeMirror getType() {
        return type;
    }
}
