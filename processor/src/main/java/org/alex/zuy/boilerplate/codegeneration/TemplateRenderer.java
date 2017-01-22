package org.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.util.Map;

public interface TemplateRenderer {

    String renderTemplate(String templateName, Map<String, ?> data) throws IOException;

    final class TemplateRenderingException extends RuntimeException {

        public TemplateRenderingException(Throwable cause) {
            super(cause);
        }
    }
}
