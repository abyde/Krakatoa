package util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class MergeIteratorsTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		int numIts = 3;
		int numItems = 20;

		Random r = new Random(1);
		Iterator<Integer>[] it = new Iterator[numIts];
		for (int i = 0; i < numIts; i++) {
			TreeSet<Integer> ts = new TreeSet<Integer>();
			for (int j = 0; j < numItems; j++) {
				ts.add(r.nextInt(10000));
			}
			it[i] = ts.iterator();
		}

		Iterator<Integer> mit = new MergeIterators<Integer>(
				new Comparator<Integer>() {
					public int compare(Integer i1, Integer i2) {
						return i1 - i2;
					}
				}, it, new Integer[3]);

		Integer current = 0;
		while (mit.hasNext()) {
			Integer next = mit.next();
			System.out.println("next = " + next);
			if (next < current)
				throw new RuntimeException("Incorrect order!");
			current = next;
		}
	}
}
