package leo.rustjava.iterator.interfaces;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.adapters.Rev;

import java.util.function.BiFunction;

public interface DoubleEndedIterator<Item> extends Iterator<Item> {
    Option<Item> nextBack();

    default DoubleEndedIterator<Item> copy() {
        throw new RuntimeException(new CloneNotSupportedException("copy not supported for iterator " + getClass().getName()));
    }

    default Option<Item> nthBack(int n) {
        return advanceBackBy(n) ? nextBack() : Option.None();
    }

    default boolean advanceBackBy(int n) {
        for (int i = 0; i < n; i++) {
            if (nextBack().isNone()) {
                return false;
            }
        }
        return true;
    }

    default <B> B rfold(B seed, BiFunction<? super B, ? super Item, ? extends B> f) {
        B state = seed;
        while (true) {
            Option<Item> item = nextBack();
            if (item.isNone()) return state;
            state = f.apply(state, item.unwrap());
        }
    }

    default DoubleEndedIterator<Item> rev() {
        return new Rev<>(this);
    }
}
