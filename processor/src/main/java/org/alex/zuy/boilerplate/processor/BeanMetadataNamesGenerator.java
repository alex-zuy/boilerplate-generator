package org.alex.zuy.boilerplate.processor;

public interface BeanMetadataNamesGenerator {

    String makeBeanPropertiesClassName(String simpleBeanClassName);

    String makeBeanRelationshipsClassName(String simpleBeanClassName);

    String makeBeanPropertyConstantName(String propertyName);
}
