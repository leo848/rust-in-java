package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import static leo.rustjava.Option.*;
import static leo.rustjava.iterator.Iterators.from;

public class Range implements Iterator<Integer>, DoubleEndedIterator<Integer>, ExactSizeIterator<Integer>, FusedIterator<Integer> {
	private int start;
	private int end;

	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public Option<Integer> next() {
		if (start < end) {
			return Some(start++);
		} else {
			return None();
		}
	}

	@Override
	public boolean contains(Integer query) {
		boolean result = query < end && query >= start;
		if (!result) start = end;
		else start = query + 1;
		return result;
	}

	@Override
	public int len() {
		return start - end;
	}

	@Override
	public boolean isEmpty() {
		return start >= end;
	}

	@Override
	public Iterator<Integer> dedup() {
		return this;
	}

	@Override
	public Iterator<Integer> skip(int n) {
		start += n;
		return this;
	}

	@Override
	public Option<Integer> last() {
		if (isEmpty()) return None();
		start = end - 1;
		return next();
	}

	@Override
	public Iterator<Integer> duplicates() {
		return new Empty<>();
	}

	@Override
	public Iterator<Integer> unique() {
		return this;
	}

	@Override
	public boolean allEqual() {
		return len() <= 1;
	}

	@Override
	public boolean allUnique() {
		return true;
	}

	@Override
	public ListIter<Integer> sorted() {
		return from(toList());
	}

	@Override
	public Option<Integer> positionMin() {
		return Some(0);
	}

	@Override
	public Iterator<Integer> take(int n) {
		end = start + n;
		return this;
	}

	@Override
	public Iterator<Integer> kSmallest(int k) {
		return take(k);
	}

	@Override
	public Option<Integer> nextBack() {
		if (start < end) {
			return Some(--end);
		} else {
			return None();
		}
	}

	@Override
	public String toString() {
		return start + ".." + end;
	}
}
