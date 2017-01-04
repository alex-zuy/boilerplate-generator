package org.alex.zuy.boilerplate.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.annotation.processing.Processor;

public class SingleProcessingRoundAnnotationProcessorWrapper {

    private SingleProcessingRoundAnnotationProcessorWrapper() { }

    public static Processor newInstance(Processor processor) {
        return (Processor) Proxy.newProxyInstance(processor.getClass().getClassLoader(), new Class[]{Processor.class},
            new SingleProcessingRoundInvocationHandler(processor));
    }

    private static final class SingleProcessingRoundInvocationHandler implements InvocationHandler {

        private static final String METHOD_NAME_PROCESS = "process";

        private boolean wasProcessMethodCalled;

        private Processor targetProcessor;

        public SingleProcessingRoundInvocationHandler(Processor targetProcessor) {
            this.targetProcessor = targetProcessor;
        }

        @Override
        public Object invoke(Object proxyInstance, Method method, Object[] args) throws Throwable {
            if (method.getName().equals(METHOD_NAME_PROCESS)) {
                if (!wasProcessMethodCalled) {
                    wasProcessMethodCalled = true;
                    return method.invoke(targetProcessor, args);
                } else {
                    return true;
                }
            } else {
                return method.invoke(targetProcessor, args);
            }
        }
    }
}
