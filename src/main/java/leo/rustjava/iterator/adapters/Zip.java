package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;

import static leo.rustjava.Option.None;
import static leo.rustjava.Option.Some;

public class Zip<L, R> implements Iterator<Pair<L, R>> {
    Iterator<L> left;
    Iterator<R> right;

    public Zip(Iterator<L> left, Iterator<R> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Option<Pair<L, R>> next() {
        Option<L> leftItem = left.next();
        if (leftItem.isNone()) return None();
        Option<R> rightItem = right.next();
        if (rightItem.isNone()) return None();
        return Some(new Pair<>(leftItem.unwrap(), rightItem.unwrap()));
    }

    @Override
    public String toString() {
        return "Zip { " + left + ", " + right + " }";
    }
}
