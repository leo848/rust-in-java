package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.HashMap;

import static leo.rustjava.Option.None;

public class Duplicates<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final HashMap<T, Integer> presentItems;

	public Duplicates(Iterator<T> iter) {
		this.iter = iter;
		this.presentItems = new HashMap<>();
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		if (item.isNone()) return None();
		presentItems.put(item.unwrap(), presentItems.getOrDefault(item.unwrap(), 0) + 1);

		if (presentItems.get(item.unwrap()) == 2) return item;
		else return next();
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.max(iter.sizeHint());
	}

	@Override
	public String toString() {
		return "Duplicates { " + iter + " }";
	}
}
