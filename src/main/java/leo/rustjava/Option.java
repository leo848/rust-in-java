package leo.rustjava;

import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.Iterators;
import leo.rustjava.iterator.interfaces.IntoIter;

import java.util.Objects;
import java.util.function.*;

@SuppressWarnings({"unused", "AssignmentToNull"})
public final class Option<T> implements IntoIter<T> {
	public static Option<Object> None = None();
	private T value;

	private Option() {
	}

	// Methods provided by Rust (a language 100x better than this)

	private Option(T value) {
		this.value = value;
	}

	public static <T> Option<T> Maybe(T value) {
		return value == null ? None() : Some(value);
	}

	public static <T> Option<T> None() {
		return new Option<>();
	}

	public static <T> Option<T> Some(T value) {
		if (value == null) throw new NullPointerException();
		return new Option<>(value);
	}

	public T unwrapOr(T defaultT) {
		if (isNone()) return defaultT;
		return this.unwrap();
	}

	public boolean isNone() {
		return value == null;
	}

	public T unwrap() {
		if (isNone()) throw new NullPointerException();
		return value;
	}

	public T unwrapOrElse(Supplier<? extends T> supplier) {
		if (isNone()) return supplier.get();
		return this.unwrap();
	}

	public T expect(String msg) {
		if (isNone()) throw new NullPointerException(msg);
		return value;
	}

	public boolean isSomeAnd(Predicate<? super T> predicate) {
		Option<Boolean> maybe = this.map(predicate::test);
		if (maybe.isNone()) return false;
		return maybe.unwrap();
	}

	public boolean isNoneOr(Predicate<? super T> predicate) {
		if (isNone()) return true;
		return predicate.test(this.unwrap());
	}

	public <U> U mapOrElse(Supplier<? extends U> supplier, Function<? super T, ? extends U> function) {
		if (isNone()) return supplier.get();
		return this.map(function).unwrap();
	}

	public <U> Option<U> map(Function<? super T, ? extends U> function) {
		if (value == null) return Option.None();
		return Option.Some(function.apply(this.unwrap()));
	}

	public <U> Option<U> and(Option<U> option) {
		if (isNone() || option.isNone()) return Option.None();
		return option;
	}

	public <U> Option<U> andThen(Function<? super T, ? extends Option<U>> function) {
		if (isNone()) return Option.None();
		return function.apply(this.unwrap());
	}

	public Option<T> filter(Predicate<? super T> predicate) {
		if (isNone()) return Option.None();
		else if (predicate.test(this.unwrap())) {
			return this;
		} else {
			return Option.None();
		}
	}

	public Option<T> or(Option<T> option) {
		if (isSome()) return this;
		return option;
	}

	public boolean isSome() {
		return !isNone();
	}

	public Option<T> orElse(Supplier<? extends Option<T>> supplier) {
		if (isSome()) return this;
		else return supplier.get();
	}

	public Option<T> xor(Option<T> option) {
		if (isSome() && option.isSome()) return Option.None();
		else if (isSome()) return this;
		else return option;
	}

	@SuppressWarnings("UnusedReturnValue")
	public Option<T> insert(T value) {
		this.value = value;
		return this;
	}

	public T getOrInsert(T value) {
		if (isNone()) this.value = value;
		return this.unwrap();
	}

	public T getOrInsertWith(Supplier<? extends T> supplier) {
		if (isNone()) this.value = supplier.get();
		return this.unwrap();
	}

	public Option<T> take() {
		if (isNone()) return Option.None();
		T newVal = this.unwrap();
		this.value = null;
		return Option.Some(newVal);
	}

	public Option<T> replace(T value) {
		Option<T> newVal = this.shallowCopy();
		this.value = value;
		return newVal;
	}

	Option<T> shallowCopy() {
		if (isNone()) return Option.None();
		return Option.Some(this.unwrap());
	}

	public boolean contains(T value) {
		if (isNone()) return false;
		return this.unwrap().equals(value);
	}

	public Option<T> inspect(Consumer<? super T> consumer) {
		if (isSome()) consumer.accept(this.unwrap());
		return this;
	}

	public <U> Option<Pair<T, U>> zip(Option<? extends U> other) {
		if (this.isNone() || other.isNone()) return None();
		return Some(new Pair<>(this.unwrap(), other.unwrap()));
	}

	public void ifSome(Consumer<? super T> consumer) {
		if (isNone()) return;
		consumer.accept(this.unwrap());
	}

	public void ifElse(Consumer<? super T> consumer, Runnable runnable) {
		if (isNone()) runnable.run();
		else consumer.accept(this.unwrap());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Option<?> option = (Option<?>) o;

		return Objects.equals(value, option.value);
	}

	@Override
	public Iterator<T> iter() {
		return mapOr(Iterators.empty(), Iterators::once);
	}

	public <U> U mapOr(U defaultU, Function<? super T, ? extends U> function) {
		if (isNone()) return defaultU;
		return this.map(function).unwrap();
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}

	public String toString() {
		if (value == null) {
			return "None";
		} else {
			return "Some(" + value + ")";
		}
	}
}
