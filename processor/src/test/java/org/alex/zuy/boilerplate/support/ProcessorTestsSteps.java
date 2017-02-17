package org.alex.zuy.boilerplate.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.alex.zuy.boilerplate.utils.IoUtils;

public class ProcessorTestsSteps {

    private TestBuildSetupBuilder builder;

    private Class<?> testClass;

    public ProcessorTestsSteps(TestBuildSetupBuilder builder, Class<?> testClass) {
        this.builder = builder;
        this.testClass = testClass;
    }

    public ProcessorTestsSteps addTestSpecificSourceFilesInDirectory(String directory) throws IOException {
        builder.addTestSpecificSources(testClass, directory);
        return this;
    }

    public ProcessorTestsSteps addTestSpecificSourceFile(String packageName, String fileName)
        throws IOException {
        String filePath = String.format("%s.%s.java", packageName, fileName);
        return addTestSpecificSourceFileImpl(filePath, getFileResourcePath(fileName));
    }

    public ProcessorTestsSteps addTestSpecificSourceFile(String fileName) throws IOException {
        String filePath = fileName + ".java";
        return addTestSpecificSourceFileImpl(filePath, getFileResourcePath(fileName));
    }

    private ProcessorTestsSteps addTestSpecificSourceFileImpl(String filePath, String fileResourcePath)
        throws IOException {
        builder.addSourceFile(filePath, getFileContents(testClass.getResource(fileResourcePath)));
        return this;
    }

    private String getFileResourcePath(String fileName) {
        return String.format("%s/%s.java", testClass.getSimpleName(), fileName);
    }

    private static String getFileContents(URL resourceUrl) throws IOException {
        try (InputStream inputStream = resourceUrl.openStream();
             Reader reader = new InputStreamReader(inputStream)) {
            return IoUtils.readToString(reader);
        }
    }
}
