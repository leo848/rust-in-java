package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.*;

import static leo.rustjava.Option.None;

public class Empty<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    public Empty() {
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public <B> B fold(B seed, BiFunction<? super B, ? super T, ? extends B> f) {
        return seed;
    }

    @Override
    public <B> B tryFold(B seed, BiFunction<? super B, ? super T, ? extends Option<B>> f) {
        return seed;
    }

    @Override
    public Option<Integer> position(Predicate<? super T> p) {
        return None();
    }

    @Override
    public Iterator<Pair<Integer, T>> enumerate() {
        return new Empty<>();
    }

    @Override
    public Option<T> last() {
        return None();
    }

    @Override
    public Option<T> nth(int n) {
        return None();
    }

    @Override
    public boolean advanceBy(int n) {
        return false;
    }

    @Override
    public Iterator<T> stepBy(int step) {
        return this;
    }

    @Override
    public Iterator<T> chain(IntoIter<T> other) {
        return other.iter();
    }

    @Override
    public <U> Iterator<Pair<T, U>> zip(Iterator<U> other) {
        return new Empty<>();
    }

    @Override
    public Iterator<T> intersperse(T t) {
        return this;
    }

    @Override
    public Iterator<T> cycle() {
        return this;
    }

    @Override
    public Iterator<T> scan1(BiFunction<? super T, ? super T, ? extends T> f) {
        throw new UnsupportedOperationException("no first element");
    }

    @Override
    public <U> Iterator<U> scan(U seed, BiFunction<? super U, ? super T, ? extends U> f) {
        return new Once<>(seed);
    }

    @Override
    public Iterator<T> filter(Predicate<T> p) {
        return this;
    }

    @Override
    public <U> Iterator<U> filterMap(Function<? super T, ? extends Option<U>> f) {
        return new Empty<>();
    }

    @Override
    public <U> Iterator<U> flatMap(Function<? super T, IntoIter<U>> f) {
        return new Empty<>();
    }

    @Override
    public Iterator<T> skipWhile(Predicate<? super T> p) {
        return this;
    }

    @Override
    public Iterator<T> takeWhile(Predicate<? super T> p) {
        return this;
    }

    @Override
    public Iterator<T> mapWhile(Function<? super T, Option<T>> f) {
        return this;
    }

    @Override
    public Iterator<T> inspect(Consumer<? super T> c) {
        return this;
    }

    @Override
    public Iterator<T> skip(int n) {
        return this;
    }

    @Override
    public Iterator<T> interleave(IntoIter<T> other) {
        return other.iter();
    }

    @Override
    public Iterator<T> interleaveShortest(IntoIter<T> other) {
        return this;
    }

    @Override
    public <K> Iterator<Pair<K, List<T>>> groupBy(Function<? super T, K> key) {
        return new Empty<>();
    }

    @Override
    public Iterator<Iterator<T>> chunks(int chunkSize) {
        return new Empty<>();
    }

    @Override
    public Iterator<ExactSizeIterator<T>> chunksExact(int chunkSize) {
        return new Empty<>();
    }

    @Override
    public Iterator<Pair<T, T>> pairWindows() {
        return new Empty<>();
    }

    @Override
    public Iterator<Pair<T, T>> pairs() {
        return new Empty<>();
    }

    @Override
    public <U> Iterator<Pair<T, U>> cartesianProduct(IntoIter<U> other) {
        return new Empty<>();
    }

    @Override
    public <U> Iterator<U> coalesce(BiFunction<T, T, U> function) {
        return new Empty<>();
    }

    @Override
    public Iterator<T> dedup() {
        return this;
    }

    @Override
    public Iterator<T> dedupBy(BiPredicate<? super T, ? super T> cmp) {
        return this;
    }

    @Override
    public Iterator<Pair<Integer, T>> dedupWithCount() {
        return new Empty<>();
    }

    @Override
    public Iterator<Pair<Integer, T>> dedupByWithCount(BiPredicate<? super T, ? super T> cmp) {
        return new Empty<>();
    }

    @Override
    public Iterator<T> duplicates() {
        return this;
    }

    @Override
    public <U> Iterator<T> duplicatesBy(Function<? super T, U> id) {
        return this;
    }

    @Override
    public Iterator<T> unique() {
        return this;
    }

