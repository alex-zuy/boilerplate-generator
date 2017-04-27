package com.github.alex.zuy.boilerplate.support.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import com.github.alex.zuy.boilerplate.utils.IoUtils;

public class CompileOutputClassLoader extends ClassLoader {

    private JavaFileManager fileManager;

    public CompileOutputClassLoader(ClassLoader classLoader, JavaFileManager fileManager) {
        super(classLoader);
        this.fileManager = fileManager;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            return super.findClass(className);
        }
        catch (ClassNotFoundException cnfe) {
            try {
                final byte[] classDefinition = findClassDefinition(className);
                if (classDefinition != null) {
                    return defineClass(className, classDefinition, 0, classDefinition.length);
                }
                else {
                    throw new ClassNotFoundException(className);
                }
            }
            catch (IOException ioe) {
                throw new ClassNotFoundException(className);
            }
        }
    }

    private byte[] findClassDefinition(String className) throws IOException {
        final JavaFileObject classFile = fileManager.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, className,
            Kind.CLASS);
        if (classFile != null) {
            try (InputStream in = classFile.openInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                IoUtils.copy(in, outputStream);
                return outputStream.toByteArray();
            }
        }
        else {
            return null;
        }
    }
}
