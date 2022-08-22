package leo.rustjava.iterator;

import leo.rustjava.Pair;
import leo.rustjava.iterator.sources.Empty;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;
import static leo.rustjava.iterator.Iterators.*;
import static org.junit.jupiter.api.Assertions.*;

class IteratorTest {
	@Test
	void filter() {
		iterEquals(
				Iterators.of(0, 6, 12, 18, 24),
				range(0).filter(i -> i % 2 == 0).filter(i -> i % 3 == 0).take(5)
		);
	}

	<T> void iterEquals(Iterator<T> i1, Iterator<T> i2) {
		assertEquals(
				i1.toList(),
				i2.toList()
		);
	}

	@Test
	void chain() {
		iterEquals(
				of(20, 30, 40, 10, 9, 8, 50),
				of(20, 30, 40).chain(of(10, 9, 8)).chain(once(50))
		);
	}

	@Test
	void nth() {
		assertEquals(
				Some(2),
				Iterators.of(1, 2, 3).nth(1)
		);
		Iterator<String> iter = of("one", "two", "three");
		assertEquals(iter.nth(10), None);
	}

	@Test
	void stepBy() {
		iterEquals(
				of(0, 10, 20, 30, 40, 50),
				range(0).stepBy(10).take(6)
		);

		iterEquals(
				of(0, 2, 4),
				rangeInclusive(0, 5).stepBy(2)
		);
	}

