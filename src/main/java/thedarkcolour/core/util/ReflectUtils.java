package thedarkcolour.core.util;

/**
 * Has a bunch of {@link java.lang.reflect} related methods that save me from eyesore try-catches.
 * I guess re-usability too.
 * Currently used in {@link thedarkcolour.futuremc.entity.bee.EntityBee}
 */
public final class ReflectUtils {
    /*
     * Returns an accessible field with the given name from the given class.
     *
     * Does not guarantee a modifiable field.
     * Use getNonFinalAccessibleField to get an always-modifiable field.
     *//*
    public static Field getAccessibleField(Class clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*
     * Returns a modifiable, accessible field with the given name from the given class.
     * When possible, do not use this.
     *//*
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

    /**
     * Sets an object's field to the given argument.
     * Assumes that you know what you're doing.
     *//*
    public static <V> void setFieldValue(Class clazz, String name, Object obj, V value) {
        try {
            getNonFinalAccessibleField(clazz, name).set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes a hidden non-primitive method.
     * If invoking a static method, pass null as invokeOn.
     * Should probably make another method that has explicit argument type array in case of performance issues.
     *//*
    public static <T> T invokeMethod(Class<?> clazz, String methodName, Object invokeOn, Object... args) {
        try {
            Class<?>[] classes = new Class<?>[args.length];
            for (int i = 0; i < args.length; ++i) {
                classes[i] = args[i].getClass();
            }
            //noinspection unchecked
            return (T) clazz.getDeclaredMethod(methodName, classes).invoke(invokeOn, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}