package org.alex.zuy.boilerplate.codegeneration;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class SourceCodeFormatterImpl implements SourceCodeFormatter {

    private Formatter formatter = new Formatter();

    @Override
    public String formatSource(String source) {
        try {
            return formatter.formatSource(source);
        }
        catch (FormatterException e) {
            throw new SourceCodeFormattingException(e);
        }
    }
}
