package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.BiPredicate;

import static leo.rustjava.Option.None;

public class DedupBy<T> implements Iterator<T> {
    private final Iterator<T> iter;
    private final BiPredicate<T, T> cmp;
    public Option<T> duplicate;

    public DedupBy(Iterator<T> iter, BiPredicate<T, T> cmp) {
        this.iter = iter;
        this.cmp = cmp;
        this.duplicate = None();
    }

    @Override
    public Option<T> next() {
        var item = iter.find(e -> duplicate.isNoneOr(d -> !cmp.test(d, e)));
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
        return "DedupBy { " + iter + " }";
    }
}
