package thedarkcolour.core.util;

import java.util.function.Consumer;

public final class Util {
    /**
     * Ported over from 1.13+. I cannot stress how useful this is.
     */
    public static <T> T make(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }
}