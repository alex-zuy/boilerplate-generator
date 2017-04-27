package com.github.alex.zuy.boilerplate.api.annotations;

public @interface DomainConfiguration {

    Includes includes();

    Excludes excludes() default @Excludes;

    @interface Includes {

        String[] basePackages() default {};

        String[] typeAnnotations() default {};

        String[] packageInfoAnnotations() default {};
    }

    @interface Excludes {

        String[] typeAnnotations() default {};

        String[] patterns() default {};
    }
}
