package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import static leo.rustjava.Option.*;

public class FromIterable<T> implements Iterator<T> {
	private final java.util.Iterator<? extends T> iter;

	public FromIterable(java.util.Iterator<? extends T> iter) {
		this.iter = iter;
	}

	@Override
	public Option<T> next() {
		if (iter.hasNext()) {
			return Some(iter.next());
		} else return None();
	}
}
