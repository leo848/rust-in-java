package leo.rustjava.iterator.sources;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.interfaces.DoubleEndedIterator;
import leo.rustjava.iterator.interfaces.ExactSizeIterator;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.List;

// Iterators not present in Rust modules
public class ListIter<T> implements Iterator<T>, DoubleEndedIterator<T>, ExactSizeIterator<T>, FusedIterator<T> {
    List<T> list;
    int index;
    int indexBack;

    public ListIter(List<T> list) {
        this.list = list;
        this.index = 0;
        this.indexBack = list.size() - 1;
    }

    @Override
    public Option<T> next() {
        if (index <= indexBack) {
            return Option.Some(list.get(index++));
        } else {
            return Option.None();
        }
    }

    @Override
    public Option<T> nextBack() {
        if (index <= indexBack) {
            return Option.Some(list.get(indexBack--));
        } else {
            return Option.None();
        }
    }

    @Override
    public int len() {
        return list.size() - index;
    }

    @Override
    public List<T> toList() {
        if (index == 0) return list;
        else return list.subList(index, list.size());
    }

    @Override
    public String toString() {
        return "ListIter { " + list + " }";
    }
}
