package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

public class Map<T, U> implements Iterator<U> {
	private final Iterator<T> iter;
	private final Function<T, U> f;

	public Map(Iterator<T> iter, Function<T, U> f) {
		this.iter = iter;
		this.f = f;
	}

	@Override
	public Option<U> next() {
		return iter.next().map(f);
	}

	@Override
	public SizeHint sizeHint() {
		return iter.sizeHint();
	}

	@Override
	public String toString() {
		return "Map { " + iter + " }";
	}
}
