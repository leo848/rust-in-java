package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.adapters.Chain;
import leo.rustjava.iterator.interfaces.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.*;

import static leo.rustjava.Option.*;
import static leo.rustjava.iterator.Iterators.of;

public class Once<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    T value;
    boolean exhausted;

    public Once(T value) {
        this.value = value;
        this.exhausted = false;
    }

    @Override
    public int count() {
        return exhausted ? 0 : 1;
    }

    @Override
    public <B> B fold(B seed, BiFunction<? super B, ? super T, ? extends B> f) {
        if (exhausted) return seed;
        return f.apply(seed, next().unwrap());
    }

    @Override
    public <B> B tryFold(B seed, BiFunction<? super B, ? super T, ? extends Option<B>> f) {
        if (exhausted) return seed;
        return f.apply(seed, next().unwrap()).unwrapOr(seed);
    }

    @Override
    public Option<Integer> position(Predicate<? super T> p) {
        return exhausted ? None() : DoubleEndedIterator.super.position(p);
    }

    @Override
    public Iterator<Pair<Integer, T>> enumerate() {
        return exhausted ? new Empty<>() : new Once<>(new Pair<>(0, next().unwrap()));
    }

    @Override
    public Option<T> last() {
        return next();
    }

    @Override
    public Option<T> nth(int n) {
        var item = next();
        return n == 0 ? item : None();
    }

    @Override
    public boolean advanceBy(int n) {
        return DoubleEndedIterator.super.advanceBy(n);
    }

    @Override
    public Iterator<T> stepBy(int step) {
        return this;
    }

    @Override
    public Iterator<T> chain(IntoIter<T> other) {
        return exhausted ? other.iter() : DoubleEndedIterator.super.chain(other);
    }

    @Override
    public <U> Iterator<Pair<T, U>> zip(Iterator<U> other) {
        return exhausted ? new Empty<>() : DoubleEndedIterator.super.zip(other);
    }

    @Override
    public Iterator<T> scan1(BiFunction<? super T, ? super T, ? extends T> f) {
        return new Empty<>();
    }

    @Override
    public <U> Iterator<U> scan(U seed, BiFunction<? super U, ? super T, ? extends U> f) {
        if (exhausted) return new Once<>(seed);
        return new Once<>(f.apply(seed, next().unwrap()));
    }

    @Override
    public Iterator<T> filter(Predicate<? super T> p) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.filter(p);
    }

    @Override
    public <U> Iterator<U> filterMap(Function<? super T, ? extends Option<U>> f) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.filterMap(f);
    }

    @Override
    public <U> Iterator<U> flatMap(Function<? super T, IntoIter<U>> f) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.flatMap(f);
    }

    @Override
    public Iterator<T> skipWhile(Predicate<? super T> p) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.skipWhile(p);
    }

    @Override
    public Iterator<T> takeWhile(Predicate<? super T> p) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.takeWhile(p);
    }

    @Override
    public Iterator<T> inspect(Consumer<? super T> c) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.inspect(c);
    }

    @Override
    public Iterator<T> interleave(IntoIter<T> other) {
        if (exhausted) return other.iter();
        return new Chain<>(this, other);
    }

    @SuppressWarnings("BoundedWildcard")
    @Override
    public Iterator<T> interleaveShortest(IntoIter<T> other) {
        var iter = other.iter();
        var item = iter.next();
        if (item.isNone()) return new Empty<>();
        if (exhausted) return new Empty<>();
        return of(next().unwrap(), item.unwrap());
    }

    @Override
    public <K> Iterator<Pair<K, List<T>>> groupBy(Function<? super T, ? extends K> key) {
        if (exhausted) return new Empty<>();
        var item = next().unwrap();
        return new Once<>(new Pair<>(key.apply(item), List.of(item)));
    }

    @Override
    public Iterator<Iterator<T>> chunks(int chunkSize) {
        return new Once<>(this);
    }

    @Override
    public Iterator<ExactSizeIterator<T>> chunksExact(int chunkSize) {
        if (exhausted) return new Empty<>();
        if (chunkSize == 1) return new Once<>(this);
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
    public Iterator<T> dedup() {
        return this;
    }

    @Override
    public Iterator<T> dedupBy(BiPredicate<? super T, ? super T> cmp) {
        return this;
    }

    @Override
    public Iterator<Pair<Integer, T>> dedupWithCount() {
        if (exhausted) return new Empty<>();
        return new Once<>(new Pair<>(1, next().unwrap()));
    }

    @Override
    public Iterator<Pair<Integer, T>> dedupByWithCount(BiPredicate<? super T, ? super T> cmp) {
        if (exhausted) return new Empty<>();
        return new Once<>(new Pair<>(1, next().unwrap()));
    }

    @Override
    public Iterator<T> duplicates() {
        return new Empty<>();
    }

    @Override
    public <U> Iterator<T> duplicatesBy(Function<? super T, U> id) {
        return new Empty<>();
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
    public Iterator<List<T>> powerset() {
        if (exhausted) return new Once<>(List.of());
        return of(List.of(), List.of(next().unwrap()));
    }

    @Override
    public Iterator<Integer> positions(Predicate<? super T> predicate) {
        if (exhausted) return new Empty<>();
        return DoubleEndedIterator.super.positions(predicate);
    }

    @Override
    public Option<Pair<Integer, T>> findPosition(Predicate<? super T> predicate) {
        var item = next();
        if (item.isSomeAnd(predicate)) return Some(new Pair<>(0, item.unwrap()));
        return None();
    }

    @Override
    public Option<T> find(Predicate<? super T> p) {
        var item = next();
        if (item.isSomeAnd(p)) return item;
        return None();
    }

    @Override
    public Option<T> findOrFirst(Predicate<? super T> predicate) {
        return next();
    }

    @Override
    public Option<T> findOrLast(Predicate<? super T> predicate) {
        return next();
    }

    @Override
    public boolean contains(T query) {
        return next().contains(query);
    }

    @Override
    public boolean any(Predicate<? super T> p) {
        return next().isSomeAnd(p);
    }

    @Override
    public boolean allEqual() {
        return true;
    }

    @Override
    public boolean all(Predicate<? super T> p) {
        return next().isSomeAnd(p);
    }

    @Override
    public boolean allUnique() {
        return true;
    }

    @Override
    public String join(String sep) {
        if (exhausted) return "";
        return next().unwrap().toString();
    }

    @Override
    public void forEach(Consumer<? super T> f) {
        next().ifSome(f);
    }

    @Override
    public ListIter<T> sorted() {
        if (exhausted) return new ListIter<>(List.of());
        return of(next().unwrap());
    }

    @Override
    public ListIter<T> sortedBy(Comparator<? super T> cmp) {
        if (exhausted) return new ListIter<>(List.of());
        return of(next().unwrap());
    }

    @Override
    public List<T> toList() {
        if (exhausted) return List.of();
        return List.of(next().unwrap());
    }

    @Override
    public <K extends Comparable<K>> ListIter<T> sortedByKey(Function<? super T, K> key) {
        if (exhausted) return new ListIter<>(List.of());
        return new ListIter<>(List.of(next().unwrap()));
    }

    @Override
    public Iterator<T> take(int n) {
        if (n == 0 || exhausted) return new Empty<>();
        return this;
    }

    @Override
    public Option<Pair<T, T>> minMax() {
        if (exhausted) return None();
        var item = next().unwrap();
        return Some(new Pair<>(item, item));
    }

    @Override
    public Option<Integer> positionMax() {
        if (exhausted) return None();
        return Some(0);
    }

    @Override
    public <K> Option<T> maxByKey(Function<? super T, K> key) {
        return next();
    }

    @Override
    public Option<T> reduce(BiFunction<? super T, ? super T, ? extends T> f) {
        return next();
    }

    @Override
    public <K> Option<Integer> positionMaxByKey(Function<? super T, K> key) {
        return next().map(elt -> 0);
    }

    @Override
    public Option<Integer> positionMaxBy(Comparator<? super T> cmp) {
        return next().map(elt -> 0);
    }

    @Override
    public Option<T> maxBy(Comparator<? super T> c) {
        return next();
    }

    @Override
    public Option<Integer> positionMin() {
        return next().map(elt -> 0);
    }

    @Override
    public <K> Option<T> minByKey(Function<? super T, K> key) {
        return next();
    }

    @Override
    public <K> Option<Integer> positionMinByKey(Function<? super T, K> key) {
        return next().map(elt -> 0);
    }

    @Override
    public Option<Integer> positionMinBy(Comparator<? super T> cmp) {
        return next().map(elt -> 0);
    }

    @Override
    public Option<T> minBy(Comparator<? super T> c) {
        return next();
    }

    @Override
    public Option<Pair<Integer, Integer>> positionMinMax() {
        return next().map(elt -> new Pair<>(0, 0));
    }

    @Override
    public <K> Option<Pair<T, T>> minMaxByKey(Function<? super T, K> key) {
        return next().map(elt -> new Pair<>(elt, elt));
    }

    @Override
    public <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<? super T, K> key) {
        return next().map(elt -> new Pair<>(0, 0));
    }

    @Override
    public Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<? super T> cmp) {
        return next().map(elt -> new Pair<>(0, 0));
    }

    @Override
    public Option<Pair<T, T>> minMaxBy(Comparator<? super T> cmp) {
        return next().map(elt -> new Pair<>(elt, elt));
    }

    @Override
    public HashMap<T, Integer> counts() {
        HashMap<T, Integer> map = new HashMap<>();
        if (exhausted) return map;
        map.put(next().unwrap(), 1);
        return map;
    }

    @Override
    public <K> HashMap<K, Integer> countsBy(Function<? super T, ? extends K> key) {
        HashMap<K, Integer> map = new HashMap<>();
        if (exhausted) return map;
        map.put(next().map(key).unwrap(), 1);
        return map;
    }

    @Override
    public boolean equals(IntoIter<T> iterable) {
        var iter = iterable.iter();
        return next().equals(iter.next()) && iter.next().isNone();
    }

    @Override
    public int len() {
        return exhausted ? 0 : 1;
    }

    @Override
    public Option<T> nextBack() {
        return next();
    }

    @Override
    public Option<T> next() {
        if (exhausted) return None();
        else {
            exhausted = true;
            return Some(value);
        }
    }

    @Override
    public String toString() {
        return "Once { " + value + " }";
    }
}
