package com.xx.iocdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerInvocationHandler implements InvocationHandler {

    private Object context;
    private Method activityMethod;

    public ListenerInvocationHandler(Object context, Method method) {
        this.context = context;
        this.activityMethod = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return activityMethod.invoke(context, args);
    }
}
