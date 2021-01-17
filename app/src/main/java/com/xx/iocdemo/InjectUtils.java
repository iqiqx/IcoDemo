package com.xx.iocdemo;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {

    private static final String TAG = "InjectUtils";

    public static void injectLayout(Object context) {
        Class<?> clazz = context.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method setContentViewMethod = clazz.getMethod("setContentView", int.class);
                setContentViewMethod.invoke(context, layoutId);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void injectView(Object context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int resId = viewInject.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(context, resId);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(context, view);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectClick(Object context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                BaseEvent baseEvent = annotationClass.getAnnotation(BaseEvent.class);
                if (baseEvent == null) {
                    continue;
                }
                String listenerSetter = baseEvent.listenerSetter();
                Class<?> listenerType = baseEvent.listenerType();
                String callbackMethod = baseEvent.callbackMethod();
                try {
                    Method valueMethod = annotationClass.getDeclaredMethod("value");
                    int[] viewId = (int[]) valueMethod.invoke(annotation);
                    if (viewId != null) {
                        for (int id : viewId) {
                            Method findViewByIdMethod = clazz.getMethod("findViewById", int.class);
                            View view = (View) findViewByIdMethod.invoke(context, id);
                            if (view != null) {
                                ListenerInvocationHandler invocationHandler = new
                                        ListenerInvocationHandler(context, method);
                                Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                        new Class[]{listenerType}, invocationHandler);
                                Method onClickMethod = view.getClass().getMethod(listenerSetter, listenerType);
                                onClickMethod.invoke(view, proxy);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
