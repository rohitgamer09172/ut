/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.reflection;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Generated;

public final class ReflectionUtils {
    public static boolean hasClass(String className) {
        return ReflectionUtils.getClass(className) != null;
    }

    public static boolean hasMethod(@NotNull Class<?> clazz, String methodName, Class<?> ... parameterTypes) {
        return ReflectionUtils.getMethod(clazz, methodName, parameterTypes) != null;
    }

    @Nullable
    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String methodName, Class<?> ... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e) {
            while (clazz != null) {
                try {
                    return clazz.getDeclaredMethod(methodName, parameterTypes);
                }
                catch (NoSuchMethodException ignored) {
                    clazz = clazz.getSuperclass();
                }
            }
            return null;
        }
    }

    @Nullable
    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    @Generated
    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

