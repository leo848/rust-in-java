package leo.rustjava.iterator;

import leo.rustjava.Option;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;

public record SizeHint(int lower, Option<Integer> upper) {
    public static SizeHint UNKNOWN = new SizeHint(0, None());
    public static SizeHint ENDLESS = new SizeHint(Integer.MAX_VALUE, None());
    public static SizeHint ZERO = SizeHint.exact(0);

    public static SizeHint exact(int n) {
        return new SizeHint(n, Some(n));
    }

    public static SizeHint max(int n) {
        return new SizeHint(0, Some(n));
    }

    public static SizeHint max(SizeHint sizeHint) {
        return new SizeHint(0, sizeHint.upper);
    }

    public SizeHint add(SizeHint other) {
        return new SizeHint(
                lower + other.lower(),
                upper.andThen(u -> other.upper.map(o -> u + o))
        );
    }

    public SizeHint add(int n) {
        return new SizeHint(lower + n, upper.map(u -> u + n));
    }

    public SizeHint mul(SizeHint other) {
        return new SizeHint(
                lower * other.lower,
                upper.andThen(u -> other.upper.map(o -> u * o))
        );
    }

    public SizeHint div(int factor) {
        return new SizeHint(
                lower / factor - 1,
                upper.map(u -> u / factor + 1)
        );
    }

    public SizeHint sub(int count) {
        return new SizeHint(
                lower - count,
                upper.map(u -> u - count)
        );
    }

    public SizeHint mul(int i) {
        return new SizeHint(
                lower * i,
                upper.map(u -> u * i)
        );
    }
}
