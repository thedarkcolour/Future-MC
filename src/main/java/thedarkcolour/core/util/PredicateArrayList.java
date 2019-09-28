package thedarkcolour.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * Allows one to compare an outside object to an inside object using a predicate.
 * Helpful with things like ItemStacks.
 */
public final class PredicateArrayList<E> extends ArrayList<E> {
    private final BiPredicate<E, E> isEquivalent;

    public PredicateArrayList(BiPredicate<E, E> isEquivalent, int initialCapacity) {
        super(initialCapacity);
        this.isEquivalent = isEquivalent;
    }

    public PredicateArrayList(BiPredicate<E, E> isEquivalent, Collection<? extends E> c) {
        super(c);
        this.isEquivalent = isEquivalent;
    }

    public PredicateArrayList(BiPredicate<E, E> isEquivalent) {
        this.isEquivalent = isEquivalent;
    }

    public boolean containsEquivalent(E obj) {
        for (E o: this) {
            if(isEquivalent.test(o, obj)) {
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

    public PredicateArrayList<E> bAdd(E o) {
        add(o);
        return this;
    }

    @SafeVarargs
    public final PredicateArrayList<E> addAll(E... obj) {
        for (E o: obj) {
            add(o);
        }
        return this;
    }
}