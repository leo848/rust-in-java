package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.IntoIter;

import java.util.function.Function;

import static leo.rustjava.iterator.Iterators.empty;

public class FlatMap<T, U> implements Iterator<U> {
	private final Iterator<? extends T> iter;
	private final Function<T, Iterator<U>> f;

	Iterator<U> currentIter = empty();

	public FlatMap(Iterator<? extends T> iter, Function<T, IntoIter<U>> f) {
		this.iter = iter;
		this.f = f.andThen(IntoIter::iter);
	}

	@Override
	public Option<U> next() {
		Option<U> item = currentIter.next();
		if (item.isSome()) return item;

		Option<Iterator<U>> nextIter = iter.next().map(f);
		if (nextIter.isNone()) return Option.None();
		currentIter = nextIter.unwrap();

		return next();
	}

	@Override
	public String toString() {
		return "FlatMap { " + iter + " }";
	}
}
