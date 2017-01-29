package org.alex.zuy.boilerplate.stringtemplate;

import java.util.Map;
import java.util.regex.Pattern;

public class StringTemplateRendererImpl implements StringTemplateRenderer {

    @Override
    public String render(String template, Map<String, ?> data) {
        String result = template;
        for (Map.Entry<String, ?> entry : data.entrySet()) {
            String placeholderRegex = String.format("\\$\\{%s\\}", Pattern.quote(entry.getKey()));
            result = result.replaceAll(placeholderRegex, String.valueOf(entry.getValue()));
        }
        return result;
    }
}
