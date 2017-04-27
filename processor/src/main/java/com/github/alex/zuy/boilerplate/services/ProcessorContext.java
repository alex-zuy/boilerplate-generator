
package com.github.alex.zuy.boilerplate.services;

import java.util.Locale;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.immutables.value.Value;

@Value.Immutable
public interface ProcessorContext {

    Elements getElementUtils();

    Types getTypeUtils();

    Filer getFiler();

    Locale getLocale();

    Messager getMessager();
}
