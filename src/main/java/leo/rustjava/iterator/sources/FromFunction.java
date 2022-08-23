package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;

import java.util.function.Supplier;

public class FromFunction<T> implements Iterator<T> {
	Supplier<? extends Option<T>> supplier;

	public FromFunction(Supplier<? extends Option<T>> supplier) {
		this.supplier = supplier;
	}

	@Override
	public Option<T> next() {
		return supplier.get();
	}

	@Override
	public SizeHint sizeHint() {
		return SizeHint.UNKNOWN;
	}

	@Override
	public String toString() {
		return "FunctionIter";
	}
}
