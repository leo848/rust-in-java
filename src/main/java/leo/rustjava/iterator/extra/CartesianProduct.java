package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.adapters.Peekable;
import leo.rustjava.iterator.interfaces.IntoIter;

import java.util.ArrayList;
import java.util.List;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;

public class CartesianProduct<T, U> implements Iterator<Pair<T, U>> {
	private final Peekable<T> iterA;
	private final Iterator<U> iterB;
	private final List<U> cache;
	private int index = 0;
	private boolean exhausted = false;

	public CartesianProduct(Iterator<T> iterA, IntoIter<U> iterB) {
		this.iterA = iterA.peekable();
		this.iterB = iterB.iter().fuse();
		this.cache = new ArrayList<>();
	}

	@Override
	public Option<Pair<T, U>> next() {
		if (iterA.peek().isNone()) return None();

		if (exhausted) {
			if (index >= cache.size()) {
				index -= cache.size();
				iterA.next();
				return next();
			}
			return Some(new Pair<>(iterA.peek().unwrap(), cache.get(index++)));
		}

		var itemB = iterB.next();
		if (itemB.isSome()) {
			itemB.ifSome(cache::add);
			return Some(new Pair<>(iterA.peek().unwrap(), itemB.unwrap()));
		} else {
			exhausted = true;
			iterA.next();
			return next();
		}
	}

	@Override
	public SizeHint sizeHint() {
		return iterA.sizeHint().mul(iterB.sizeHint());
	}

	@Override
	public String toString() {
		return "CartesianProduct { " + iterA + ", " + iterB + " }";
	}
}
