package com.github.alex.zuy.boilerplate.stringtemplate;

import java.util.Map;

public interface StringTemplateRenderer {

    String render(String template, Map<String, ?> data);
}
