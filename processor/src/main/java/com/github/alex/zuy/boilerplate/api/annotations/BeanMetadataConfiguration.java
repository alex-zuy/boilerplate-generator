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
