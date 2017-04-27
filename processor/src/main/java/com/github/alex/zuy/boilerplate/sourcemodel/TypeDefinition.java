package com.github.alex.zuy.boilerplate.sourcemodel;

import org.immutables.value.Value;

@Value.Immutable
public interface TypeDefinition {

    String getSourceCode();

    String getPackageName();

    String getSimpleName();
}
