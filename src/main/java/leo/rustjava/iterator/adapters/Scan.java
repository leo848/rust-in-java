package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import java.util.function.BiFunction;

public class Scan<T, U> implements Iterator<U> {
	private final Iterator<T> iter;
	private final BiFunction<U, T, U> f;
	private U state;

	public Scan(Iterator<T> iter, BiFunction<U, T, U> f, U state) {
		this.iter = iter;
		this.f = f;
		this.state = state;
	}

	@Override
	public Option<U> next() {
		Option<T> item = iter.next();
		if (item.isNone()) return Option.None();
		state = f.apply(state, item.unwrap());
		return Option.Some(state);
	}

	@Override
	public String toString() {
		return "Scan { " + iter + " }";
	}
}
