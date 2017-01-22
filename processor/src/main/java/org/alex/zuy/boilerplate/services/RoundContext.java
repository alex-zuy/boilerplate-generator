package org.alex.zuy.boilerplate.services;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.immutables.value.Value;

@Value.Immutable
public interface RoundContext {

    RoundEnvironment getRoundEnvironment();

    Set<? extends TypeElement> getAnnotations();
}
