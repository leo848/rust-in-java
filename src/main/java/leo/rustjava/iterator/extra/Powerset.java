package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.Iterators;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.List;

import static leo.rustjava.Option.*;

public class Powerset<T> implements Iterator<List<T>>, ExactSizeIterator<List<T>>, FusedIterator<List<T>> {
	private final List<T> cache;
	private int binaryCounter = 0;

	public Powerset(Iterator<T> iter) {
		this.cache = iter.toList();
	}

	@Override
	public Option<List<T>> next() {
		if (binaryCounter >= (1 << cache.size())) return None();
		var item = Some(Iterators
				.from(cache)
				.enumerate()
				.filter(pair -> (1 << pair.left() & binaryCounter) >> pair.left() != 0)
				.map(Pair::right)
				.toList()); // todo: powerset not sorted as in itertools
		binaryCounter++;
		return item;
	}

	@Override
	public int len() {
		return 1 << cache.size() - binaryCounter;
	}

	@Override
	public String toString() {
		return "Powerset { " + cache + " }";
	}
}
