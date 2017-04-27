package com.github.alex.zuy.boilerplate.sourcemodel;

import java.util.Map;

public interface TemplateDescription {

    String getTemplateName();

    Map<String, Object> getData();
}
