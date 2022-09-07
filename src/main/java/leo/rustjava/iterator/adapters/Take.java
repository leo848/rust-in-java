package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

public class Take<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private int n;

	public Take(Iterator<T> iter, int n) {
		this.iter = iter;
		this.n = n;
	}

	@Override
	public Option<T> next() {
		if (n > 0) {
			n--;
			return iter.next();
		} else {
			return Option.None();
		}
	}

	@Override
	public Iterator<T> copy() {
		return new Take<>(iter.copy(), n);
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.max(n);
	}

	@Override
	public String toString() {
		return "Take { " + iter + ", n: " + n + " }";
	}
}
