package com.github.alex.zuy.boilerplate.api.annotations;

/**
 * Configuration of support classes.
 * <p>
 * Configures generation of support classes for generated code.
 */
public @interface SupportClassesConfiguration {

    /**
     * Base package for support classes.
     * <p>
     * Base package in which support classes should be declared. Only named package may be used.
     * Example: {@code "com.app.generated.support"}
     *
     * @return support classes base package.
     */
    String basePackage();
}
