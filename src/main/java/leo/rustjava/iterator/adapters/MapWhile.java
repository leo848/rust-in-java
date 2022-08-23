package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import java.util.function.Function;

public class MapWhile<T, U> implements Iterator<U> {
	private final Iterator<? extends T> iter;
	private final Function<? super T, ? extends Option<U>> function;

	public MapWhile(Iterator<? extends T> iter, Function<? super T, ? extends Option<U>> function) {
		this.iter = iter;
		this.function = function;
	}

	@Override
	public Option<U> next() {
		return iter.next().andThen(function);
	}

	@Override
	public String toString() {
		return "MapWhile { " + iter + " }";
	}
}
