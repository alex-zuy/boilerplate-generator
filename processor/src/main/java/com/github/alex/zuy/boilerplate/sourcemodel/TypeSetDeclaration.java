package com.github.alex.zuy.boilerplate.sourcemodel;

import java.util.Set;

import org.immutables.value.Value;

@Value.Immutable
public interface TypeSetDeclaration {

    Set<TypeDescription> getTypes();
}
