package leo.rustjava.iterator.adapters;

import leo.rustjava.ControlFlow;
import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.IntoIter;
import leo.rustjava.iterator.sources.ListIter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.*;

public class Adapt<T, U> implements Iterator<U> {
	private final Iterator<U> iter;

	public Adapt(Iterator<T> iter, Function<? super Iterator<T>, ? extends Iterator<U>> function) {
		this.iter = function.apply(iter);
	}

	@Override
	public Option<U> next() {
		return iter.next();
	}

	@Override
	public SizeHint sizeHint() {
		return iter.sizeHint();
	}

	@Override
	public int count() {
		return iter.count();
	}

	@Override
	public <B> B fold(B seed, BiFunction<? super B, ? super U, ? extends B> f) {
		return iter.fold(seed, f);
	}

	@Override
	public <B> B tryFold(B seed, BiFunction<? super B, ? super U, ControlFlow<B, B>> f) {
		return iter.tryFold(seed, f);
	}

	@Override
	public Option<Integer> position(Predicate<? super U> p) {
		return iter.position(p);
	}

	@Override
	public Iterator<Pair<Integer, U>> enumerate() {
		return iter.enumerate();
	}

	@Override
	public Option<U> last() {
		return iter.last();
	}

	@Override
	public Option<U> nth(int n) {
		return iter.nth(n);
	}

	@Override
	public boolean advanceBy(int n) {
		return iter.advanceBy(n);
	}

	@Override
	public Iterator<U> stepBy(int step) {
		return iter.stepBy(step);
	}

	@Override
	public Iterator<U> chain(IntoIter<U> other) {
		return iter.chain(other);
	}

	@Override
	public <U1> Iterator<Pair<U, U1>> zip(IntoIter<U1> other) {
		return iter.zip(other);
	}

	@Override
	public Iterator<U> intersperse(U u) {
		return iter.intersperse(u);
	}

	@Override
	public Peekable<U> peekable() {
		return iter.peekable();
	}

	@Override
	public Iterator<U> cycle() {
		return iter.cycle();
	}

	@Override
	public Iterator<U> scan1(BiFunction<? super U, ? super U, ? extends U> f) {
		return iter.scan1(f);
	}

	@Override
	public <U1> Iterator<U1> scan(U1 seed, BiFunction<? super U1, ? super U, ? extends U1> f) {
		return iter.scan(seed, f);
	}

	@Override
	public Iterator<U> filter(Predicate<? super U> p) {
		return iter.filter(p);
	}

	@Override
	public <U1> Iterator<U1> filterMap(Function<? super U, ? extends Option<U1>> f) {
		return iter.filterMap(f);
	}

	@Override
	public <U1> Iterator<U1> flatMap(Function<? super U, ? extends IntoIter<U1>> f) {
		return iter.flatMap(f);
	}

	@Override
	public Iterator<U> skipWhile(Predicate<? super U> p) {
		return iter.skipWhile(p);
	}

	@Override
	public Iterator<U> takeWhile(Predicate<? super U> p) {
		return iter.takeWhile(p);
	}

	@Override
	public Iterator<U> mapWhile(Function<? super U, Option<U>> f) {
		return iter.mapWhile(f);
	}

	@Override
	public Iterator<U> inspect(Consumer<? super U> c) {
		return iter.inspect(c);
	}

	@Override
	public Iterator<U> skip(int n) {
		return iter.skip(n);
	}

	@Override
	public Iterator<U> fuse() {
		return iter.fuse();
	}

	@Override
	public Iterator<U> iter() {
		return iter.iter();
	}

	@Override
	public Iterator<U> interleave(IntoIter<U> other) {
		return iter.interleave(other);
	}

	@Override
	public Iterator<U> interleaveShortest(IntoIter<U> other) {
		return iter.interleaveShortest(other);
	}

	@Override
	public <K> Iterator<Pair<K, List<U>>> groupBy(Function<? super U, ? extends K> key) {
		return iter.groupBy(key);
	}

	@Override
	public Iterator<Iterator<U>> chunks(int chunkSize) {
		return iter.chunks(chunkSize);
	}

	@Override
	public Iterator<ExactSizeIterator<U>> chunksExact(int chunkSize) {
		return iter.chunksExact(chunkSize);
	}

	@Override
	public Iterator<Pair<U, U>> pairWindows() {
		return iter.pairWindows();
	}

	@Override
	public Iterator<Pair<U, U>> pairs() {
		return iter.pairs();
	}

	@Override
	public <U1> Iterator<Pair<U, U1>> cartesianProduct(IntoIter<U1> other) {
		return iter.cartesianProduct(other);
	}

	@Override
	public <U1> Iterator<U1> coalesce(BiFunction<U, U, U1> function) {
		return iter.coalesce(function);
	}

	@Override
	public Iterator<U> dedup() {
		return iter.dedup();
	}

	@Override
	public Iterator<U> dedupBy(BiPredicate<? super U, ? super U> cmp) {
		return iter.dedupBy(cmp);
	}

	@Override
	public Iterator<Pair<Integer, U>> dedupWithCount() {
		return iter.dedupWithCount();
	}

	@Override
	public Iterator<Pair<Integer, U>> dedupByWithCount(BiPredicate<? super U, ? super U> cmp) {
		return iter.dedupByWithCount(cmp);
	}

	@Override
	public Iterator<U> duplicates() {
		return iter.duplicates();
	}

