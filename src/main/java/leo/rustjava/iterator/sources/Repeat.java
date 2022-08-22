package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.EndlessIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

public class Repeat<T> implements Iterator<T>, DoubleEndedIterator<T>, EndlessIterator<T>, FusedIterator<T> {
    T item;

    public Repeat(T item) {
        this.item = item;
    }

    @Override
    public Option<T> next() {
        return Option.Some(item);
    }

    @Override
    public Option<T> nextBack() {
        return Option.Some(item);
    }

    @Override
    public String toString() {
        return "Repeat { " + item + " }";
    }
}
