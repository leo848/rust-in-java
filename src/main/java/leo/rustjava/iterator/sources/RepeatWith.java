package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.EndlessIterator;

import java.util.function.Supplier;

import static leo.rustjava.Option.Some;

public class RepeatWith<T> implements Iterator<T>, EndlessIterator<T> {
    private final Supplier<? extends T> supplier;

	public RepeatWith(Supplier<? extends T> supplier) {
		this.supplier = supplier;
	}

    @Override
    public Option<T> next() {
        return Some(supplier.get());
    }

    @Override
    public String toString() {
        return "RepeatWith";
    }
}
