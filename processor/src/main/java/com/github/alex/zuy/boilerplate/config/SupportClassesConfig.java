package com.github.alex.zuy.boilerplate.config;

import org.immutables.value.Value;

@Value.Immutable
public interface SupportClassesConfig {

    String getBasePackage();
}
