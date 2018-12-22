package ru.hitrerros.purse.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


@SuppressWarnings("SameParameterValue")
public  class ReflectionHelper {
    private ReflectionHelper() {
    }

    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object getFieldValue(Object object, String name) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            isAccessible = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {

            try {
                field = object.getClass().getSuperclass().getDeclaredField(name); //getField() for public fields
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException | IllegalAccessException n) {
                e.printStackTrace();
            } finally {
                       }
        }
        return null;
    }

    private static void setFieldValue(Object object, String name, Object value) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            isAccessible = field.canAccess(object);
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException n) {

            try {
                field = object.getClass().getSuperclass().getDeclaredField(name); //getField() for public fields
                field.setAccessible(true);
                field.set(object, value);
            } catch (NoSuchFieldException | IllegalAccessException x) {
                x.printStackTrace();
            }


        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    public static Object callMethod(Object object, String name, Object... args) {
        Method method = null;
        boolean isAccessible = true;
        try {
            method = object.getClass().getDeclaredMethod(name, toClasses(args));
            isAccessible = method.canAccess(object);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (method != null && !isAccessible) {
                method.setAccessible(false);
            }
        }
        return null;
    }

    static private Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }


    public static  <T,R>  T transferObject(R sourceObject,Class<T> clazz) {

        @SuppressWarnings("unchecked")
        T newDoc = ReflectionHelper.instantiate(clazz);

        for (Field field : sourceObject.getClass().getDeclaredFields()) {
            Optional<Object> value = Optional.of(ReflectionHelper.getFieldValue(sourceObject,field.getName()));
            value.ifPresent(v->ReflectionHelper.setFieldValue(Objects.requireNonNull(newDoc),field.getName(),v));
        }

        return newDoc;
    }


}
