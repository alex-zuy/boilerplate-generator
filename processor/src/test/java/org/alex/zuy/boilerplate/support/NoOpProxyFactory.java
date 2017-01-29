package org.alex.zuy.boilerplate.support;

import java.lang.reflect.Proxy;

public class NoOpProxyFactory {

    private NoOpProxyFactory() {

    }

    public static <T> T newInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
            (proxyInstance, method, arguments) -> null);
    }

}
