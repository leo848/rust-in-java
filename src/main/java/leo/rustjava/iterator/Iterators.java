package leo.rustjava.iterator;

import leo.rustjava.Option;
import leo.rustjava.iterator.sources.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Iterators {
    @SafeVarargs
    public static <Item> ListIter<Item> of(Item... items) {
        return new ListIter<>(Arrays.asList(items));
    }

    public static <Item> FromFunction<Item> fromFunction(Supplier<Option<Item>> f) {
        return new FromFunction<>(f);
    }

    public static <Item> Repeat<Item> repeat(Item item) {
        return new Repeat<>(item);
    }

    public static <Item> RepeatWith<Item> repeatWith(Supplier<Item> supplier) {
        return new RepeatWith<>(supplier);
    }

    public static Range range(int start, int end) {
        return new Range(start, end);
    }

    public static EndlessRange range(int start) {
        return new EndlessRange(start);
    }

    public static Range rangeInclusive(int start, int end) {
        return new Range(start, end + 1);
    }

    public static <Item> Successors<Item> successors(Item seed, Function<Item, Option<Item>> f) {
        return new Successors<>(seed, f);
    }

    public static <Item> Empty<Item> empty() {
        return new Empty<>();
    }

    public static <Item> Once<Item> once(Item item) {
        return new Once<>(item);
    }

    public static <Item> OnceWith<Item> onceWith(Supplier<Item> supplier) {
        return new OnceWith<>(supplier);
    }

    public static ListIter<Character> chars(String string) {
        return fromList(string.chars().mapToObj(num -> (char) num).toList());
    }

    public static <Item> ListIter<Item> fromList(List<Item> list) {
        return new ListIter<>(list);
    }

}
