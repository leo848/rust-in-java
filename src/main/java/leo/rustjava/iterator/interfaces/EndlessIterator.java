package leo.rustjava.iterator.interfaces;

import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.List;

public interface EndlessIterator<T> extends Iterator<T>, FusedIterator<T> {
	@Override
	default SizeHint sizeHint() {
		return SizeHint.ENDLESS;
	}

	@Override
	default List<T> toList() {
		throw new UnsupportedOperationException(
				"an endless iterator cannot be converted to an infinite data structure"
		);
	}
}
