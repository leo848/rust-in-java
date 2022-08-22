package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import static leo.rustjava.Option.None;

public class DedupWithCount<T> implements Iterator<Pair<Integer, T>> {
    private final Iterator<T> iter;
    private final Option<T> duplicate;

    public DedupWithCount(Iterator<T> iter) {
        this.iter = iter;
        this.duplicate = None();
    }

    @Override
    public Option<Pair<Integer, T>> next() {
        var item = iter
                .enumerate()
                .find(pair -> duplicate.isNoneOr(d -> !d.equals(pair.right())));
        if (item.isNone()) return None();
        duplicate.insert(item.unwrap().right());
        return item;
    }

    @Override
    public SizeHint sizeHint() {
        return SizeHint.max(iter.sizeHint());
    }

    @Override
    public String toString() {
        return "DedupWithCount { " + iter + " }";
    }
}
