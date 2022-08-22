package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.function.Supplier;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;

public class OnceWith<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    private final Supplier<T> supplier;
    private boolean exhausted;

    public OnceWith(Supplier<T> supplier) {
        this.supplier = supplier;
        this.exhausted = false;
    }

    @Override
    public int len() {
        return exhausted ? 0 : 1;
    }

    @Override
    public Option<T> nextBack() {
        return next();
    }

    @Override
    public Option<T> next() {
        if (exhausted) {
            return None();
        } else {
            exhausted = true;
            return Some(supplier.get());
        }
    }
}
