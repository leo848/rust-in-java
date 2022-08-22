package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.EndlessIterator;

public class EndlessRange implements Iterator<Integer>, EndlessIterator<Integer> {
    int n;

    public EndlessRange(int n) {
        this.n = n;
    }

    @Override
    public Option<Integer> next() {
        return Option.Some(n++);
    }

    @Override
    public String toString() {
        return "EndlessRange { n: " + n + " }";
    }
}
