package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import static leo.rustjava.Option.None;

public class Dedup<T> implements Iterator<T> {
    private final Iterator<T> iter;
    private final Option<T> duplicate;

    public Dedup(Iterator<T> iter) {
        this.iter = iter;
        this.duplicate = None();
    }

    @Override
    public Option<T> next() {
        var item = iter.find(e -> duplicate.isNoneOr(d -> !d.equals(e)));
        if (item.isNone()) return None();
        duplicate.insert(item.unwrap());
        return item;
    }

    @Override
    public SizeHint sizeHint() {
        return SizeHint.max(iter.sizeHint());
    }

    @Override
    public String toString() {
        return "Dedup { " + iter + " }";
    }
}
