package com.github.alex.zuy.boilerplate.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodInvocationLoggingProxy {

    public static <T> T createProxy(Object target, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
            new LoggingInvocationHandler(target));
    }

    private static class LoggingInvocationHandler implements InvocationHandler {

        private final Object target;

        private LoggingInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            String args = objects == null ? "\t > no args" : Arrays.stream(objects)
                .map(object -> String.format("\t%s", object))
                .collect(Collectors.joining("\n", "[\n", "\n]"));
            System.err.printf("+ %s.%s()\n%s\n", target.getClass().getSimpleName(), method.getName(), args);
            Object result;
            try {
                result = method.invoke(target, objects);
                boolean isVoidMethod = Void.TYPE.equals(method.getReturnType());
                if (!isVoidMethod) {
                    System.err.printf("Returned value: %s\n", result);
                }
            }
            catch (InvocationTargetException ite) {
                throw ite.getTargetException();
            }
            finally {
                System.err.printf("- %s.%s()\n\n", target.getClass().getSimpleName(), method.getName());
            }
            return result;
        }
    }
}
