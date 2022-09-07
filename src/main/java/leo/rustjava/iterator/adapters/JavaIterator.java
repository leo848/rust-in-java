package leo.rustjava.iterator.adapters;

import leo.rustjava.iterator.Iterator;

import java.util.NoSuchElementException;

public class JavaIterator<T> implements java.util.Iterator<T> {
	private final Peekable<T> iter;

	public JavaIterator(Iterator<T> iter) {
		this.iter = iter.peekable();
	}

	@Override
	public boolean hasNext() {
		return iter.peek().isSome();
	}

	@Override
	public T next() {
		return iter.next().unwrapOrElse(() -> {
			throw new NoSuchElementException();
		});
	}
}
