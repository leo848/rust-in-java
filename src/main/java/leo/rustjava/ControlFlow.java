package leo.rustjava;

import java.util.function.Function;

import static leo.rustjava.Either.*;
import static leo.rustjava.Unit.Unit;

public final class ControlFlow<B, C> {
	private final Either<B, C> inner;

	private ControlFlow(Either<B, C> inner) {
		this.inner = inner;
	}

	public static <B, C> ControlFlow<B, C> Break(B breakValue) {
		return new ControlFlow<>(Left(breakValue));
	}

	public static <C> ControlFlow<Unit, C> Break() {
		return Break(Unit());
	}

	public static <B, C> ControlFlow<B, C> Continue(C continueValue) {
		return new ControlFlow<>(Right(continueValue));
	}

	public static <B> ControlFlow<B, Unit> Continue() {
		return Continue(Unit());
	}

	public boolean isBreak() {
		return inner.isLeft();
	}

	public boolean isContinue() {
		return inner.isRight();
	}

	public Option<B> breakValue() {
		return inner.left();
	}

	public Option<C> continueValue() {
		return inner.right();
	}

	public <T> ControlFlow<T, C> mapBreak(Function<? super B, ? extends T> function) {
		return new ControlFlow<>(inner.mapLeft(function));
	}
}
