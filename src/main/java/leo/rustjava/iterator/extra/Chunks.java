package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.ArrayList;
import java.util.List;

import static leo.rustjava.Option.*;
import static leo.rustjava.iterator.Iterators.from;

public class Chunks<T> implements Iterator<Iterator<T>>, FusedIterator<Iterator<T>> {
    private final Iterator<? extends T> iter;
    private final int chunkSize;

    public Chunks(Iterator<? extends T> iter, int chunkSize) {
        this.iter = iter;
        this.chunkSize = chunkSize;
    }

    @Override
    public Option<Iterator<T>> next() {
        List<T> list = new ArrayList<>();
        iter.take(chunkSize).forEach(list::add);
        if (list.isEmpty()) return None();
        else return Some(from(list));
    }

    @Override
    public SizeHint sizeHint() {
        return new SizeHint(
                iter.sizeHint().lower() / chunkSize + 1,
                iter.sizeHint().upper().map(n -> n / chunkSize + 1)
        );
    }

    @Override
    public String toString() {
        return "Chunks { " + iter + ", size: " + " } ";
    }
}
