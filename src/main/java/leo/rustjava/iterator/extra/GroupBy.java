package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.adapters.Peekable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static leo.rustjava.Option.*;

public class GroupBy<T, K> implements Iterator<Pair<K, List<T>>> {
    private final Peekable<T> iter;
    private final Function<? super T, K> key;

    public GroupBy(Iterator<T> iter, Function<? super T, K> key) {
        this.iter = iter.peekable();
        this.key = key;
    }

    @Override
    public Option<Pair<K, List<T>>> next() {
        List<T> list = new ArrayList<>();
        Option<K> maybeFirstKey = iter.peek().map(key);
        if (maybeFirstKey.isNone()) return None();
        K firstKey = maybeFirstKey.unwrap();

        while (true) {
            if (iter.peek().isNone() && iter.next().isNone()) break;
                // SAFETY: if Peekable is implemented correctly this must never fail
            else if (iter.peek().map(key).contains(firstKey))
                list.add(iter.next().unwrap());
            else break;
        }

        return Some(new Pair<>(firstKey, list));
    }

    @Override
    public SizeHint sizeHint() {
        return iter.sizeHint();
    }

    @Override
    public String toString() {
        return "GroupBy { " + iter + " }";
    }
}
