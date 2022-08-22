package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.ArrayList;
import java.util.List;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;

public class Cycle<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final List<T> cache;
	private int index = 0;

	public Cycle(Iterator<T> iter) {
		this.iter = iter.fuse();
		this.cache = new ArrayList<>();
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		item.ifSome(cache::add);
		if (item.isSome()) return item;
		else {
			int size = cache.size();
			if (size == 0) return None();
			index %= size;
			try {
				return Some(cache.get(index++));
			} catch (IndexOutOfBoundsException e) {
				return None();
			}
		}
	}

	@Override
	public SizeHint sizeHint() {
		return this.index == 0 ? SizeHint.UNKNOWN : this.cache.size() == 0 ? SizeHint.ZERO : SizeHint.ENDLESS;
	}

	@Override
	public String toString() {
		return "Cycle { " + iter + " }";
	}
}
