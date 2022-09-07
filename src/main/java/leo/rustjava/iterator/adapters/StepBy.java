package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

public class StepBy<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final int step;
	private boolean firstTake;

	public StepBy(Iterator<T> iter, int n) {
		this.iter = iter;
		this.step = n - 1;
		this.firstTake = true;
	}

	@Override
	public Option<T> next() {
		if (firstTake) {
			firstTake = false;
			return iter.next();
		} else {
			return iter.nth(step);
		}
	}

	@Override
	public Iterator<T> stepBy(int step) {
		if (!firstTake) return Iterator.super.stepBy(step);
		return new StepBy<>(iter, (this.step + 1) * step);
	}

	@Override
	public Iterator<T> copy() {
		if (!firstTake) return Iterator.super.copy();
		return new StepBy<>(iter.copy(), step);
	}

	@Override
	public String toString() {
		return "StepBy { " + iter + ", n: " + " }";
	}
}
