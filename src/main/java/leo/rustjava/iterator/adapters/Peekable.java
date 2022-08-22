package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;

import java.util.function.BiFunction;

import static leo.rustjava.Option.None;

public class Peekable<T> implements Iterator<T> {
	private final Iterator<T> iter;
	private final Option<Option<T>> peeked;

	public Peekable(Iterator<T> iter) {
		this.iter = iter;
		this.peeked = None();
	}

	@Override
	public Option<T> next() {
		Option<Option<T>> peeked = this.peeked.take();
		if (peeked.isSome()) return peeked.unwrap();
		return iter.next();
	}

	@Override
	public int count() {
		if (peeked.isSomeAnd(Option::isNone)) return 0;
		else if (peeked.isSome()) return 1 + iter.count();
		else return iter.count();
	}

	@Override
	public Option<T> nth(int n) {
		var peeked = this.peeked.take();
		if (peeked.isSomeAnd(Option::isNone)) return None();
		else if (peeked.isSome()) {
			if (n == 0) return peeked.unwrap();
			else return iter.nth(n - 1);
		} else return iter.nth(n);
	}

	@Override
	public Option<T> last() {
		var peeked = this.peeked.take();
		Option<T> peekedOpt = None();
		if (peeked.isSomeAnd(Option::isSome)) peekedOpt = peeked.unwrap();
		return iter.last().or(peekedOpt);
	}

	@Override
	public <B> B fold(B seed, BiFunction<B, T, B> f) {
		B acc = seed;
		if (peeked.isSomeAnd(Option::isNone)) return seed;
		else if (peeked.isSome()) acc = f.apply(seed, peeked.unwrap().unwrap());
		return iter.fold(acc, f);
	}

	public Option<T> peek() {
		return peeked.getOrInsertWith(iter::next);
	}

	@Override
	public String toString() {
		return "Peekable { " + iter + " }";
	}
}
