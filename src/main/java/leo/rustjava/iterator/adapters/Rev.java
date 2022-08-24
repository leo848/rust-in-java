package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;

public class Rev<T> implements Iterator<T>, DoubleEndedIterator<T> {
    private final DoubleEndedIterator<T> iter;

	public Rev(DoubleEndedIterator<T> iter) {
		this.iter = iter;
	}

	@Override
	public Option<T> next() {
		return iter.nextBack();
	}

	@Override
	public DoubleEndedIterator<T> rev() {
		return iter;
	}

	@Override
	public Option<T> nextBack() {
		return iter.next();
	}
}
