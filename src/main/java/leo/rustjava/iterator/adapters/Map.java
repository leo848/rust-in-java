package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;

public class Map<T, U> implements Iterator<U> {
	private final Iterator<? extends T> iter;
	private final Function<? super T, ? extends U> f;

	public Map(Iterator<? extends T> iter, Function<? super T, ? extends U> f) {
		this.iter = iter;
		this.f = f;
	}

	@Override
	public Option<U> next() {
		return iter.next().map(f);
	}

	@Override
	public SizeHint sizeHint() {
		return iter.sizeHint();
	}

	@Override
	public <V> Iterator<V> map(Function<? super U, ? extends V> f) {
		return new Map<>(iter, this.f.andThen(f));
	}

	@Override
	public Iterator<U> copy() {
		return new Map<>(iter.copy(), f);
	}

	@Override
	public String toString() {
		return "Map { " + iter + " }";
	}
}
