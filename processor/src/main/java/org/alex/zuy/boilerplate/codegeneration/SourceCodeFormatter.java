package org.alex.zuy.boilerplate.codegeneration;

public interface SourceCodeFormatter {

    String formatSource(String source);

    class SourceCodeFormattingException extends RuntimeException {

        public SourceCodeFormattingException(Throwable throwable) {
            super(throwable);
        }
    }
}
