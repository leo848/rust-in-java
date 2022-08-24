package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Predicate;

public class Filter<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Predicate<? super T> predicate;

	public Filter(Iterator<T> iter, Predicate<? super T> predicate) {
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

	@Override
	public Iterator<T> filter(Predicate<? super T> predicate) {
		return new Filter<>(iter, elt -> {
			if (!Filter.this.predicate.test(elt)) return false;
			return predicate.test(elt);
		});
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
