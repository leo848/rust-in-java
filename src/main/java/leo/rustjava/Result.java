package leo.rustjava;

import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.IntoIter;
import leo.rustjava.iterator.sources.Empty;
import leo.rustjava.iterator.sources.Once;

import java.util.Objects;
import java.util.function.*;

import static leo.rustjava.Either.*;

public final class Result<T, E> implements IntoIter<T> {
	private final Either<T, E> inner;

	private Result(Option<? extends T> value, Option<? extends E> error) {
		var hasType = value.isSome();
		if (!hasType && error.isNone()) throw new NullPointerException("empty result");
		else if (hasType && error.isSome()) throw new NullPointerException("unionized result");
		inner = hasType ? Left(value.unwrap()) : Right(error.unwrap());
	}

	private Result(Either<T, E> inner) {
		this.inner = inner;
	}

	public static <T, E> Result<T, E> Ok(T value) {
		return new Result<>(Left(value));
	}

	public static <T, E> Result<T, E> Err(E error) {
		return new Result<>(Right(error));
	}

	public boolean isOk() {
		return inner.isLeft();
	}

	public boolean isOkAnd(Predicate<? super T> predicate) {
		return inner.isLeftAnd(predicate);
	}

	public boolean isErr() {
		return inner.isRight();
	}

	public boolean isErrAnd(Predicate<? super E> predicate) {
		return inner.isRightAnd(predicate);
	}

	public Option<T> ok() {
		return inner.left();
	}

	public Option<E> err() {
		return inner.right();
	}

	public <U> Result<U, E> map(Function<? super T, ? extends U> function) {
		return new Result<>(inner.mapLeft(function));
	}

	public <U> U mapOr(U defaultU, Function<? super T, U> function) {
		return inner.mapLeft(function).leftOr(defaultU);
	}

	public <U> U mapOrElse(Supplier<? extends U> supplier, Function<? super T, U> function) {
		return inner.mapLeft(function).leftOrElse(supplier);
	}

	public <E2> Result<T, E2> mapErr(Function<? super E, ? extends E2> function) {
		return new Result<>(inner.mapRight(function));
	}

	public <E2> E2 mapErrOr(E2 defaultErr, Function<? super E, E2> function) {
		return inner.mapRight(function).rightOr(defaultErr);
	}

	public <E2> E2 mapErrOrElse(Supplier<? extends E2> supplier, Function<? super E, E2> function) {
		return inner.mapRight(function).rightOrElse(supplier);
	}

	public Result<T, E> inspect(Consumer<? super T> consumer) {
		return new Result<>(inner.mapLeft(left -> {
			consumer.accept(left);
			return left;
		}));
	}

	public Result<T, E> inspectErr(Consumer<? super E> consumer) {
		return new Result<>(inner.mapRight(right -> {
			consumer.accept(right);
			return right;
		}));
	}

	public T expect(String msg) {
		if (isErr()) throw new NullPointerException(msg + ": " + unwrapErr());
		return unwrap();
	}

	public T unwrap() {
		return inner.unwrapLeft();
	}

	public E expectErr(String msg) {
		if (isOk()) throw new NullPointerException(msg + ": " + unwrap());
		return unwrapErr();
	}

	public E unwrapErr() {
		return inner.unwrapRight();
	}

	public <U> Result<U, E> and(Result<U, E> other) {
		if (isOk()) return other;
		return Err(unwrapErr());
	}

	public <U> Result<U, E> andThen(Function<? super T, Result<U, E>> function) {
		if (isOk()) return function.apply(unwrap());
		return Err(unwrapErr());
	}

	public <F> Result<T, F> or(Result<T, F> other) {
		if (isErr()) return other;
		return Ok(unwrap());
	}

	public <F> Result<T, F> orElse(Function<? super E, Result<T, F>> function) {
		if (isErr()) return function.apply(unwrapErr());
		return Ok(unwrap());
	}

	public T unwrapOr(T defaultT) {
		return inner.leftOr(defaultT);
	}

	public T unwrapOrElse(Supplier<? extends T> supplier) {
		return inner.leftOrElse(supplier);
	}

	public boolean contains(T value) {
		return isOkAnd(v -> Objects.equals(v, value));
	}

	public boolean containsErr(E error) {
		return isErrAnd(e -> Objects.equals(e, error));
	}

	@Override
	public Iterator<T> iter() {
		return mapOrElse(Empty::new, Once::new);
	}
}
