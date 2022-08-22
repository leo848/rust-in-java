package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import static leo.rustjava.Option.None;

public class PairWindows<T> implements Iterator<Pair<T, T>> {
	private final Iterator<T> iter;
	private Option<Pair<T, T>> pair;

	public PairWindows(Iterator<T> iter) {
		this.iter = iter;
		this.pair = None();
	}

	@Override
	public Option<Pair<T, T>> next() {
		if (pair.isNone()) {
			pair = iter.next().zip(iter.next());
			return pair;
		}
		pair = pair.map(Pair::right).zip(iter.next());
		return pair;
	}

	@Override
	public SizeHint sizeHint() {
		return new SizeHint(
				iter.sizeHint().lower() - 1,
				iter.sizeHint().upper()
		);
	}
}
