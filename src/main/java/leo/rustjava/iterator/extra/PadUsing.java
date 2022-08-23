package leo.rustjava.iterator.extra;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

import static leo.rustjava.Option.Some;

public class PadUsing<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Function<? super Integer, ? extends T> function;
	private final int min;
	private int count = -1;

	public PadUsing(Iterator<T> iter, int min, Function<? super Integer, ? extends T> function) {
		this.iter = iter;
		this.min = min;
		this.function = function;
	}

	@Override
	public Option<T> next() {
		count++;
		var item = iter.next();
		if (item.isNone() && count < min) return Some(function.apply(count));
		return item;
	}

	@Override
	public SizeHint sizeHint() {
		return new SizeHint(min, iter.sizeHint().upper()).sub(count);
	}

	@Override
	public String toString() {
		return "PadUsing { " + iter + ", min: " + min + " }";
	}
}
