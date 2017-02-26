@BeanMetadataConfiguration(
    supportClassesConfiguration = @SupportClassesConfiguration(basePackage = "org.alex.zuy.boilerplate.test.generated.support"),
    domainConfiguration = @DomainConfiguration(
        includes = @DomainConfiguration.Includes(
            typeAnnotations = {"org.alex.zuy.boilerplate.IncludeMarker"})),
    generationStyle = @GenerationStyle(
        propertiesClassName = "${beanClassName}Properties",
        relationshipsClassName = "${beanClassName}Relationships",
        stringConstantStyle = GenerationStyle.StringConstantStyle.UPPERCASE,
        relationshipsClassTerminalMethodName = "${beanPropertyName}Property"
    ))

package org.alex.zuy.boilerplate;

import org.alex.zuy.boilerplate.api.annotations.BeanMetadataConfiguration;
import org.alex.zuy.boilerplate.api.annotations.DomainConfiguration;
import org.alex.zuy.boilerplate.api.annotations.GenerationStyle;
import org.alex.zuy.boilerplate.api.annotations.SupportClassesConfiguration;
