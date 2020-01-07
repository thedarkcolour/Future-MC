package thedarkcolour.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;

/**
 * Extremely sophisticated list.
 */
public final class PredicateArrayList<E> extends ArrayList<E> {
    private final BiPredicate<E, E> isEquivalent;

    public PredicateArrayList(BiPredicate<E, E> isEquivalent) {
        this.isEquivalent = isEquivalent;
    }

    public boolean containsEquivalent(E obj) {
        for (E o: this) {
            if (isEquivalent.test(o, obj)) {
                return true;
            }
        }
        return false;
    }

    public void removeEquivalent(E block) {
        for (E b : this) {
            if (isEquivalent.test(block, b)) {
                remove(b);
            }
        }
    }

    public PredicateArrayList<E> insert(E o) {
        add(o);
        return this;
    }

    @SafeVarargs
    public final PredicateArrayList<E> insertAll(E... obj) {
        addAll(Arrays.asList(obj));
        return this;
    }
}