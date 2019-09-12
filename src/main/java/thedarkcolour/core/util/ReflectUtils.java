package thedarkcolour.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectUtils {
    /**
     * Returns an accessible field with the given name from the given class.
     *
     * Does not guarantee a modifiable field.
     * Use {@link ReflectUtils#getNonFinalAccessibleField(Class, String)} to get an always-modifiable field.
     */
    public static Field getAccessibleField(Class clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a modifiable, accessible field with the given name from the given class.
     * When possible, do not use this.
     */
    public static Field getNonFinalAccessibleField(Class clazz, String name) {
        Field field = getAccessibleField(clazz, name);
        Field modifiers = getAccessibleField(Field.class, "modifiers");

        try {
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }
}