	@Override
	public <U1> Iterator<U> duplicatesBy(Function<? super U, U1> id) {
		return iter.duplicatesBy(id);
	}

	@Override
	public Iterator<U> unique() {
		return iter.unique();
	}

	@Override
	public <U1> Iterator<U> uniqueBy(Function<? super U, U1> id) {
		return iter.uniqueBy(id);
	}

	@Override
	public Iterator<U> combinations(int k) {
		return iter.combinations(k);
	}

	@Override
	public Iterator<U> permutations(int k) {
		return iter.permutations(k);
	}

	@Override
	public Iterator<List<U>> powerset() {
		return iter.powerset();
	}

	@Override
	public Iterator<U> padUsing(int min, Function<? super Integer, ? extends U> function) {
		return iter.padUsing(min, function);
	}

	@Override
	public Iterator<Integer> positions(Predicate<? super U> predicate) {
		return iter.positions(predicate);
	}

	@Override
	public Option<Pair<Integer, U>> findPosition(Predicate<? super U> predicate) {
		return iter.findPosition(predicate);
	}

	@Override
	public Option<U> find(Predicate<? super U> p) {
		return iter.find(p);
	}

	@Override
	public Option<U> findOrFirst(Predicate<? super U> predicate) {
		return iter.findOrFirst(predicate);
	}

	@Override
	public Option<U> findOrLast(Predicate<? super U> predicate) {
		return iter.findOrLast(predicate);
	}

	@Override
	public boolean contains(U query) {
		return iter.contains(query);
	}

	@Override
	public boolean any(Predicate<? super U> p) {
		return iter.any(p);
	}

	@Override
	public boolean allEqual() {
		return iter.allEqual();
	}

	@Override
	public boolean all(Predicate<? super U> p) {
		return iter.all(p);
	}

	@Override
	public boolean allUnique() {
		return iter.allUnique();
	}

	@Override
	public String join(String sep) {
		return iter.join(sep);
	}

	@Override
	public void forEach(Consumer<? super U> f) {
		iter.forEach(f);
	}

	@Override
	public ListIter<U> sorted() {
		return iter.sorted();
	}

	@Override
	public ListIter<U> sortedBy(Comparator<? super U> cmp) {
		return iter.sortedBy(cmp);
	}

	@Override
	public List<U> toList() {
		return iter.toList();
	}

	@Override
	public <K extends Comparable<K>> ListIter<U> sortedByKey(Function<? super U, K> key) {
		return iter.sortedByKey(key);
	}

	@Override
	public <U1> Iterator<U1> map(Function<? super U, ? extends U1> f) {
		return iter.map(f);
	}

	@Override
	public Iterator<U> kSmallest(int k) {
		return iter.kSmallest(k);
	}

	@Override
	public Iterator<U> take(int n) {
		return iter.take(n);
	}

	@Override
	public Option<Pair<U, U>> minMax() {
		return iter.minMax();
	}

	@Override
	public Option<Integer> positionMax() {
		return iter.positionMax();
	}

	@Override
	public <K> Option<U> maxByKey(Function<? super U, K> key) {
		return iter.maxByKey(key);
	}

	@Override
	public Option<U> reduce(BiFunction<? super U, ? super U, ? extends U> f) {
		return iter.reduce(f);
	}

	@Override
	public <K> Option<Integer> positionMaxByKey(Function<? super U, K> key) {
		return iter.positionMaxByKey(key);
	}

	@Override
	public Option<Integer> positionMaxBy(Comparator<? super U> cmp) {
		return iter.positionMaxBy(cmp);
	}

	@Override
	public Option<U> maxBy(Comparator<? super U> c) {
		return iter.maxBy(c);
	}

	@Override
	public Option<Integer> positionMin() {
		return iter.positionMin();
	}

	@Override
	public <K> Option<U> minByKey(Function<? super U, K> key) {
		return iter.minByKey(key);
	}

	@Override
	public <K> Option<Integer> positionMinByKey(Function<? super U, K> key) {
		return iter.positionMinByKey(key);
	}

	@Override
	public Option<Integer> positionMinBy(Comparator<? super U> cmp) {
		return iter.positionMinBy(cmp);
	}

	@Override
	public Option<U> minBy(Comparator<? super U> c) {
		return iter.minBy(c);
	}

	@Override
	public Option<Pair<Integer, Integer>> positionMinMax() {
		return iter.positionMinMax();
	}

	@Override
	public <K> Option<Pair<U, U>> minMaxByKey(Function<? super U, K> key) {
		return iter.minMaxByKey(key);
	}

	@Override
	public <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<? super U, K> key) {
		return iter.positionMinMaxByKey(key);
	}

	@Override
	public Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<? super U> cmp) {
		return iter.positionMinMaxBy(cmp);
	}

	@Override
	public Option<Pair<U, U>> minMaxBy(Comparator<? super U> cmp) {
		return iter.minMaxBy(cmp);
	}

	@Override
	public HashMap<U, Integer> counts() {
		return iter.counts();
	}

	@Override
	public <K> HashMap<K, Integer> countsBy(Function<? super U, ? extends K> key) {
		return iter.countsBy(key);
	}

	@Override
	public <U1, V> Iterator<V> zipWith(IntoIter<? extends U1> other, BiFunction<? super U, U1, ? extends V> function) {
		return iter.zipWith(other, function);
	}

	@Override
	public boolean equals(IntoIter<U> iterable) {
		return iter.equals(iterable);
	}
}