	@Test
	void zip() {
		iterEquals(
				of(new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4), new Pair<>(4, 5)),
				rangeInclusive(1, 5).zip(rangeInclusive(2, 5))
		);
	}

	@Test
	void peekable() {
		var iter = of(1, 2, 3).peekable();
		assertEquals(Some(1), iter.peek());
		assertEquals(Some(1), iter.peek());
		assertEquals(Some(1), iter.peek());
		assertEquals(Some(1), iter.next());
		assertEquals(Some(2), iter.peek());
		assertEquals(Some(2), iter.next());
		assertEquals(Some(3), iter.next());
		assertEquals(None(), iter.peek());
	}

	@Test
	void intersperse() {
		iterEquals(
				of(1, 0, 2, 0, 3, 0, 4),
				range(1, 5).intersperse(0)
		);
	}

	@Test
	void enumerate() {
		iterEquals(
				of(new Pair<>(0, 5), new Pair<>(1, 4), new Pair<>(2, 2), new Pair<>(3, 3)),
				of(5, 4, 2, 3).enumerate()
		);
	}

	@Test
	void takeWhile() {
		iterEquals(
				of(1, 4, 9, 16, 25, 36, 49),
				range(1).map(i -> i * i).takeWhile(i -> i < 50)
		);
	}

	@Test
	void skipWhile() {
		iterEquals(
				of(100, 121, 144, 169, 196),
				range(0)
						.map(n -> n * n)
						.skipWhile(n -> n < 100)
						.takeWhile(n -> n < 200)
		);
	}

	@Test
	void mapWhile() {
		iterEquals(
				of(0, -1 / 4, -2 / 3, -3 / 2, -4),
				range(0).mapWhile(x -> {
					if (x == 5) return None();
					return Some(x / (x - 5));
				})
		);
	}

	@Test
	void rev() {
		iterEquals(
				of(100, 81, 64, 49, 36, 25),
				rangeInclusive(5, 10).rev().map(x -> x * x)
		);
	}

	@Test
	void position() {
		var iter = of(1, 2, 3, 200, 5, 6);
		assertEquals(Some(3), iter.position(x -> x > 100));

		var iter2 = of(1, 2, 3, 200, 5, 6);
		assertEquals(None(), iter2.position(x -> x > 1000));
	}

	@Test
	void findBy() {
		assertEquals(Some(2), range(-200, 200).stepBy(2).find(e -> e > 0));
		var iter = of(20, 30, 40);
		assertEquals(Some(20), iter.find((ignored) -> true));
		assertEquals(Some(30), iter.find((ignored) -> true));
		assertEquals(Some(40), iter.find((ignored) -> true));
		assertEquals(None(), iter.find((ignored) -> true));
	}

	@Test
	void equals() {
		iterEquals(
				of(1, 2, 3),
				of(1, 2, 3)
		);
	}

	@Test
	void cycle() {
		iterEquals(
				of(1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8, 1, 2, 4, 8),
				rangeInclusive(0, 3)
						.map(n -> 1 << n)
						.takeWhile(n -> n < 10)
						.cycle()
						.take(16)
		);
		iterEquals(empty(), empty().cycle());
	}

	@Test
	void groupBy() {
		iterEquals(
				of(
						new Pair<>(true, List.of(1, 2, 4)),
						new Pair<>(false, List.of(-4, -3, -1)),
						new Pair<>(true, List.of(400)),
						new Pair<>(false, List.of(-20, -30))
				),
				of(1, 2, 4, -4, -3, -1, 400, -20, -30).groupBy(i -> i > 0)
		);
		iterEquals(
				of(
						new Pair<>(0, List.of(0, 12)),
						new Pair<>(2, List.of(8, 8)),
						new Pair<>(1, List.of(13)),
						new Pair<>(2, List.of(20, 8, 8)),
						new Pair<>(0, List.of(12)),
						new Pair<>(1, List.of(10)),
						new Pair<>(2, List.of(5, 2)),
						new Pair<>(0, List.of(18, 0, 15))
				),
				of(0, 12, 8, 8, 13, 20, 8, 8, 12, 10, 5, 2, 18, 0, 15).groupBy(i -> i % 3)
		);
	}

	@Test
	void pairs() {
		iterEquals(
				of(
						new Pair<>(20, 30),
						new Pair<>(40, 20),
						new Pair<>(30, 40),
						new Pair<>(20, 30),
						new Pair<>(40, 20),
						new Pair<>(30, 40)
				),
				range(2, 5).map(n -> n * 10).cycle().pairs().take(6)
		);
	}

	@Test
	void chunks() {
		iterEquals(
				of(
						List.of(1, 2, 4, 8),
						List.of(16, 32, 64, 128),
						List.of(256, 512, 1024)
				),
				successors(1, n -> Some(n * 2)).takeWhile(n -> n < 2000).chunks(4).map(Iterator::toList)
		);

		iterEquals(
				of(
						of(1, 2, 4, 8),
						of(16, 32, 64, 128)
				).map(Iterator::toList),
				successors(1, n -> Some(n * 2)).takeWhile(n -> n < 2000).chunksExact(4).map(Iterator::toList)
		);
	}

	@Test
	void cartesianProduct() {
		iterEquals(
				of(
						new Pair<>(0, 'a'),
						new Pair<>(0, 'b'),
						new Pair<>(0, 'c'),
						new Pair<>(5, 'a'),
						new Pair<>(5, 'b'),
						new Pair<>(5, 'c'),
						new Pair<>(10, 'a'),
						new Pair<>(10, 'b'),
						new Pair<>(10, 'c')
				),
				range(0, 3).map(n -> n * 5).cartesianProduct(chars("abc"))
		);
	}

	@Test
	void dedup() {
		iterEquals(
				of(1.0, 2.0, 3.0, 2.0),
				of(1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 2.0).dedup()
		);
	}

	@Test
	void dedupBy() {
		iterEquals(
				of(
						new Pair<>(0, 1.0),
						new Pair<>(0, 2.0),
						new Pair<>(0, 3.0),
						new Pair<>(1, 2.0)
				),
				of(
						new Pair<>(0, 1.0),
						new Pair<>(1, 1.0),
						new Pair<>(0, 2.0),
						new Pair<>(0, 3.0),
						new Pair<>(1, 3.0),
						new Pair<>(1, 2.0),
						new Pair<>(2, 2.0)
				).dedupBy((p1, p2) -> p1.right().equals(p2.right()))
		);
	}

	@Test
	void duplicates() {
		iterEquals(
				of(20, 10),
				of(10, 20, 30, 20, 40, 10, 50).duplicates()
		);
	}

	@Test
	void duplicatesBy() {
		iterEquals(
				of("aa", "c"),
				of("a", "bb", "aa", "c", "ccc").duplicatesBy(String::length)
		);
	}

	@Test
	void unique() {
		iterEquals(
				rangeInclusive(10, 50).stepBy(10),
				of(10, 20, 30, 20, 40, 10, 50).unique()
		);
	}

	@Test
	void powerset() {
		iterEquals(
				of(
						List.of(),
						List.of(1),
						List.of(2),
						List.of(1, 2),
						List.of(3),
						List.of(1, 3),
						List.of(2, 3),
						List.of(1, 2, 3),
						List.of(4),
						List.of(1, 4),
						List.of(2, 4),
						List.of(1, 2, 4),
						List.of(3, 4),
						List.of(1, 3, 4),
						List.of(2, 3, 4),
						List.of(1, 2, 3, 4)
				),
				of(1, 2, 3, 4).powerset()
		);
	}

	@Test
	void padUsing() {
		iterEquals(
				of(0, 1, 2, 3, 4, 10, 12, 14, 16, 18),
				range(0, 5).padUsing(10, i -> 2 * i)
		);
		iterEquals(
				of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
				range(0, 10).padUsing(10, i -> 2 * i)
		);
	}

	@Test
	void positions() {
		var data = List.of(1, 2, 3, 3, 4, 6, 7, 9);

		iterEquals(
				of(1, 4, 5),
				fromList(data).positions(v -> v % 2 == 0)
		);
		iterEquals(
				of(0, 2, 3, 6, 7),
				fromList(data).positions(v -> v % 2 == 1)
		);
	}

	@Test
	void findPosition() {
		assertEquals(
				Some(new Pair<>(17, 'H')),
				chars("der name beginnt Hier").findPosition(Character::isUpperCase)
		);
	}

	@Test
	void findOrLast() {
		var numbers = range(1, 5).toList();
		assertEquals(Some(4), fromList(numbers).findOrLast(x -> x > 5));
		assertEquals(Some(3), fromList(numbers).findOrLast(x -> x > 2));
		assertEquals(None(), new Empty<Integer>().findOrLast(x -> x > 5));
	}

	@Test
	void findOrFirst() {
		var numbers = range(1, 5).toList();
		assertEquals(Some(1), fromList(numbers).findOrFirst(x -> x > 5));
		assertEquals(Some(3), fromList(numbers).findOrFirst(x -> x > 2));
		assertEquals(None(), new Empty<Integer>().findOrFirst(x -> x > 5));
	}

	@Test
	void contains() {
		var numbers = range(1, 5);
		assertTrue(numbers.contains(2));
		assertEquals(Some(3), numbers.next());
		assertFalse(numbers.contains(5));
		assertEquals(None(), numbers.next());
	}

	@Test
	void join() {
		assertEquals("a, b, c", of('a', 'b', 'c').join(", "));
		assertEquals("1:3:5:7:9", range(1, 10).stepBy(2).join(":"));
	}

	@Test
	void sorted() {
		iterEquals(
				of(1, 2, 4, 5, 9),
				of(2, 5, 4, 9, 1).sorted()
		);
	}

	@Test
	void sortedBy() {
		iterEquals(
				of(9, 5, 4, 2, 1, 1, 0),
				of(1, 9, 2, 4, 1, 0, 5).sortedBy(Comparator.reverseOrder())
		);
	}

	@Test
	void sortedByKey() {
		iterEquals(
				of("a", "cc", "ddd", "bbb", "rust is better", "praise the borrow checker"),
				of("rust is better", "cc", "ddd", "praise the borrow checker", "bbb", "a").sortedByKey(String::length)
		);
	}

	@Test
	void kSmallest() {
		var numbers = of(6, 9, 1, 14, 0, 4, 8, 7, 11, 2, 10, 3, 13, 12, 5);

		iterEquals(
				range(0, 5),
				numbers.kSmallest(5)
		);
	}

	@Test
	void minMax() {
		assertEquals(Some(new Pair<>(1, 1)), of(1, 1, 1, 1, 1).minMax());
		assertEquals(Some(new Pair<>(1, 5)), range(1, 6).minMax());
		assertEquals(None(), new Empty<Integer>().minMax());
	}

	@Test
	void positionMax() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMax());
		List<Integer> b = List.of(-3, 0, 1, 5, -10);
		assertEquals(Some(3), fromList(b).positionMax());
		List<Integer> c = List.of(1, 1, -1, -1);
		assertEquals(Some(1), fromList(c).positionMax());
	}

	@Test
	void positionMaxByKey() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMaxByKey(Math::abs));
		assertEquals(Some(4), of(-3, 0, 1, 5, -10).positionMaxByKey(Math::abs));
		assertEquals(Some(3), of(1, 1, -1, -1).positionMaxByKey(Math::abs));
	}

	@Test
	void positionMin() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMin());
		assertEquals(Some(4), of(-3, 0, 1, 5, -10).positionMin());
		assertEquals(Some(2), of(1, 1, -1, -1).positionMin());
	}

	@Test
	void positionMinByKey() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMinByKey(Math::abs));
		assertEquals(Some(1), of(-3, 0, 1, 5, -10).positionMinByKey(Math::abs));
		assertEquals(Some(0), of(1, 1, -1, -1).positionMinByKey(Math::abs));
	}

	@Test
	void positionMinMax() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMinMax());
		assertEquals(Some(new Pair<>(0, 0)), of(10).positionMinMax());
		assertEquals(
				Some(new Pair<>(4, 3)),
				of(-3, 0, 1, 5, -10).positionMinMax()
		);
		assertEquals(
				Some(new Pair<>(2, 1)),
				of(1, 1, -1, -1).positionMinMax()
		);
	}

	@Test
	void positionMinMaxByKey() {
		List<Integer> a = new ArrayList<>();
		assertEquals(None(), fromList(a).positionMinMaxByKey(Math::abs));
		assertEquals(Some(new Pair<>(0, 0)), of(10).positionMinMaxByKey(Math::abs));
		assertEquals(
				Some(new Pair<>(1, 4)),
				of(-3, 0, 1, 5, -10).positionMinMaxByKey(Math::abs)
		);
		assertEquals(
				Some(new Pair<>(0, 3)),
				of(1, 1, -1, -1).positionMinMaxByKey(Math::abs)
		);
	}

	@Test
	void counts() {
		var counts = of(1, 1, 1, 3, 3, 5, 1).counts();
		assertEquals(4, counts.get(1));
		assertEquals(2, counts.get(3));
		assertEquals(1, counts.get(5));
		assertNull(counts.get(2));
		assertNull(counts.get(4));
	}

	@Test
	void countsBy() {
		var characters = of(
				new Pair<>("Amy", "Pond"),
				new Pair<>("James", "Bond"),
				new Pair<>("James", "Sullivan"),
				new Pair<>("Amy", "Santiago"),
				new Pair<>("James", "Kirk"),
				new Pair<>("Amy", "Wong"),
				new Pair<>("James", "Norington")
		);
		var firstNameFrequency = characters.countsBy(Pair::left);
		assertEquals(3, firstNameFrequency.get("Amy"));
		assertEquals(4, firstNameFrequency.get("James"));
		assertFalse(firstNameFrequency.containsKey("Asha"));
	}

	@Test
	void interleave() {
		iterEquals(
				of(1, -1, 2, -2, 3, 4, 5, 6),
				range(1, 7).interleave(of(-1, -2))
		);
	}

	@Test
	void interleaveShortest() {
		iterEquals(
				of(1, -1, 2, -2, 3),
				range(1, 7).interleaveShortest(of(-1, -2))
		);
		iterEquals(
				of(1, -1, 2, -2),
				of(1, 2).interleaveShortest(of(-1, -2, -3, -4))
		);
	}

	@Test
	void pairWindows() {
		iterEquals(
				of(
						new Pair<>(1, 2),
						new Pair<>(2, 3),
						new Pair<>(3, 4),
						new Pair<>(4, 5)
				),
				range(1, 6).pairWindows()
		);
	}

	@Test
	void nthBack() {
		assertEquals(Some(2), rangeInclusive(-2_000_000_000, 4).nthBack(3));
	}

	@Test
	void i_repeatWith() {
		iterEquals(
				of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
				repeatWith(() -> 1).take(10)
		);
	}

	@Test
	void i_onceWith() {
		var i = 300;
		iterEquals(
				of(345),
				onceWith(() -> i + 40 + 5)
		);
	}
}