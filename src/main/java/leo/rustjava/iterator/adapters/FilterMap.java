package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Function;
import java.util.function.Predicate;

import static leo.rustjava.Option.*;

public class FilterMap<T, U> implements Iterator<U> {
	private final Iterator<? extends T> iter;
	private final Function<? super T, ? extends Option<U>> f;

	public FilterMap(Iterator<? extends T> iter, Function<? super T, ? extends Option<U>> f) {
		this.iter = iter;
		this.f = f;
	}

	@Override
	public Option<U> next() {
		return iter.next().andThen(f);
	}

	@Override
	public <V> Iterator<V> filterMap(Function<? super U, ? extends Option<V>> f) {
		return new FilterMap<>(iter, this.f.andThen(option -> option.andThen(f)));
	}


	@Override
	public Iterator<U> filter(Predicate<? super U> p) {
		return new FilterMap<>(
				iter,
				this.f.andThen(
						option -> option.andThen(elt -> p.test(elt) ? Some(elt) : None())
				)
		);
	}

	@Override
	public <V> Iterator<V> map(Function<? super U, ? extends V> f) {
		return new FilterMap<>(
				iter,
				this.f.andThen(opt -> opt.map(f))
		);
	}

	@Override
	public Iterator<U> copy() {
		return new FilterMap<>(iter.copy(), f);
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
