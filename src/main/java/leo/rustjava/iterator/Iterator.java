package leo.rustjava.iterator;

import leo.rustjava.*;
import leo.rustjava.iterator.adapters.Map;
import leo.rustjava.iterator.adapters.*;
import leo.rustjava.iterator.extra.*;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.IntoIter;
import leo.rustjava.iterator.sources.Empty;
import leo.rustjava.iterator.sources.ListIter;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

import static leo.rustjava.Option.*;
import static leo.rustjava.Unit.Unit;
import static leo.rustjava.iterator.Iterators.from;

@SuppressWarnings("unused")
public interface Iterator<Item> extends IntoIter<Item> {
    Option<Item> next();

    default SizeHint sizeHint() {
        return SizeHint.UNKNOWN;
    }

    default int count() {
        return fold(0, (acc, item) -> acc + 1);
    }

    /*#####################################################################
    # Static methods. Use these using `Iterator::apply(Iterator::method)` #
    #####################################################################*/
    static int sum(Iterator<Integer> iter) {
        return iter.reduce(Integer::sum).unwrapOr(0);
    }

    static int product(Iterator<Integer> iter) {
        return iter.reduce((acc, elt) -> acc * elt).unwrapOr(1);
    }

	/*###########################################################
	# Methods that return a single value (not of the item type) #
	###########################################################*/

    default <B> B fold(B seed, BiFunction<? super B, ? super Item, ? extends B> f) {
        B state = seed;
        while (true) {
            Option<Item> item = next();
            if (item.isNone()) return state;
            state = f.apply(state, item.unwrap());
        }
    }

    default <B> B tryFold(B seed, BiFunction<? super B, ? super Item, ControlFlow<B, B>> f) {
        B state = seed;
        while (true) {
            Option<Item> item = next();
            if (item.isNone()) return state;
            var flow = f.apply(state, item.unwrap());
            if (flow.isBreak()) return flow.breakValue().unwrap();
            state = flow.continueValue().unwrap();
        }
    }

    default Option<Integer> position(Predicate<? super Item> p) {
        return enumerate().fold(
                None(), (state, pair) -> {
                    if (state.isSome() || !p.test(pair.right())) return state;
                    else return Some(pair.left());
                }
        );
    }

    default Iterator<Pair<Integer, Item>> enumerate() {
        return new Enumerate<>(this);
    }

    default Option<Item> last() {
        return fold(
                None(), (last, item) -> Some(item)
        );
    }

    default Option<Item> nth(int n) {
        return advanceBy(n) ? next() : None();
    }

    default boolean advanceBy(int n) {
        return IntStream.range(0, n).noneMatch(i -> next().isNone());
    }

    default Iterator<Item> stepBy(int step) {
        return new StepBy<>(this, step);
    }

    default Iterator<Item> chain(IntoIter<Item> other) {
        return new Chain<>(this, other);
    }

    default <U> Iterator<Pair<Item, U>> zip(Iterator<U> other) {
        return new Zip<>(this, other);
    }

    default Iterator<Item> intersperse(Item item) {
        return new Intersperse<>(this, item);
    }

    default Peekable<Item> peekable() {
        return new Peekable<>(this);
    }

    default Iterator<Item> cycle() {
        return new Cycle<>(this);
    }

    default Iterator<Item> scan1(BiFunction<? super Item, ? super Item, ? extends Item> f) {
        Option<Item> first = next();
        if (first.isNone()) throw new IllegalArgumentException("Empty iterator");
        return scan(first.unwrap(), f);
    }


    /*####################################
    # Methods that return a new iterator #
    ####################################*/

    default <U> Iterator<U> scan(U seed, BiFunction<? super U, ? super Item, ? extends U> f) {
        return new Scan<>(this, f, seed);
    }

    default Iterator<Item> filter(Predicate<? super Item> p) {
        return new Filter<>(this, p);
    }

    default <U> Iterator<U> filterMap(Function<? super Item, ? extends Option<U>> f) {
        return new FilterMap<>(this, f);
    }

    default <U> Iterator<U> flatMap(Function<? super Item, IntoIter<U>> f) {
        return new FlatMap<>(this, f);
    }

    default Iterator<Item> skipWhile(Predicate<? super Item> p) {
        return new SkipWhile<>(this, p);
    }

