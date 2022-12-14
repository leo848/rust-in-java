package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Predicate;

import static leo.rustjava.Option.*;

public class Positions<T> implements Iterator<Integer> {
	private final Iterator<? extends T> iter;
	private final Predicate<? super T> predicate;
	private int count = -1;

	public Positions(Iterator<? extends T> iter, Predicate<? super T> predicate) {
		this.iter = iter;
		this.predicate = predicate;
	}

	@Override
	public Option<Integer> next() {
		count++;
		var item = iter.next();
		if (item.isNone()) return None();
		if (predicate.test(item.unwrap())) return Some(count);
		return next();
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.max(iter.sizeHint());
	}

	@Override
	public String toString() {
		return "Positions { " + iter + " }";
	}
}
