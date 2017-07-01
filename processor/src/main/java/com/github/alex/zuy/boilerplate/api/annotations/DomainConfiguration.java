package com.github.alex.zuy.boilerplate.api.annotations;

/**
 * Domain configuration.
 */
public @interface DomainConfiguration {

    /**
     * Includes configuration.
     * <p>
     * Use this annotation to specify which classes should be included in domain.
     *
     * @return includes configuration
     */
    Includes includes();

    /**
     * Excludes configuration.
     * <p>
     * Use this annotation to specify which classes among included classes
     * (included by means of {@link #includes()}) should be excluded from domain
     * (takes precedence over includes).
     *
     * @return excludes configuration
     */
    Excludes excludes() default @Excludes;

    /**
     * Includes configuration.
     * <p>
     * Any class that matches at least one constrain is included in domain.
     */
    @interface Includes {

        /**
         * Includes classes by package.
         * <p>
         * Includes classes declared in either of listed packages.
         * <b>Does NOT includes classes declared in "subpackages"!</b>
         *
         * @return list of packages to include classes in.
         */
        String[] basePackages() default {};

        /**
         * Includes classes by annotation.
         * <p>
         * Includes classes which are annotated with either of listed annotations.
         * Class annotation may appear directly or be inherited ({@link java.lang.annotation.Inherited}).
         *
         * @return list of annotations fully qualified names.
         */
        String[] typeAnnotations() default {};

        /**
         * Includes classes by annotated packages.
         * <p>
         * Includes classes declared in packages which are annotated with
         * either of listed annotations.
         * <b>Does NOT includes classes declared in "subpackages"!</b>
         *
         * @return list of annotations fully qualified names.
         */
        String[] packageInfoAnnotations() default {};
    }

    @interface Excludes {

        /**
         * Excludes classes by annotation.
         * <p>
         * Excludes classes which are annotated with either of listed annotations.
         * Only directly present annotations are taken into account.
         *
         * @return list of annotations fully qualified names.
         */
        String[] typeAnnotations() default {};

        /**
         * Excludes classes by name pattern.
         * <p>
         * Excludes classes with fully qualified names matching either pattern.
         *
         * @return list of patterns
         */
        String[] patterns() default {};
    }
}
