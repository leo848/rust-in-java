package leo.rustjava.iterator.adapters;

import leo.rustjava.Option;
import leo.rustjava.iterator.Iterator;
import leo.rustjava.iterator.SizeHint;
import leo.rustjava.iterator.interfaces.FusedIterator;

import java.util.function.Predicate;

import static leo.rustjava.Option.None;

public class TakeWhile<T> implements Iterator<T>, FusedIterator<T> {
    private final Iterator<T> iter;
	private final Predicate<? super T> predicate;
	private boolean flag;

	public TakeWhile(Iterator<T> iter, Predicate<? super T> predicate) {
		this.iter = iter;
		this.predicate = predicate;
	}

    @Override
    public Option<T> next() {
        if (flag) return None();
        Option<T> item = iter.next();
        if (item.isNone()) return None();
        else if (predicate.test(item.unwrap())) return item;
        else {
            flag = true;
            return None();
        }
    }

    @Override
    public SizeHint sizeHint() {
        if (flag) return SizeHint.ZERO;
        else return new SizeHint(0, iter.sizeHint().upper());
    }

    @Override
    public String toString() {
        return "TakeWhile { " + iter + " }";
    }
}
