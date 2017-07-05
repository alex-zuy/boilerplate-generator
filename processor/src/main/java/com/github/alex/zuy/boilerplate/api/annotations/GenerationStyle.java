package com.github.alex.zuy.boilerplate.api.annotations;

/**
 * Annotation for configuring code generation style(e.g. names).
 */
public @interface GenerationStyle {

    /**
     * Properties class name format.
     * <p>
     * Format must contain placeholder value {@code ${beanClassName}} which will be substituted
     * with simple name of bean class.
     *
     * @return properties class name format
     */
    String propertiesClassName() default "${beanClassName}_p";

    /**
     * Relationships class name format.
     * <p>
     * Format must contain placeholder value {@code ${beanClassName}} which will be substituted
     * with simple name of bean class.
     *
     * @return relationships class name format
     */
    String relationshipsClassName() default "${beanClassName}_r";

    /**
     * Relationships class terminal method name format.
     * <p>
     * Format must contain placeholder value {@code ${beanPropertyName}} which will be substituted
     * with corresponding bean property name.
     *
     * @return relationships class terminal method name format.
     */
    String relationshipsClassTerminalMethodName() default "${beanPropertyName}Property";

    /**
     * String constant fields name style.
     * <p>
     * Defines style of constant fields names in properties classes.
     * Please refer to documentation of {@link StringConstantStyle} for available options.
     *
     * @return string constant fields name style.
     */
    StringConstantStyle stringConstantStyle() default StringConstantStyle.UPPERCASE;

    enum StringConstantStyle {

        /**
         * Constant should be named in camelcase.
         * <p>
         * Example:
         * <code>
         * <pre>
         *    public static final String name = ...;
         *    public static final String homeAddress = ...;
         * </pre>
         * </code>
         */
        CAMELCASE,

        /**
         * Constant should be named in uppercase.
         * <p>
         * Example:
         * <code>
         * <pre>
         *    public static final String NAME = ...;
         *    public static final String HOME_ADDRESS = ...;
         * </pre>
         * </code>
         */
        UPPERCASE;
    }
}
