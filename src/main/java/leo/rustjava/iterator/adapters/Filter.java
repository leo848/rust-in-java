package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Predicate;

public class Filter<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Predicate<T> predicate;

	public Filter(Iterator<T> iter, Predicate<T> predicate) {
		this.iter = iter;
		this.predicate = predicate;
	}

	@Override
	public Option<T> next() {
		while (true) {
			Option<T> item = iter.next();
			if (item.isNone()) return Option.None();
			if (predicate.test(item.unwrap())) {
				return item;
			}
		}
	}

	@SuppressWarnings("BoundedWildcard")
	@Override
	public Iterator<T> filter(final Predicate<T> predicate) {
		return new Filter<>(iter, this.predicate.and(predicate));
	}

	@Override
	public SizeHint sizeHint() {
		return new SizeHint(0, iter.sizeHint().upper());
	}

	@Override
	public String toString() {
		return "Filter { " + iter + " }";
	}
}
