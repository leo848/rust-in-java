package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.EndlessIterator;

import static leo.rustjava.Option.Some;
import static leo.rustjava.iterator.Iterators.*;

public class EndlessRange implements Iterator<Integer>, EndlessIterator<Integer> {
    int n;

    public EndlessRange(int n) {
        this.n = n;
    }

    @Override
    public Option<Integer> next() {
        return Some(n++);
    }

    @Override
    public Iterator<Integer> copy() {
        return new EndlessRange(n);
    }

    @Override
    public Iterator<Integer> take(int amount) {
        return range(n, n + amount);
    }

    @Override
    public Iterator<Integer> skip(int amount) {
        return range(n + amount);
    }

    @Override
    public Iterator<Integer> kSmallest(int k) {
        return take(k);
    }

    @Override
    public Option<Integer> positionMin() {
        return Some(0);
    }

    @Override
    public Iterator<Integer> duplicates() {
        return empty();
    }

    @Override
    public Iterator<Integer> unique() {
        return this;
    }

    @Override
    public boolean contains(Integer query) {
        return query >= n;
    }

    @Override
    public boolean allEqual() {
        return false;
    }

    @Override
    public boolean allUnique() {
        return true;
    }

    @Override
    public String toString() {
        return n + "..";
    }
}
