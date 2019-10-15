package utility;

import java.util.Set;

import gnu.trove.set.hash.THashSet;

public class SetFunctions<T> {
	public int intersection (THashSet<T> set, THashSet<T> HashSet) {
		THashSet<T> a;
		THashSet<T> b;
		int counter = 0;
		if(set == null || HashSet == null)
			return 0;
		if (set.size() <= HashSet.size()) {
			a = set;
			b = HashSet; 
		} else {
			a = HashSet;
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
		THashSet<T> returnSet = new THashSet<>();
		returnSet.addAll(set1);
		returnSet.addAll(set2);
		return returnSet;
	}

}

