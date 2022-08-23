package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.HashSet;
import java.util.Set;

import static leo.rustjava.Option.None;

public class Unique<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Set<T> alreadyPresent;

	public Unique(Iterator<T> iter) {
		this.iter = iter;
		alreadyPresent = new HashSet<>();
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		if (item.isNone()) return None();
		if (alreadyPresent.contains(item.unwrap())) return next();
		alreadyPresent.add(item.unwrap());
		return item;
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.max(iter.sizeHint());
	}

	@Override
	public String toString() {
		return "Unique { " + iter + " }";
	}
}
