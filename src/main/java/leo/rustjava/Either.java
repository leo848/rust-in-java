package leo.rustjava;

import java.util.function.*;

import static leo.rustjava.Option.*;

public final class Either<L, R> {
	private final Option<L> left;
	private final Option<R> right;

	private Either(L left, R right) {
		this(Maybe(left), Maybe(right));
	}

	private Either(Option<L> left, Option<R> right) {
		if (left.isNone() && right.isNone()) throw new NullPointerException("empty either");
		else if (left.isSome() && right.isSome()) throw new NullPointerException("unionized either");
		this.left = left;
		this.right = right;
	}

	public static <L, R> Either<L, R> Left(L value) {
		return new Either<>(Some(value), None());
	}

	public static <L, R> Either<L, R> Right(R value) {
		return new Either<>(None(), Some(value));
	}

	public boolean isLeft() {
		return left.isSome();
	}

	public boolean isLeftAnd(Predicate<? super L> predicate) {
		return left.isSomeAnd(predicate);
	}

	public boolean isRight() {
		return right.isSome();
	}

	public boolean isRightAnd(Predicate<? super R> predicate) {
		return right.isSomeAnd(predicate);
	}

	public Option<L> left() {
		return left;
	}

	public Option<R> right() {
		return right;
	}

	public Either<R, L> flip() {
		return new Either<>(right, left);
	}

	public <U> Either<U, R> mapLeft(Function<? super L, ? extends U> function) {
		return new Either<>(
				left.map(function),
				right
		);
	}

	public <U> Either<L, U> mapRight(Function<? super R, ? extends U> function) {
		return new Either<>(
				left,
				right.map(function)
		);
	}

	public <U> U either(Function<? super L, U> f, Function<? super R, ? extends U> g) {
		if (isLeft()) return f.apply(left.unwrap());
		else if (isRight()) return g.apply(right.unwrap());
		else throw new IllegalStateException("empty either");
	}

	public <U> Either<U, R> leftAndThen(Function<? super L, Either<U, R>> function) {
		if (isLeft()) return function.apply(left.unwrap());
		else return Right(right.unwrap());
	}

	public <U> Either<L, U> rightAndThen(Function<? super R, Either<L, U>> function) {
		if (isRight()) return function.apply(right.unwrap());
		else return Left(left.unwrap());
	}

	public L leftOr(L other) {
		return left.unwrapOr(other);
	}

	public L leftOrElse(Supplier<? extends L> supplier) {
		return left.unwrapOrElse(supplier);
	}

	public R rightOr(R other) {
		return right.unwrapOr(other);
	}

	public R rightOrElse(Supplier<? extends R> supplier) {
		return right.unwrapOrElse(supplier);
	}

	public L unwrapLeft() {
		return left.unwrap();
	}

	public R unwrapRight() {
		return right.unwrap();
	}

	public L expectLeft(String msg) {
		return left.expect(msg);
	}

	public R expectRight(String msg) {
		return right.expect(msg);
	}

	public void match(Consumer<? super L> f, Consumer<? super R> g) {
		if (isLeft()) f.accept(left.unwrap());
		else if (isRight()) g.accept(right.unwrap());
		throw new NullPointerException("empty either");
	}
}
