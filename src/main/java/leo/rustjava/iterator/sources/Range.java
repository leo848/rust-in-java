package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

public class Range implements Iterator<Integer>, DoubleEndedIterator<Integer>, ExactSizeIterator<Integer>, FusedIterator<Integer> {
	int start;
	int end;

	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public Option<Integer> next() {
		if (start < end) {
			return Option.Some(start++);
		} else {
			return Option.None();
		}
	}

	@Override
	public int len() {
		return start - end;
	}

	@Override
	public Option<Integer> nextBack() {
		if (start < end) {
			return Option.Some(--end);
		} else {
			return Option.None();
		}
	}

	@Override
	public String toString() {
		return start + ".." + end;
	}
}
