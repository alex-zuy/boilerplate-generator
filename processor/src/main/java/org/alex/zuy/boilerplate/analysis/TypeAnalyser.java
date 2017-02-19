package org.alex.zuy.boilerplate.analysis;

import javax.lang.model.type.TypeMirror;

import org.alex.zuy.boilerplate.domain.types.Type;

public interface TypeAnalyser {

    Type<?> analyse(TypeMirror typeMirror) throws UnsupportedTypeException;
}
