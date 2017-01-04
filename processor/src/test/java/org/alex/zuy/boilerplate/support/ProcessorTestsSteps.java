package org.alex.zuy.boilerplate.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.alex.zuy.boilerplate.utils.IOUtils;

public class ProcessorTestsSteps {

    public static final String DEFAULT_SOURCE_FILES_PACKAGE_NAME = "com.example";

    private TestBuildSetupBuilder builder;

    public ProcessorTestsSteps(TestBuildSetupBuilder builder) {
        this.builder = builder;
    }

    public ProcessorTestsSteps addTestSpecificSourceFile(Class testClass, String packageName, String fileName)
        throws IOException {

        String fileResourcePath = String.format("%s/%s.java", testClass.getSimpleName(), fileName);
        String filePath = String.format("%s.%s.java", packageName, fileName);
        try (InputStream inputStream = testClass.getResourceAsStream(fileResourcePath);
             Reader reader = new InputStreamReader(inputStream)) {
            builder.addSourceFile(filePath, IOUtils.readToString(reader));
        }
        return this;
    }

    public ProcessorTestsSteps addTestSpecificSourceFile(Class testClass, String fileName) throws IOException {
        return addTestSpecificSourceFile(testClass, DEFAULT_SOURCE_FILES_PACKAGE_NAME, fileName);
    }
}
