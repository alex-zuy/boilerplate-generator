@BeanMetadataConfiguration(
    supportClassesConfiguration = @SupportClassesConfiguration(basePackage = "com.github.alex.zuy.boilerplate.boilerplate.test.generated.support"),
    domainConfiguration = @DomainConfiguration(
        includes = @DomainConfiguration.Includes(
            typeAnnotations = {"com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker"})),
    generationStyle = @GenerationStyle(
        propertiesClassName = "${beanClassName}Properties",
        relationshipsClassName = "${beanClassName}Relationships",
        stringConstantStyle = GenerationStyle.StringConstantStyle.UPPERCASE,
        relationshipsClassTerminalMethodName = "${beanPropertyName}Property"
    ))

package com.github.alex.zuy.boilerplate.boilerplate;

import com.github.alex.zuy.boilerplate.api.annotations.BeanMetadataConfiguration;
import com.github.alex.zuy.boilerplate.api.annotations.DomainConfiguration;
import com.github.alex.zuy.boilerplate.api.annotations.GenerationStyle;
import com.github.alex.zuy.boilerplate.api.annotations.SupportClassesConfiguration;