    default Iterator<Item> takeWhile(Predicate<? super Item> p) {
        return new TakeWhile<>(this, p);
    }

    default Iterator<Item> mapWhile(Function<? super Item, Option<Item>> f) {
        return new MapWhile<>(this, f);
    }

    default Iterator<Item> inspect(Consumer<? super Item> c) {
        return new Inspect<>(this, c);
    }

    default Iterator<Item> skip(int n) {
        if (n == 0) return this;
        return new Skip<>(this, n);
    }

    default Iterator<Item> fuse() {
        return new Fuse<>(this);
    }

    @Override
    default Iterator<Item> iter() {
        return this;
    }

    default Iterator<Item> interleave(IntoIter<Item> other) {
        return new Interleave<>(this, other);
    }

    default Iterator<Item> interleaveShortest(IntoIter<Item> other) {
        return new InterleaveShortest<>(this, other);
    }

    default <K> Iterator<Pair<K, List<Item>>> groupBy(Function<? super Item, ? extends K> key) {
        return new GroupBy<>(this, key);
    }

    default Iterator<Iterator<Item>> chunks(int chunkSize) {
        return new Chunks<>(this, chunkSize);
    }

    default Iterator<ExactSizeIterator<Item>> chunksExact(int chunkSize) {
        return new ChunksExact<>(this, chunkSize);
    }

    default Iterator<Pair<Item, Item>> pairWindows() {
        return new PairWindows<>(this);
    }

    default Iterator<Pair<Item, Item>> pairs() {
        return new Pairs<>(this);
    }

    default <U> Iterator<Pair<Item, U>> cartesianProduct(IntoIter<U> other) {
        return new CartesianProduct<>(this, other);
    }

    default <U> Iterator<U> coalesce(BiFunction<Item, Item, U> function) {
        throw new UnsupportedOperationException();
    }


    /*#######################
    # Collect this iterator #
    #######################*/

    default Iterator<Item> dedup() {
        return new Dedup<>(this);
    }

    default Iterator<Item> dedupBy(BiPredicate<? super Item, ? super Item> cmp) {
        return new DedupBy<>(this, cmp);
    }

    default Iterator<Pair<Integer, Item>> dedupWithCount() {
        return new DedupWithCount<>(this);
    }

    /*#############################################
    # Itertools methods (not in standard library) #
    #############################################*/

    default Iterator<Pair<Integer, Item>> dedupByWithCount(BiPredicate<? super Item, ? super Item> cmp) {
        return new DedupByWithCount<>(this, cmp);
    }

    default Iterator<Item> duplicates() {
        return new Duplicates<>(this);
    }

    default <U> Iterator<Item> duplicatesBy(Function<? super Item, U> id) {
        return new DuplicatesBy<>(this, id);
    }

    default Iterator<Item> unique() {
        return new Unique<>(this);
    }

    default <U> Iterator<Item> uniqueBy(Function<? super Item, U> id) {
        return new UniqueBy<>(this, id);
    }

    default Iterator<Item> combinations(int k) {
        throw new UnsupportedOperationException();
    }

    default Iterator<Item> permutations(int k) {
        throw new UnsupportedOperationException();
    }

    default Iterator<List<Item>> powerset() {
        return new Powerset<>(this);
    }

    default Iterator<Item> padUsing(int min, Function<? super Integer, ? extends Item> function) {
        return new PadUsing<>(this, min, function);
    }

    default Iterator<Integer> positions(Predicate<? super Item> predicate) {
        return new Positions<>(this, predicate);
    }

    default Option<Pair<Integer, Item>> findPosition(Predicate<? super Item> predicate) {
        return enumerate().find(pair -> predicate.test(pair.right()));
    }

    default Option<Item> find(Predicate<? super Item> p) {
        while (true) {
            var item = next();
            if (item.isNone()) break;
            if (p.test(item.unwrap())) return item;
        }
        return None();
    }

    default Option<Item> findOrFirst(Predicate<? super Item> predicate) {
        var first = next();
        if (first.map(predicate::test).contains(true)) return first;
        return find(predicate).or(first);
    }

    default Option<Item> findOrLast(Predicate<? super Item> predicate) {
        Option<Item> last = None();
        while (true) {
            var item = next();
            if (item.isNone()) break;
            if (item.map(predicate::test).contains(true)) return item;
            last = item;
        }
        return last;
    }

