package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.HashSet;
import java.util.function.Function;

import static leo.rustjava.Option.None;

public class UniqueBy<T, U> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Function<T, U> id;
	private final HashSet<U> alreadyPresent;

	public UniqueBy(Iterator<T> iter, Function<T, U> id) {
		this.iter = iter;
		this.id = id;
		alreadyPresent = new HashSet<>();
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		if (item.isNone()) return None();
		var key = id.apply(item.unwrap());
		if (alreadyPresent.contains(key)) return next();
		alreadyPresent.add(key);
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
