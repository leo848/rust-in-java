package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

public class StepBy<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final int n;
	private boolean firstTake;

	public StepBy(Iterator<T> iter, int n) {
		this.iter = iter;
		this.n = n - 1;
		this.firstTake = true;
	}

	@Override
	public Option<T> next() {
		if (firstTake) {
			firstTake = false;
			return iter.next();
		} else {
			return iter.nth(n);
		}
	}

	@Override
	public String toString() {
		return "StepBy { " + iter + ", n: " + " }";
	}
}
