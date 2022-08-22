package leo.rustjava.iterator.interfaces;

import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

public interface ExactSizeIterator<T> extends Iterator<T> {
	default boolean isEmpty() {
		return len() == 0;
	}

	int len();

	@Override
	default SizeHint sizeHint() {
		return SizeHint.exact(len());
	}
}
