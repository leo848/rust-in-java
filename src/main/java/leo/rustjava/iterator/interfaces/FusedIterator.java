package leo.rustjava.iterator.interfaces;

import leo.rustjava.iterator.Iterator;

public interface FusedIterator<T> extends Iterator<T> {
	@Override
	default Iterator<T> fuse() {
		return this;
	}
}
