package leo.rustjava.iterator.interfaces;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.sources.ListIter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface EndlessIterator<T> extends Iterator<T>, FusedIterator<T> {
	@Override
	default SizeHint sizeHint() {
		return SizeHint.ENDLESS;
	}

	private UnsupportedOperationException unsupported(String method) {
		return new UnsupportedOperationException("This iterator is infinite, yet `Iterator::" + method + "` exhausts the iterator");
	}

	@Override
	default List<T> toList() {
		throw unsupported("toList");
	}

	@Override
	default int count() {
		throw unsupported("count");
	}

	@Override
	default <B> B fold(B seed, BiFunction<? super B, ? super T, ? extends B> f) {
		throw unsupported("fold");
	}

	@Override
	default <B> B tryFold(B seed, BiFunction<? super B, ? super T, ? extends Option<B>> f) {
		throw unsupported("tryFold");
	}

	@Override
	default Option<T> last() {
		throw unsupported("last");
	}

	@Override
	default boolean allEqual() {
		throw unsupported("allEqual");
	}

	@Override
	default boolean allUnique() {
		throw unsupported("allUnique");
	}

	@Override
	default String join(String sep) {
		throw unsupported("join");
	}

	@Override
	default ListIter<T> sorted() {
		throw unsupported("sorted");
	}

	@Override
	default ListIter<T> sortedBy(Comparator<? super T> cmp) {
		throw unsupported("sortedBy");
	}

	@Override
	default <K extends Comparable<K>> ListIter<T> sortedByKey(Function<? super T, K> key) {
		throw unsupported("sortedByKey");
	}

	@Override
	default Iterator<T> kSmallest(int k) {
		throw unsupported("kSmallest");
	}

	@Override
	default <K> Option<Integer> positionMaxByKey(Function<? super T, K> key) {
		throw unsupported("positionMaxByKey");
	}

	@Override
	default Option<Integer> positionMaxBy(Comparator<? super T> cmp) {
		throw unsupported("positionMaxBy");
	}

	@Override
	default Option<T> maxBy(Comparator<? super T> c) {
		throw unsupported("maxBy");
	}

	@Override
	default Option<Integer> positionMin() {
		throw unsupported("positionMin");
	}

	@Override
	default <K> Option<T> minByKey(Function<? super T, K> key) {
		throw unsupported("minByKey");
	}

	@Override
	default <K> Option<Integer> positionMinByKey(Function<? super T, K> key) {
		throw unsupported("positionMinByKey");
	}

	@Override
	default Option<Integer> positionMinBy(Comparator<? super T> cmp) {
		throw unsupported("positionMinBy");
	}

	@Override
	default Option<T> minBy(Comparator<? super T> c) {
		throw unsupported("minBy");
	}

	@Override
	default Option<Pair<Integer, Integer>> positionMinMax() {
		throw unsupported("positionMinMax");
	}

	@Override
	default <K> Option<Pair<T, T>> minMaxByKey(Function<? super T, K> key) {
		throw unsupported("minMaxByKey");
	}

	@Override
	default <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<? super T, K> key) {
		throw unsupported("positionMinMaxByKey");
	}

	@Override
	default Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<? super T> cmp) {
		throw unsupported("positionMinMaxBy");
	}

	@Override
	default Option<Pair<T, T>> minMaxBy(Comparator<? super T> cmp) {
		throw unsupported("minMaxBy");
	}

	@Override
	default HashMap<T, Integer> counts() {
		throw unsupported("counts");
	}

	@Override
	default <K> HashMap<K, Integer> countsBy(Function<? super T, ? extends K> key) {
		throw unsupported("countsBy");
	}

	@Override
	default boolean equals(IntoIter<T> iterable) {
		throw unsupported("equals");
	}
}
