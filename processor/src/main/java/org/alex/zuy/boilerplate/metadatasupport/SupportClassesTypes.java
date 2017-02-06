package org.alex.zuy.boilerplate.metadatasupport;

import org.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface SupportClassesTypes {

    Type<?> getPropertyChainNodeType();
}
