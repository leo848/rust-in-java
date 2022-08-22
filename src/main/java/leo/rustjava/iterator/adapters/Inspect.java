package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Consumer;

public class Inspect<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Consumer<T> consumer;

	public Inspect(Iterator<T> iter, Consumer<T> consumer) {
		this.iter = iter;
		this.consumer = consumer;
	}

	@Override
	public Option<T> next() {
		var item = iter.next();
		if (item.isNone()) return item;
		consumer.accept(item.unwrap());
		return item;
	}

	@Override
	public SizeHint sizeHint() {
		return iter.sizeHint();
	}

	@Override
	public String toString() {
		return "Inspect { " + iter + " }";
	}
}
