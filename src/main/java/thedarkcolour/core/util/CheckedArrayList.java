package thedarkcolour.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;

public class CheckedArrayList<E> extends ArrayList<E> {
    private final BiPredicate<E, E> isEquivalent;

    public CheckedArrayList(BiPredicate<E, E> isEquivalent, int initialCapacity) {
        super(initialCapacity);
        this.isEquivalent = isEquivalent;
    }

    public CheckedArrayList(BiPredicate<E, E> isEquivalent, Collection<? extends E> c) {
        super(c);
        this.isEquivalent = isEquivalent;
    }

    public CheckedArrayList(BiPredicate<E, E> isEquivalent) {
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

    public CheckedArrayList<E> bAdd(E o) {
        add(o);
        return this;
    }

    @SafeVarargs
    public final CheckedArrayList<E> addAll(E... obj) {
        for (E o: obj) {
            add(o);
        }
        return this;
    }
}