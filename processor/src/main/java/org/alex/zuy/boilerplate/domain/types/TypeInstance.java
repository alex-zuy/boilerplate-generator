package org.alex.zuy.boilerplate.domain.types;

import java.util.List;

public interface TypeInstance extends Type<TypeInstance> {

    List<Type<?>> getParameters();
}
