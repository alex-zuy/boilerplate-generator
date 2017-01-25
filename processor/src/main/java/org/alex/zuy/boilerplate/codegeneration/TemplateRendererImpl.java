package org.alex.zuy.boilerplate.codegeneration;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateRendererImpl implements TemplateRenderer {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String TEMPLATE_PATH = "org/alex/zuy/boilerplate/templates";

    private final Configuration configuration;

    public TemplateRendererImpl() throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_25);
        configuration.setDirectoryForTemplateLoading(
            new File(getClass().getClassLoader().getResource(TEMPLATE_PATH).getFile()));
        configuration.setDefaultEncoding(DEFAULT_ENCODING);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(true);
    }

    @Override
    public String renderTemplate(TemplateRenderingTask task) throws IOException {
        try (Writer writer = new StringWriter()) {
            Template template = configuration.getTemplate(task.getTemplateName());
            template.process(task.getData(), writer);
            return writer.toString();
        }
        catch (TemplateException e) {
            throw new TemplateRenderingException(e);
        }
    }
}
