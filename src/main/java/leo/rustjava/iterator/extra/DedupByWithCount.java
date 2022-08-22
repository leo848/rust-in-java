package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.BiPredicate;

import static leo.rustjava.Option.None;

public class DedupByWithCount<T> implements Iterator<Pair<Integer, T>> {
    private final Iterator<T> iter;
    private final BiPredicate<T, T> cmp;
    public Option<T> duplicate;

    public DedupByWithCount(Iterator<T> iter, BiPredicate<T, T> cmp) {
        this.iter = iter;
        this.cmp = cmp;
        this.duplicate = None();
    }

    @Override
    public Option<Pair<Integer, T>> next() {
        var item = iter.enumerate().find(p -> duplicate.isNoneOr(d -> !cmp.test(d, p.right())));
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
        return "DedupByWithCount { " + iter + " }";
    }
}