    default boolean contains(Item query) {
        return any(query::equals);
    }


    default boolean any(Predicate<? super Item> p) {
        while (true) {
            var item = next();
            if (item.isNone()) break;
            if (p.test(item.unwrap())) return true;
        }
        return false;
    }

    default boolean allEqual() {
        var item = next();
        if (item.isNone()) return true;
        var content = item.unwrap();
        return all(content::equals);
    }

    default boolean all(Predicate<? super Item> p) {
        while (true) {
            var item = next();
            if (item.isNone()) break;
            if (!p.test(item.unwrap())) return false;
        }
        return true;
    }

    default boolean allUnique() {
        var used = new HashSet<>();
        return all(used::add);
    }

    default String join(String sep) {
        var item = next();
        if (item.isNone()) return "";
        var sb = new StringBuilder(item.unwrap().toString());
        forEach(e -> {
            sb.append(sep);
            sb.append(e.toString());
        });
        return sb.toString();
    }

    default void forEach(Consumer<? super Item> f) {
        fold(
                0, (_state, item) -> {
                    f.accept(item);
                    return 0;
                }
        );
    }

    default void tryForEach(Function<? super Item, ControlFlow<Unit, Unit>> f) {
        tryFold(Unit(), (_acc, elt) -> f.apply(elt));
    }

    default ListIter<Item> sorted() {
        return sortedBy(null);
    }

    default ListIter<Item> sortedBy(Comparator<? super Item> cmp) {
        var list = toList();
        list.sort(cmp);
        return from(list);
    }

    default List<Item> toList() {
        List<Item> list = new ArrayList<>();
        while (true) {
            Option<Item> item = next();
            if (item.isNone()) break;
            list.add(item.unwrap());
        }
        return list;
    }

    default <K extends Comparable<K>> ListIter<Item> sortedByKey(Function<? super Item, K> key) {
        var list = map(e -> new Pair<>(key.apply(e), e)).toList();
        list.sort(Comparator.comparing(Pair::left));
        return from(list.stream().map(Pair::right).toList());
    }

    default <U> Iterator<U> map(Function<? super Item, ? extends U> f) {
        return new Map<>(this, f);
    }

    @SuppressWarnings("unchecked")
    default Iterator<Item> kSmallest(int k) {
        TreeSet<Item> heap = new TreeSet<>();
        take(k).forEach(heap::add);
        forEach(e -> {
            var last = heap.last();
            if (((Comparable<Item>) last).compareTo(e) > 0) {
                heap.remove(last);
                heap.add(e);
            }
        });
        return from(heap.stream().toList());
    }

    default Iterator<Item> take(int n) {
        if (n == 0) return new Empty<>();
        return new Take<>(this, n);
    }

    @SuppressWarnings("unchecked")
    default Option<Pair<Item, Item>> minMax() {
        Option<Item> min = None();
        Option<Item> max = None();
        forEach(elt -> {
            min
                    .insert(min.or(Some(elt))
                            .map(old -> ((Comparable<Item>) elt).compareTo(old) < 0 ? elt : old).unwrap());
            max
                    .insert(max.or(Some(elt))
                            .map(old -> ((Comparable<Item>) elt).compareTo(old) >= 0 ? elt : old).unwrap());
        });
        return min.zip(max);
    }

    default Option<Integer> positionMax() {
        return enumerate().maxByKey(Pair::right).map(Pair::left);
    }

    @SuppressWarnings("unchecked")
    default <K> Option<Item> maxByKey(Function<? super Item, K> key) {
        return map(e -> new Pair<>(key.apply(e), e))
                .reduce((a, b) -> ((Comparable<K>) a.left()).compareTo(b.left()) > 0 ? a : b)
                .map(Pair::right);
    }

    default Option<Item> reduce(BiFunction<? super Item, ? super Item, ? extends Item> f) {
        Option<Item> first = next();
        if (first.isNone()) return None();
        return Some(fold(first.unwrap(), f));
    }

    default <K> Option<Integer> positionMaxByKey(Function<? super Item, K> key) {
        return enumerate().maxByKey(pair -> key.apply(pair.right())).map(Pair::left);
    }

    default Option<Integer> positionMaxBy(Comparator<? super Item> cmp) {
        return enumerate()
                .maxBy(Comparator.comparing(Pair::right, cmp))
                .map(Pair::left);
    }

