package com.github.alex.zuy.boilerplate.metadatasupport;

import com.github.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface SupportClassesTypes {

    Type<?> getPropertyChainNodeType();
}
