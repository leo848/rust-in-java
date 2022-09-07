package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import static leo.rustjava.Option.Some;

public class Intersperse<T> implements Iterator<T> {
	private final Peekable<T> iter;
	private final T separator;
	private boolean needsSep;


	public Intersperse(Iterator<T> iter, T separator) {
		this.iter = iter.peekable();
		this.separator = separator;
		this.needsSep = false;
	}

	@Override
	public Option<T> next() {
		if (needsSep && iter.peek().isSome()) {
			needsSep = false;
			return Some(separator);
		} else {
			needsSep = true;
			return iter.next();
		}
	}

	@Override
	public Iterator<T> copy() {
		if (needsSep) return Iterator.super.copy();
		return new Intersperse<>(iter.copy(), separator);
	}

	@Override
	public String toString() {
		return "Intersperse { " + iter + ", " + separator + " }";
	}
}
