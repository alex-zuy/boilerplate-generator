package com.github.alex.zuy.boilerplate.boilerplate;


import java.util.Locale;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.github.alex.zuy.boilerplate.services.ProcessorContext;

public class ProcessorContextImpl implements ProcessorContext {

    private Elements elementUtils;

    private Types types;

    private Filer filer;

    private Locale locale;

    private Messager messager;

    public ProcessorContextImpl(Elements elementUtils, Types types, Filer filer, Locale locale,
        Messager messager) {
        this.elementUtils = elementUtils;
        this.types = types;
        this.filer = filer;
        this.locale = locale;
        this.messager = messager;
    }

    @Override
    public Elements getElementUtils() {
        return elementUtils;
    }

    @Override
    public Types getTypeUtils() {
        return types;
    }

    @Override
    public Filer getFiler() {
        return filer;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Messager getMessager() {
        return messager;
    }
}
