package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.FusedIterator;
import leo.rustjava.iterator.interfaces.IntoIter;

import static leo.rustjava.Option.*;

public class Interleave<T> implements Iterator<T>, FusedIterator<T> {
	private final Option<Iterator<T>> iterA;
	private final Option<Iterator<T>> iterB;
	boolean flip;

	public Interleave(Iterator<T> iterA, IntoIter<T> iterB) {
		this.iterA = Some(iterA);
		this.iterB = Some(iterB.iter());
		this.flip = false;
	}

	@SuppressWarnings("DuplicatedCode")
	@Override
	public Option<T> next() {
		flip = !flip;
		if (iterA.isNone() && iterB.isNone()) return None();
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
		return iterA.mapOr(SizeHint.ZERO, Iterator::sizeHint)
				.add(iterB.mapOr(SizeHint.ZERO, Iterator::sizeHint));
	}

	@Override
	public String toString() {
		return "Interleave { " + iterA + ", " + iterB + " }";
	}
}
