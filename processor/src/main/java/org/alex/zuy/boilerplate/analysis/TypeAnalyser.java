package org.alex.zuy.boilerplate.analysis;

import javax.lang.model.type.TypeMirror;

import org.alex.zuy.boilerplate.domain.Types;

public interface TypeAnalyser {

    Types.Type<?> analyse(TypeMirror typeMirror);

    final class UnsupportedTypeException extends RuntimeException {

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
}
