package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import static leo.rustjava.Option.None;

public class Empty<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    public Empty() {
    }

    @Override
    public Option<T> next() {
        return None();
    }

    @Override
    public Option<T> nextBack() {
        return None();
    }

    @Override
    public int len() {
        return 0;
    }

    @Override
    public String toString() {
        return "Empty";
    }
}