    @Override
    public <U> Iterator<T> uniqueBy(Function<? super T, U> id) {
        return this;
    }

    @Override
    public Iterator<T> combinations(int k) {
        return this;
    }

    @Override
    public Iterator<T> permutations(int k) {
        return this;
    }

    @Override
    public Iterator<List<T>> powerset() {
        return new Empty<>();
    }

    @Override
    public Iterator<Integer> positions(Predicate<? super T> predicate) {
        return new Empty<>();
    }

    @Override
    public Option<Pair<Integer, T>> findPosition(Predicate<? super T> predicate) {
        return None();
    }

    @Override
    public Option<T> find(Predicate<? super T> p) {
        return None();
    }

    @Override
    public Option<T> findOrFirst(Predicate<? super T> predicate) {
        return None();
    }

    @Override
    public Option<T> findOrLast(Predicate<? super T> predicate) {
        return None();
    }

    @Override
    public boolean contains(T query) {
        return false;
    }

    @Override
    public boolean any(Predicate<? super T> p) {
        return false;
    }

    @Override
    public boolean allEqual() {
        return true;
    }

    @Override
    public boolean all(Predicate<? super T> p) {
        return true;
    }

    @Override
    public boolean allUnique() {
        return true;
    }

    @Override
    public String join(String sep) {
        return "";
    }

    @Override
    public void forEach(Consumer<? super T> f) {
    }

    @Override
    public ListIter<T> sorted() {
        return new ListIter<>(List.of());
    }

    @Override
    public ListIter<T> sortedBy(Comparator<? super T> cmp) {
        return new ListIter<>(List.of());
    }

    @Override
    public List<T> toList() {
        return List.of();
    }

    @Override
    public <K extends Comparable<K>> ListIter<T> sortedByKey(Function<? super T, K> key) {
        return new ListIter<>(List.of());
    }

    @Override
    public <U> Iterator<U> map(Function<? super T, ? extends U> f) {
        return new Empty<>();
    }

    @Override
    public Iterator<T> kSmallest(int k) {
        return this;
    }

    @Override
    public Iterator<T> take(int n) {
        return this;
    }

    @Override
    public Option<Pair<T, T>> minMax() {
        return None();
    }

    @Override
    public Option<Integer> positionMax() {
        return None();
    }

    @Override
    public <K> Option<T> maxByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> f) {
        return None();
    }

    @Override
    public <K> Option<Integer> positionMaxByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public Option<Integer> positionMaxBy(Comparator<? super T> cmp) {
        return None();
    }

    @Override
    public Option<T> maxBy(Comparator<? super T> c) {
        return None();
    }

    @Override
    public Option<Integer> positionMin() {
        return None();
    }

    @Override
    public <K> Option<T> minByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public <K> Option<Integer> positionMinByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public Option<Integer> positionMinBy(Comparator<? super T> cmp) {
        return None();
    }

    @Override
    public Option<T> minBy(Comparator<? super T> c) {
        return None();
    }

    @Override
    public Option<Pair<Integer, Integer>> positionMinMax() {
        return None();
    }

    @Override
    public <K> Option<Pair<T, T>> minMaxByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<? super T, K> key) {
        return None();
    }

    @Override
    public Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<? super T> cmp) {
        return None();
    }

    @Override
    public Option<Pair<T, T>> minMaxBy(Comparator<? super T> cmp) {
        return None();
    }

    @Override
    public HashMap<T, Integer> counts() {
        return new HashMap<>();
    }

    @Override
    public <K> HashMap<K, Integer> countsBy(Function<? super T, ? extends K> key) {
        return new HashMap<>();
    }

    @Override
    public boolean equals(IntoIter<T> iterable) {
        return iterable.iter().next().isNone();
    }

    @Override
    public Option<T> nthBack(int n) {
        return None();
    }

    @Override
    public boolean advanceBackBy(int n) {
        return false;
    }

    @Override
    public DoubleEndedIterator<T> rev() {
        return this;
    }

    @Override
    public Option<T> next() {
        return None();
    }

    @Override
    public Option<T> nextBack() {
        return None();
    }

    @Override
    public int len() {
        return 0;
    }

    @Override
    public String toString() {
        return "Empty";
    }
}