    default Option<Item> maxBy(Comparator<? super Item> c) {
        return reduce((x, y) -> c.compare(x, y) > 0 ? x : y);
    }

    default Option<Integer> positionMin() {
        return enumerate().minByKey(Pair::right).map(Pair::left);
    }

    @SuppressWarnings("unchecked")
    default <K> Option<Item> minByKey(Function<? super Item, K> key) {
        return map(e -> new Pair<>(key.apply(e), e))
                .reduce((a, b) -> ((Comparable<K>) a.left()).compareTo(b.left()) <= 0 ? a : b)
                .map(Pair::right);
    }

    default <K> Option<Integer> positionMinByKey(Function<? super Item, K> key) {
        return enumerate().minByKey(pair -> key.apply(pair.right())).map(Pair::left);
    }

    default Option<Integer> positionMinBy(Comparator<? super Item> cmp) {
        return enumerate()
                .minBy(Comparator.comparing(Pair::right, cmp))
                .map(Pair::left);
    }

    default Option<Item> minBy(Comparator<? super Item> c) {
        return reduce((x, y) -> c.compare(x, y) <= 0 ? x : y);
    }

    default Option<Pair<Integer, Integer>> positionMinMax() {
        return enumerate()
                .minMaxByKey(Pair::right)
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    @SuppressWarnings("unchecked")
    default <K> Option<Pair<Item, Item>> minMaxByKey(Function<? super Item, K> key) {
        Option<Item> min = None();
        Option<Item> max = None();
        forEach(elt -> {
            min.insert(min.or(Some(elt))
                    .map(old -> ((Comparable<K>) key.apply(elt)).compareTo(key.apply(old)) < 0 ? elt : old).unwrap());
            max.insert(max.or(Some(elt))
                    .map(old -> ((Comparable<K>) key.apply(elt)).compareTo(key.apply(old)) >= 0 ? elt : old).unwrap());
        });
        return min.zip(max);
    }

    default <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<? super Item, K> key) {
        return enumerate()
                .minMaxByKey(pair -> key.apply(pair.right()))
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    default Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<? super Item> cmp) {
        return enumerate()
                .minMaxBy(Comparator.comparing(Pair::right, cmp))
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    default Option<Pair<Item, Item>> minMaxBy(Comparator<? super Item> cmp) {
        Option<Item> min = None();
        Option<Item> max = None();
        forEach(elt -> {
            min.insert(min.or(Some(elt))
                    .map(old -> cmp.compare(elt, old) < 0 ? elt : old).unwrap());
            max.insert(max.or(Some(elt))
                    .map(old -> cmp.compare(elt, old) >= 0 ? elt : old).unwrap());
        });
        return min.zip(max);
    }

    default HashMap<Item, Integer> counts() {
        HashMap<Item, Integer> counts = new HashMap<>();
        forEach(elt -> counts.put(elt, counts.getOrDefault(elt, 0) + 1));
        return counts;
    }

    default <K> HashMap<K, Integer> countsBy(Function<? super Item, ? extends K> key) {
        HashMap<K, Integer> counts = new HashMap<>();
        forEach(elt -> {
            var eltKey = key.apply(elt);
            counts.put(eltKey, counts.getOrDefault(eltKey, 0) + 1);
        });
        return counts;
    }

    default <U, V> Iterator<V> zipWith(Iterator<? extends U> other, BiFunction<? super Item, U, ? extends V> function) {
        return this.zip(other).map(pair -> function.apply(pair.left(), pair.right()));
    }


    default boolean equals(IntoIter<Item> iterable) {
        var iter = iterable.iter();
        if (this == iter) return true;
        if (iter == null) return false;
        while (true) {
            var myItem = next();
            var theirItem = iter.next();
            if (myItem.or(theirItem).isNone()) return true;
            if (!myItem.equals(theirItem)) return false;
        }
    }

    static <T> Iterator<T> flatten(Iterator<Iterator<T>> iter) {
        return new Flatten<>(iter);
    }

    default <U> U apply(Function<? super Iterator<Item>, U> function) {
        return function.apply(this);
    }

    default <U> Iterator<U> adapt(Function<Iterator<Item>, ? extends Iterator<U>> function) {
        return new Adapt<>(this, function);
    }
}
