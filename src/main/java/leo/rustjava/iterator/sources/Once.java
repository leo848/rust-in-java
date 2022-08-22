package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

public class Once<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    T value;
    boolean exhausted;

    public Once(T value) {
        this.value = value;
        this.exhausted = false;
    }

    @Override
    public int len() {
        return exhausted ? 0 : 1;
    }

    @Override
    public Option<T> nextBack() {
        return next();
    }

    @Override
    public Option<T> next() {
        if (exhausted) return Option.None();
        else {
            exhausted = true;
            return Option.Some(value);
        }
    }

    @Override
    public String toString() {
        return "Once { " + value + " }";
    }
}
