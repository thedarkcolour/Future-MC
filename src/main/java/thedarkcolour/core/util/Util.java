package thedarkcolour.core.util;

import java.util.function.Consumer;

public final class Util {
    /**
     * Really useful function.
     */
    public static <T> T make(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }
}