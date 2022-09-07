package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;

import static leo.rustjava.Option.*;

public class Enumerate<T> implements Iterator<Pair<Integer, T>> {
	private final Iterator<T> iter;
	int count = 0;

	public Enumerate(Iterator<T> iter) {
		this.iter = iter;
	}

	public Enumerate(Iterator<T> iter, int count) {
		this.iter = iter;
		this.count = count;
	}


	@Override
	public Option<Pair<Integer, T>> next() {
		Option<T> item = iter.next();
		if (item.isNone()) return None();
		var i = count;
		count += 1;
		return Some(new Pair<>(i, item.unwrap()));
	}

	@Override
	public Iterator<Pair<Integer, T>> copy() {
		return new Enumerate<>(iter.copy(), count);
	}

	@Override
	public String toString() {
		return "Enumerate { " + iter + " }";
	}
}
