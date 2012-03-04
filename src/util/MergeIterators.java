package util;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Merges multiple iterators.
 * 
 * @author abyde
 */
public class MergeIterators<E> implements Iterator<E> {
	private Iterator<E>[] itList;
	private E[] nextList;
	private E next = null;
	private Comparator<E> comp;

	public MergeIterators(Comparator<E> comp, Iterator<E>[] itList,
			E[] nextContainer) {
		if (itList == null)
			throw new IllegalArgumentException("Null array of iterators");
		if (comp == null)
			throw new IllegalArgumentException("Null comparator");
		if (nextContainer == null)
			throw new IllegalArgumentException("Null next container");
		if (itList.length != nextContainer.length)
			throw new IllegalArgumentException(
					"Iterator array and next container must have the same size");

		this.comp = comp;
		this.itList = itList;
		this.nextList = nextContainer;

		for (int i = 0; i < itList.length; i++) {
			Iterator<E> it = itList[i];
			if (it.hasNext()) {
				nextList[i] = it.next();
			} else {
				nextList[i] = null;
			}
		}
		advance();
	}

	/**
	 * where the work happens!
	 */
	private void advance() {
		// find the smallest element from any iterator.
		next = nextList[0];
		for (int i = 1; i < itList.length; i++) {
			E other = nextList[i];
			/*
			 * A.compareTo(B) returns a negative integer, zero, or a positive
			 * integer as A is less than, equal to, or greater than B.
			 */
			if (other == null)
				continue;
			if ((next == null) || (comp.compare(next, other) > 0)) {
				// next is strictly greater than 'other', so update
				next = other;
			}
		}

		/*
		 * Phase two: move all iterators until they are strictly smaller than
		 * 'next'. Note this includes the chosen one -- its value has already
		 * been stored in 'next' so this is safe.
		 */
		for (int i = 0; i < itList.length; i++) {
			Iterator<E> it = itList[i];
			E current = nextList[i];
			if (current == null)
				continue;

			boolean found = comp.compare(next, current) < 0;

			while (!found && it.hasNext()) {
				current = it.next();
				found = comp.compare(next, current) < 0;
			}

			if (found)
				nextList[i] = current;
			else
				nextList[i] = null;
		}
	}

	@Override
	public boolean hasNext() {
		return (next != null);
	}

	@Override
	public E next() {
		E ret = next;
		advance();
		return ret;
	}

	@Override
	public void remove() {
		for (int i = 0; i < itList.length; i++) {
			itList[i].remove();
		}
	}
}
