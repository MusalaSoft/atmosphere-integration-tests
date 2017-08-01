package com.musala.atmosphere.server;

import java.lang.reflect.Field;
import java.util.Stack;

/**
 * An util class with commonly used action that requires a reflection invocation.
 * 
 * @author dimcho.nedev;
 *
 */
public class AtmosphereReflectionUtils {
    /**
     * Gets a field(can be private) value by a parent object and a path to a target field.
     * 
     * @param parantObject
     *        - a object that contains the target field
     * @param path
     *        - an array of field names that represents the path to the target field
     * @return the value of the target field
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public static <K, T> T getFieldValue(K parantObject, String... path)
        throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        T fieldValue = null;
        for (String fieldName : path) {
            fieldValue = getFieldObject(parantObject, fieldName);
            parantObject = (K) fieldValue;
        }

        return fieldValue;
    }

    //@SuppressWarnings("unchecked")
    /*public static <K, T> void setFieldValue(K parantObject, T newValue, String... path)
        throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Stack<Field> fieldsStack = new Stack<>();
        Stack<Object> objectsStack = new Stack<>();
        
        for (String fieldName : path) {
            Field field = getAccessibleField(parantObject, fieldName);
            Object currentObj = field.get(parantObject);
            objectsStack.push(currentObj);
            parantObject = (K) currentObj;
            fieldsStack.push(field);
        }
        Field fieldToChange = fieldsStack.pop();
        objectsStack.pop();
        
        while (!fieldsStack.isEmpty()) {
            //Field parentField = fieldsStack.pop();
            Object parentObject = objectsStack.pop();
            fieldToChange.set(parentObject, newValue);
            newValue = fieldToChange.get(parentObject, );
            //fieldToChange = parentField;
        }
    }*/

    @SuppressWarnings("unused")
    public static <T, K> void setFieldObject(K parantObject, String childFieldName, T newValue)
        throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Field field = getAccessibleField(parantObject, childFieldName);
        field.set(parantObject, newValue);
    }

    @SuppressWarnings({"unchecked"})
    public static <T, K> T getFieldObject(K parantObject, String childFieldName)
        throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Field field = getAccessibleField(parantObject, childFieldName);

        return (T) field.get(parantObject);
    }

    private static <K> Field getAccessibleField(K parantObject, String childFieldName)
        throws NoSuchFieldException,
            SecurityException {
        Class<?> clazz = parantObject.getClass();
        Field field = clazz.getDeclaredField(childFieldName);
        field.setAccessible(true);

        return field;
    }
}
