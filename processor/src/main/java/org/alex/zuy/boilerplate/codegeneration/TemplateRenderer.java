package org.alex.zuy.boilerplate.codegeneration;

import java.io.IOException;
import java.util.Map;

import org.immutables.value.Value;

public interface TemplateRenderer {

    String renderTemplate(TemplateRenderingTask task) throws IOException;

    @Value.Immutable
    interface TemplateRenderingTask {

        @Value.Parameter
        String getTemplateName();

        @Value.Parameter
        Map<String, Object> getData();
    }

    final class TemplateRenderingException extends RuntimeException {

        public TemplateRenderingException(Throwable cause) {
            super(cause);
        }
    }
}
