package org.alex.zuy.boilerplate.analysis;

public final class BeanClassAnalysisException extends RuntimeException {

    private final String className;

    public BeanClassAnalysisException(String message, Throwable throwable, String className) {
        super(message, throwable);
        this.className = className;
    }

    public BeanClassAnalysisException(String message, String className) {
        super(message);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
