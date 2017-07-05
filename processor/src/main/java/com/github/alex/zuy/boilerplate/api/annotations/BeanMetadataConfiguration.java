package com.github.alex.zuy.boilerplate.api.annotations;

/**
 * Host annotation for configuring beans metadata generation.
 * <p>
 * Only one application of this annotation should be present in project files.
 * It might be good idea to put this annotation either:
 * <ol>
 * <li>
 * On special configuration class:
 * <code>
 * <pre>
 * {@code @}BeanMetadataConfiguration(...)
 *  public class CodeGenerationConfig {}
 * </pre>
 * </code>
 * </li>
 * <li>
 * On package declaration in package-info.java
 * <code>
 * <pre>
 * {@code @}BeanMetadataConfiguration(...)
 *  package com.example.app;
 * </pre>
 * </code>
 * </li>
 * </ol>
 * <h2>Terminology used in boilerplate generator configuration</h2>
 * <p>
 * Domain - set of all classes that should be processed. If some bean class is included
 * in domain, then generator will generate code to access direct properties of this class
 * as well as it`s relationships. It will also be possible to navigate to properties of
 * this class from other included classes.
 * </p>
 * <p>
 * Terminal property - bean property of type which class isn`t included in domain.
 * </p>
 * See documentation for attribute types for more information.
 */
public @interface BeanMetadataConfiguration {

    /**
     * Support classes configuration.
     * <p>
     * See {@link SupportClassesConfiguration}.
     *
     * @return support classes configuration.
     */
    SupportClassesConfiguration supportClassesConfiguration();

    /**
     * Code generation style configuration.
     * <p>
     * See {@link GenerationStyle}.
     *
     * @return generation style.
     */
    GenerationStyle generationStyle() default @GenerationStyle;

    /**
     * Domain configuration.
     * <p>
     * See {@link DomainConfiguration}.
     *
     * @return domain configuration.
     */
    DomainConfiguration domainConfiguration();
}
