package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

public class Pairs<T> implements Iterator<Pair<T, T>> {
    private final Iterator<T> iter;

    public Pairs(Iterator<T> iter) {
        this.iter = iter;
    }

    @Override
    public Option<Pair<T, T>> next() {
        return iter.next().zip(iter.next());
    }

    @Override
    public SizeHint sizeHint() {
        return new SizeHint(
                iter.sizeHint().lower() / 2 + 1,
                iter.sizeHint().upper().map(n -> n / 2 + 1)
        );
    }

    @Override
    public String toString() {
        return "Pairs { " + iter + " }";
    }
}
