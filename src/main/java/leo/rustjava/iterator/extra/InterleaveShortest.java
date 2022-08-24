package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.FusedIterator;
import leo.rustjava.iterator.interfaces.IntoIter;

import static leo.rustjava.Option.*;

public class InterleaveShortest<T> implements Iterator<T>, FusedIterator<T> {
	private final Option<Iterator<T>> iterA;
	private final Option<Iterator<T>> iterB;
	boolean flip;

	public InterleaveShortest(Iterator<T> iterA, IntoIter<T> iterB) {
		this.iterA = Some(iterA);
		this.iterB = Some(iterB.iter());
		this.flip = false;
	}

	@SuppressWarnings("DuplicatedCode")
	@Override
	public Option<T> next() {
		flip = !flip;
		if (iterA.isNone() || iterB.isNone()) return None();
		Option<Iterator<T>> iter = (flip ? iterA : iterB).or(flip ? iterB : iterA);
		var item = iter.unwrap().next();
		if (item.isNone()) {
			iter.take(); // my iter now!
			return next();
		}
		return item;
	}

	@Override
	public SizeHint sizeHint() {
		return iterA
				.zip(iterB)
				.mapOr(SizeHint.ZERO, pair -> new SizeHint(
						pair.left().sizeHint().lower(),
						pair.right().sizeHint().upper())
				).mul(2);
	}

	@Override
	public String toString() {
		return "InterleaveShortest { " + iterA + ", " + iterB + " }";
	}
}
