package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import java.util.function.Predicate;

import static leo.rustjava.Option.None;

public class SkipWhile<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Predicate<T> predicate;
	private boolean flag;

	public SkipWhile(Iterator<T> iter, Predicate<T> predicate) {
		this.iter = iter;
		this.predicate = predicate;
		this.flag = false;
	}

	@Override
	public Option<T> next() {
		if (flag) return iter.next();
		Option<T> item;
		do {
			item = iter.next();
			if (item.isNone()) return None();
			flag = true;
		} while (predicate.test(item.unwrap()));
		return item;
	}

	@Override
	public String toString() {
		return "SkipWhile { " + iter + " }";
	}
}
