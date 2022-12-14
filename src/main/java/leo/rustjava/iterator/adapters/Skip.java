package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

public class Skip<T> implements Iterator<T> {
    Iterator<T> iter;
    int n;

    public Skip(Iterator<T> iter, int n) {
        this.iter = iter;
        this.n = n;
    }

    @Override
    public Option<T> next() {
        while (n > 0) {
            Option<T> item = iter.next();
            if (item.isNone()) return item;
            n--;
        }
        return iter.next();
    }

    @Override
    public Iterator<T> skip(int n) {
        return new Skip<>(iter, this.n + n);
    }

    @Override
    public SizeHint sizeHint() {
        return new SizeHint(iter.sizeHint().lower(), iter.sizeHint().upper().map(n -> n - this.n));
    }

    @Override
    public Iterator<T> copy() {
        return new Skip<>(iter.copy(), n);
    }

    @Override
    public String toString() {
        return "Skip { " + iter + ", n: " + n + " }";
    }
}
