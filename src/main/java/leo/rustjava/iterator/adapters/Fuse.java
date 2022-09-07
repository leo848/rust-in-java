package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

public class Fuse<T> implements Iterator<T>, FusedIterator<T> {
	private final Iterator<T> iter;
	private boolean fused = false;

	public Fuse(Iterator<T> iter) {
		this.iter = iter;
	}

	@Override
	public Iterator<T> copy() {
		return new Fuse<>(iter.copy());
	}

	@Override
	public Option<T> next() {
		if (fused) {
			return Option.None();
		}
		Option<T> item = iter.next();
		if (item.isNone()) {
			fused = true;
			return Option.None();
		}
		return item;
	}

	@Override
	public String toString() {
		return "Fuse { " + iter + " }";
	}
}
