package org.alex.zuy.boilerplate.sourcemodel;

import java.util.List;

import org.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface MethodDeclaration extends AbstractDeclaration {

    String getName();

    Type<?> getReturnType();

    List<MethodParameterDeclaration> getParameters();

    interface MethodParameterDeclaration {

        String getName();

        Type<?> getType();
    }
}
