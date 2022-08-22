package leo.rustjava.iterator;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.adapters.Map;
import leo.rustjava.iterator.adapters.*;
import leo.rustjava.iterator.extra.*;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.IntoIter;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;
import static leo.rustjava.iterator.Iterators.fromList;

@SuppressWarnings("unused")
public interface Iterator<Item> extends IntoIter<Item> {
    default SizeHint sizeHint() {
        return SizeHint.UNKNOWN;
    }

    default int count() {
        return fold(0, (acc, item) -> acc + 1);
    }

	/*###########################################################
	# Methods that return a single value (not of the item type) #
	###########################################################*/

    default <B> B fold(B seed, BiFunction<B, Item, B> f) {
        B state = seed;
        while (true) {
            Option<Item> item = next();
            if (item.isNone()) return state;
            state = f.apply(state, item.unwrap());
        }
    }

    Option<Item> next();

    default <B> B tryFold(B seed, BiFunction<B, Item, Option<B>> f) {
        B state = seed;
        while (true) {
            Option<Item> item = next();
            if (item.isNone()) return state;
            var maybeState = f.apply(state, item.unwrap());
            if (maybeState.isNone()) return state;
            state = maybeState.unwrap();
        }
    }

    default Option<Integer> position(Predicate<Item> p) {
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

    /*##############################
    # Methods that return one item #
    ##############################*/

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

    default Iterator<Item> scan1(BiFunction<Item, Item, Item> f) {
        Option<Item> first = next();
        if (first.isNone()) throw new IllegalArgumentException("Empty iterator");
        return scan(first.unwrap(), f);
    }


    /*####################################
    # Methods that return a new iterator #
    ####################################*/

    default <U> Iterator<U> scan(U seed, BiFunction<U, Item, U> f) {
        return new Scan<>(this, f, seed);
    }

    default Iterator<Item> filter(Predicate<Item> p) {
        return new Filter<>(this, p);
    }

    default Iterator<Item> filterMap(Function<Item, Option<Item>> f) {
        return new FilterMap<>(this, f);
    }

    default <U> Iterator<U> flatMap(Function<Item, IntoIter<U>> f) {
        return new FlatMap<>(this, f);
    }

    default Iterator<Item> skipWhile(Predicate<Item> p) {
        return new SkipWhile<>(this, p);
    }

    default Iterator<Item> takeWhile(Predicate<Item> p) {
        return new TakeWhile<>(this, p);
    }

    default Iterator<Item> mapWhile(Function<Item, Option<Item>> f) {
        return new MapWhile<>(this, f);
    }

    default Iterator<Item> inspect(Consumer<Item> c) {
        return new Inspect<>(this, c);
    }

    default Iterator<Item> skip(int n) {
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

    default <K> Iterator<Pair<K, List<Item>>> groupBy(Function<Item, K> key) {
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

    default Iterator<Item> dedupBy(BiPredicate<Item, Item> cmp) {
        return new DedupBy<>(this, cmp);
    }

    default Iterator<Pair<Integer, Item>> dedupWithCount() {
        return new DedupWithCount<>(this);
    }

    /*#############################################
    # Itertools methods (not in standard library) #
    #############################################*/

    default Iterator<Pair<Integer, Item>> dedupByWithCount(BiPredicate<Item, Item> cmp) {
        return new DedupByWithCount<>(this, cmp);
    }

    default Iterator<Item> duplicates() {
        return new Duplicates<>(this);
    }

    default <U> Iterator<Item> duplicatesBy(Function<Item, U> id) {
        return new DuplicatesBy<>(this, id);
    }

    default Iterator<Item> unique() {
        return new Unique<>(this);
    }

    default <U> Iterator<Item> uniqueBy(Function<Item, U> id) {
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

    default Iterator<Item> padUsing(int min, Function<Integer, Item> function) {
        return new PadUsing<>(this, min, function);
    }

    default Iterator<Integer> positions(Predicate<Item> predicate) {
        return new Positions<>(this, predicate);
    }

    default Option<Pair<Integer, Item>> findPosition(Predicate<Item> predicate) {
        return enumerate().find(pair -> predicate.test(pair.right()));
    }

    default Option<Item> find(Predicate<Item> p) {
        while (true) {
            var item = next();
            if (item.isNone()) break;
            if (p.test(item.unwrap())) return item;
        }
        return None();
    }

    default Option<Item> findOrFirst(Predicate<Item> predicate) {
        var first = next();
        if (first.map(predicate::test).contains(true)) return first;
        return find(predicate).or(first);
    }

    default Option<Item> findOrLast(Predicate<Item> predicate) {
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

    default boolean any(Predicate<Item> p) {
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

    default boolean all(Predicate<Item> p) {
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

    default void forEach(Consumer<Item> f) {
        fold(
                0, (_state, item) -> {
                    f.accept(item);
                    return 0;
                }
        );
    }

    default Iterator<Item> sorted() {
        return sortedBy(null);
    }

    default Iterator<Item> sortedBy(Comparator<Item> cmp) {
        var list = toList();
        list.sort(cmp);
        return fromList(list);
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

    default <K extends Comparable<K>> Iterator<Item> sortedByKey(Function<Item, K> key) {
        var list = map(e -> new Pair<>(key.apply(e), e)).toList();
        list.sort(Comparator.comparing(Pair::left));
        return fromList(list.stream().map(Pair::right).toList());
    }

    default <U> Iterator<U> map(Function<Item, U> f) {
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
        return fromList(heap.stream().toList());
    }

    default Iterator<Item> take(int n) {
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
    default <K> Option<Item> maxByKey(Function<Item, K> key) {
        return map(e -> new Pair<>(key.apply(e), e))
                .reduce((a, b) -> ((Comparable<K>) a.left()).compareTo(b.left()) > 0 ? a : b)
                .map(Pair::right);
    }

    default Option<Item> reduce(BiFunction<Item, Item, Item> f) {
        Option<Item> first = next();
        if (first.isNone()) return None();
        return Some(fold(first.unwrap(), f));
    }

    default <K> Option<Integer> positionMaxByKey(Function<Item, K> key) {
        return enumerate().maxByKey(pair -> key.apply(pair.right())).map(Pair::left);
    }

    default Option<Integer> positionMaxBy(Comparator<Item> cmp) {
        return enumerate()
                .maxBy(Comparator.comparing(Pair::right, cmp))
                .map(Pair::left);
    }

    default Option<Item> maxBy(Comparator<Item> c) {
        return reduce((x, y) -> c.compare(x, y) > 0 ? x : y);
    }

    default Option<Integer> positionMin() {
        return enumerate().minByKey(Pair::right).map(Pair::left);
    }

    @SuppressWarnings("unchecked")
    default <K> Option<Item> minByKey(Function<Item, K> key) {
        return map(e -> new Pair<>(key.apply(e), e))
                .reduce((a, b) -> ((Comparable<K>) a.left()).compareTo(b.left()) <= 0 ? a : b)
                .map(Pair::right);
    }

    default <K> Option<Integer> positionMinByKey(Function<Item, K> key) {
        return enumerate().minByKey(pair -> key.apply(pair.right())).map(Pair::left);
    }

    default Option<Integer> positionMinBy(Comparator<Item> cmp) {
        return enumerate()
                .minBy(Comparator.comparing(Pair::right, cmp))
                .map(Pair::left);
    }

    default Option<Item> minBy(Comparator<Item> c) {
        return reduce((x, y) -> c.compare(x, y) <= 0 ? x : y);
    }

    default Option<Pair<Integer, Integer>> positionMinMax() {
        return enumerate()
                .minMaxByKey(Pair::right)
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    @SuppressWarnings("unchecked")
    default <K> Option<Pair<Item, Item>> minMaxByKey(Function<Item, K> key) {
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

    default <K> Option<Pair<Integer, Integer>> positionMinMaxByKey(Function<Item, K> key) {
        return enumerate()
                .minMaxByKey(pair -> key.apply(pair.right()))
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    default Option<Pair<Integer, Integer>> positionMinMaxBy(Comparator<Item> cmp) {
        return enumerate()
                .minMaxBy(Comparator.comparing(Pair::right, cmp))
                .map(pair -> new Pair<>(pair.left().left(), pair.right().left()));
    }

    default Option<Pair<Item, Item>> minMaxBy(Comparator<Item> cmp) {
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

    default <K> HashMap<K, Integer> countsBy(Function<Item, K> key) {
        HashMap<K, Integer> counts = new HashMap<>();
        forEach(elt -> {
            var eltKey = key.apply(elt);
            counts.put(eltKey, counts.getOrDefault(eltKey, 0) + 1);
        });
        return counts;
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
}