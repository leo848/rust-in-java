package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

import static leo.rustjava.Option.Some;

public class Successors<T> implements Iterator<T> {
	Function<T, Option<T>> successor;
	Option<T> value;

	public Successors(T seed, Function<T, Option<T>> successor) {
		this.value = Some(seed);
		this.successor = successor;
	}

	@Override
	public Option<T> next() {
		if (value.isNone()) return Option.None();
		T oldValue = value.unwrap();
		value = successor.apply(oldValue);
		return Some(oldValue);
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.UNKNOWN;
	}

	@Override
	public String toString() {
		return "Successors";
	}
}
