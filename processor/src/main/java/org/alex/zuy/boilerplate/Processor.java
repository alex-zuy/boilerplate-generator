
package org.alex.zuy.boilerplate;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

public class Processor extends AbstractProcessor {

    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set,
            final RoundEnvironment roundEnvironment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {

        throw new UnsupportedOperationException();
    }
}
