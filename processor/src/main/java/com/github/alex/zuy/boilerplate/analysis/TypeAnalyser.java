package com.github.alex.zuy.boilerplate.analysis;

import javax.lang.model.type.TypeMirror;

import com.github.alex.zuy.boilerplate.domain.types.Type;

public interface TypeAnalyser {

    Type<?> analyse(TypeMirror typeMirror) throws UnsupportedTypeException;
}
