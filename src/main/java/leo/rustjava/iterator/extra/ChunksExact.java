package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.ArrayList;
import java.util.List;

import static leo.rustjava.Option.*;
import static leo.rustjava.iterator.Iterators.fromList;

public class ChunksExact<T> implements Iterator<ExactSizeIterator<T>>, FusedIterator<ExactSizeIterator<T>> {
	private final Iterator<? extends T> iter;
	private final int chunkSize;

	public ChunksExact(Iterator<? extends T> iter, int chunkSize) {
		this.iter = iter;
		this.chunkSize = chunkSize;
	}

	@Override
	public Option<ExactSizeIterator<T>> next() {
		List<T> list = new ArrayList<>();
		iter.take(chunkSize).forEach(list::add);
		if (list.size() < chunkSize) return None();
		else return Some(fromList(list));
	}
}
