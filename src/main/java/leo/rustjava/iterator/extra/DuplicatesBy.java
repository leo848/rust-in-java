package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.HashMap;
import java.util.function.Function;

import static leo.rustjava.Option.None;

public class DuplicatesBy<T, U> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Function<? super T, ? extends U> id;
	private final HashMap<U, Integer> presentItems;

	public DuplicatesBy(Iterator<T> iter, Function<? super T, ? extends U> id) {
		this.iter = iter;
		this.id = id;
		this.presentItems = new HashMap<>();
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		if (item.isNone()) return None();

		U key = id.apply(item.unwrap());
		presentItems.put(key, presentItems.getOrDefault(key, 0) + 1);

		if (presentItems.get(key) == 2) return item;
		else return next();
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.max(iter.sizeHint());
	}

	@Override
	public String toString() {
		return "DuplicatesBy { " + iter + " }";
	}
}
