package com.github.alex.zuy.boilerplate.api.annotations;

public @interface GenerationStyle {

    String propertiesClassName() default "${beanClassName}_p";

    String relationshipsClassName() default "${beanClassName}_r";

    String relationshipsClassTerminalMethodName() default "${beanPropertyName}Property";

    StringConstantStyle stringConstantStyle() default StringConstantStyle.UPPERCASE;

    enum StringConstantStyle {
        CAMELCASE, UPPERCASE;
    }
}
