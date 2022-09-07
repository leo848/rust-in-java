package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.function.Supplier;

import static leo.rustjava.Option.*;
import static leo.rustjava.iterator.Iterators.empty;

public class OnceWith<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
	private final Supplier<? extends T> supplier;
	private boolean exhausted;

	public OnceWith(Supplier<? extends T> supplier) {
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
	public DoubleEndedIterator<T> copy() {
		if (exhausted) return empty();
		return new OnceWith<>(supplier);
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
