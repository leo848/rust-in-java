package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

public class FilterMap<T> implements Iterator<T> {
	private final Iterator<? extends T> iter;
	private final Function<? super T, ? extends Option<T>> f;

	public FilterMap(Iterator<? extends T> iter, Function<? super T, ? extends Option<T>> f) {
		this.iter = iter;
		this.f = f;
	}

	@Override
	public Option<T> next() {
		return iter.next().andThen(f);
	}

	@Override
	public Iterator<T> filterMap(Function<? super T, ? extends Option<T>> f) {
		return new FilterMap<>(iter, this.f.andThen(option -> option.andThen(f)));
	}

	@Override
	public SizeHint sizeHint() {
		return new SizeHint(0, iter.sizeHint().upper());
	}

	@Override
	public String toString() {
		return "FilterMap { " + iter + " }";
	}
}
