package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.IntoIter;

import static leo.rustjava.Option.*;

public class Chain<T> implements Iterator<T> {
	private final Iterator<T> iterB;
	private Option<Iterator<T>> iterA;

	public Chain(Iterator<T> a, IntoIter<T> b) {
		this.iterA = Some(a.fuse());
		this.iterB = b.iter().fuse();
	}

	@Override
	public Option<T> next() {
		if (iterA.isNone()) return iterB.next();
		Option<T> item = iterA.unwrap().next();
		if (item.isNone()) {
			iterA = None();
			return next();
		}
		return item;
	}

	@Override
	public Iterator<T> copy() {
		if (iterA.isNone()) return iterB.copy();
		return new Chain<>(iterA.unwrap().copy(), iterB.copy());
	}

	@Override
	public SizeHint sizeHint() {
		return iterA.mapOr(SizeHint.ZERO, Iterator::sizeHint).add(iterB.sizeHint());
	}

	@Override
	public String toString() {
		return "Chain { " + iterA + ", " + iterB + " }";
	}
}
