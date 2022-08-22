package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

public class FilterMap<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Function<T, Option<T>> f;

	public FilterMap(Iterator<T> iter, Function<T, Option<T>> f) {
		this.iter = iter;
		this.f = f;
	}

	@Override
	public Option<T> next() {
		return iter.next().andThen(f);
	}

	@Override
	public SizeHint sizeHint() {
		return new SizeHint(0, iter.sizeHint().upper());
	}

	@Override
	public String toString() {
		return "FilterMap { " + iter + " }";
	}
}
