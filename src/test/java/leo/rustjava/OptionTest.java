package leo.rustjava;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("EmptyMethod")
class OptionTest {
	Option<Integer> noneInt = Option.None();
	Option<Integer> someEvenInt = Option.Some(420);
	Option<Integer> someOddInt = Option.Some(69);

	Predicate<Integer> isEven = x -> x % 2 == 0;

	Function<Integer, Integer> increment = x -> x + 1;
	Function<Integer, Integer> decrement = x -> x - 1;

	@Test
	void some() {
		Option<Integer> some = Option.Some(2);
		assert some.isSome();
	}

	@Test
	void none() {
		Option<Integer> none = Option.None();
		assert none.isNone();
	}

	@Test
	void unwrap() {
		assertEquals(someEvenInt.unwrap(), 420);
		assertEquals(someOddInt.unwrap(), 69);
	}

	@Test
	void unwrapOr() {
		assertEquals(someOddInt.unwrapOr(0), 69);
		assertEquals(noneInt.unwrapOr(0), 0);
	}

	@Test
	void unwrapOrElse() {
		assertEquals(someOddInt.unwrapOrElse(() -> 200), 69);
		assertEquals(noneInt.unwrapOrElse(() -> 200), 200);
	}

	@Test
	void isSome() {
		assertTrue(someEvenInt.isSome());
		assertFalse(noneInt.isSome());
	}

	@Test
	void isSomeAnd() {
		assertFalse(noneInt.isSomeAnd(isEven));
		assertFalse(someOddInt.isSomeAnd(isEven));
		assertTrue(someEvenInt.isSomeAnd(isEven));
	}

	@Test
	void isNone() {
		assertTrue(noneInt.isNone());
		assertFalse(someOddInt.isNone());
		assertFalse(someEvenInt.isNone());
	}

	@Test
	void map() {
		assertTrue(noneInt.map(increment).isNone());
		assertEquals(someOddInt.map(increment).unwrap(), 70);
		assertEquals(someEvenInt.map(decrement).unwrap(), 419);
	}

	@Test
	void mapOr() {
		assertEquals(noneInt.mapOr(false, n -> isEven.test(n)), false);
		assertEquals(someEvenInt.mapOr(false, n -> isEven.test(n)), true);
		assertEquals(someOddInt.mapOr(false, n -> isEven.test(n)), false);
	}

	@Test
	void mapOrElse() {
	}

	@Test
	void and() {
	}

	@Test
	void testAnd() {
	}

	@Test
	void filter() {
	}

	@Test
	void or() {
	}

	@Test
	void testOr() {
	}

	@Test
	void xor() {
	}

	@Test
	void insert() {
	}

	@Test
	void getOrInsert() {
	}

	@Test
	void testGetOrInsert() {
	}

	@Test
	void take() {
	}

	@Test
	void replace() {
	}

	@Test
	void contains() {
	}
}