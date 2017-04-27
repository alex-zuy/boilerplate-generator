package com.github.alex.zuy.boilerplate.boilerplate;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.domain.types.Types;

public class JdkTypes {

    private static final String PACKAGE_JAVA_LANG = "java.lang";

    public static final Type<?> PRIMITIVE_BOOLEAN = Types.makeExactType(new QualifiedName("boolean"));

    public static final Type<?> PRIMITIVE_INT = Types.makeExactType(new QualifiedName("int"));

    public static final Type<?> JAVA_LANG_STRING = Types.makeExactType(new QualifiedName("String", PACKAGE_JAVA_LANG));

    public static final Type<?> JAVA_LANG_BOOLEAN = Types.makeExactType(
        new QualifiedName("Boolean", PACKAGE_JAVA_LANG));

    public static final Type<?> JAVA_LANG_INTEGER = Types.makeExactType(
        new QualifiedName("Integer", PACKAGE_JAVA_LANG));

    public static final Type<?> JAVA_LANG_OBJECT = Types.makeExactType(new QualifiedName("Object", PACKAGE_JAVA_LANG));
}
