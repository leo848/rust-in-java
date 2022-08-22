package leo.rustjava.iterator.interfaces;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.adapters.Rev;

public interface DoubleEndedIterator<Item> extends Iterator<Item> {
    default Option<Item> nthBack(int n) {
        return advanceBackBy(n) ? nextBack() : Option.None();
    }

    default boolean advanceBackBy(int n) {
        for (int i = 0; i < n; i++) {
            if (next().isNone()) {
                return false;
            }
        }
        return true;
    }

    Option<Item> nextBack();

    default DoubleEndedIterator<Item> rev() {
        return new Rev<>(this);
    }
}
