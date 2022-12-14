package leo.rustjava.iterator;

import leo.rustjava.Option;
import leo.rustjava.iterator.interfaces.IntoIter;
import leo.rustjava.iterator.sources.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Iterators {
    @SafeVarargs
    public static <Item> ListIter<Item> of(final Item... items) {
        return new ListIter<>(Arrays.asList(items));
    }

    public static <Item> FromFunction<Item> fromFunction(final Supplier<Option<Item>> f) {
        return new FromFunction<>(f);
    }

    public static <Item> Repeat<Item> repeat(final Item item) {
        return new Repeat<>(item);
    }

    public static <Item> RepeatWith<Item> repeatWith(final Supplier<? extends Item> supplier) {
        return new RepeatWith<>(supplier);
    }

    public static Range range(final int start, final int end) {
        return new Range(start, end);
    }

    public static EndlessRange range(final int start) {
        return new EndlessRange(start);
    }

    public static Range rangeInclusive(final int start, final int end) {
        return new Range(start, end + 1);
    }

    public static <Item> Successors<Item> successors(final Item seed, final Function<? super Item, Option<Item>> f) {
        return new Successors<>(seed, f);
    }

    public static <Item> Empty<Item> empty() {
        return new Empty<>();
    }

    public static <Item> Once<Item> once(final Item item) {
        return new Once<>(item);
    }

    public static <Item> OnceWith<Item> onceWith(final Supplier<? extends Item> supplier) {
        return new OnceWith<>(supplier);
    }

    public static ListIter<Character> chars(final String string) {
        return from(string.chars().mapToObj(num -> (char) num).toList());
    }

    public static <Item> ListIter<Item> from(final List<Item> list) {
        return new ListIter<>(list);
    }

    public static <Item> Iterator<Item> from(IntoIter<Item> iter) {
        return iter.iter();
    }

    public static <Item> Iterator<Item> from(Iterable<? extends Item> iter) {
        return from(iter.iterator());
    }

    public static <Item> Iterator<Item> from(Stream<? extends Item> stream) {
        return from(stream.iterator());
    }

    public static <Item> Iterator<Item> from(Item[] array) {
        return from(Arrays.asList(array));
    }

    public static <Item> Iterator<Item> from(java.util.Iterator<? extends Item> iter) {
        return new FromIterable<>(iter);
    }
}
