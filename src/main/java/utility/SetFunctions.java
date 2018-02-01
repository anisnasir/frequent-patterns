package utility;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.set.hash.THashSet;

public class SetFunctions<T> {
	public int intersection (THashSet<T> set, THashSet<T> tHashSet) {
		THashSet<T> a;
		THashSet<T> b;
		int counter = 0;
		if(set == null || tHashSet == null)
			return 0;
		if (set.size() <= tHashSet.size()) {
			a = set;
			b = tHashSet; 
		} else {
			a = tHashSet;
			b = set;
		}
		for (T e : a) {
			if (b.contains(e)) {
				counter++;
			} 
		}
		return counter;
	}
	public THashSet<T> intersectionSet (THashSet<T> set1, THashSet<T> set2) {
		if(set1 == null)
			return new THashSet<T>();
		else if (set2 == null)
			return new THashSet<T>();
		Set<T> a;
		Set<T> b;
		THashSet<T> returnSet = new THashSet<T>();
		if (set1.size() <= set2.size()) {
			a = set1;
			b = set2; 
		} else {
			a = set2;
			b = set1;
		}
		for (T e : a) {
			if (b.contains(e)) {
				returnSet.add(e);
			} 
		}
		return returnSet;
	}

	public THashSet<T> unionSet (THashSet<T> set1, THashSet<T> set2) {
		if(set1 == null)
			return set2;
		else if (set2 == null)
			return set1;
		THashSet<T> returnSet = new THashSet <T>();
		returnSet.addAll(set1);
		returnSet.addAll(set2);
		return returnSet;
	}

}

