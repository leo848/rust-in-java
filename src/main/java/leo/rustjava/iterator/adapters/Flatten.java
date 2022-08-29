package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import static leo.rustjava.Option.None;

public class Flatten<T> implements Iterator<T> {
	private final Iterator<Iterator<T>> iter;
	private Option<Iterator<T>> tempIter = None();

	public Flatten(Iterator<Iterator<T>> iter) {
		this.iter = iter;
	}

	@Override
	public Option<T> next() {
		if (tempIter.isNone()) tempIter = iter.next();
		var item = tempIter.unwrap().next();
		if (item.isNone()) {
			tempIter.take();
			return next();
		} else return item;
	}
}
