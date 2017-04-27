package com.github.alex.zuy.boilerplate.sourcemodel;

import java.util.List;

import com.github.alex.zuy.boilerplate.Nullable;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface MethodDescription extends AbstractDescription, TemplateDescription {

    String getName();

    @Nullable
    Type<?> getReturnType();

    List<MethodParameterDeclaration> getParameters();

    @Value.Immutable
    interface MethodParameterDeclaration {

        @Value.Parameter
        String getName();

        @Value.Parameter
        Type<?> getType();
    }
}
