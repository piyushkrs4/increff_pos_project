package com.increff.pos.util;

import java.lang.reflect.Field;

public class NormalizerUtil {
    public static <T> void normalize(T form) throws IllegalAccessException {
        Class<?> clazz = form.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String value = (String) field.get(form);
                if (value != null) {
                    field.set(form, value.trim().toLowerCase());
                }
            }
        }
    }
}