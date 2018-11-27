package utility;

import java.util.HashSet;
import java.util.Set;

public class SetFunctions<T> {
	public int intersection (HashSet<T> set, HashSet<T> HashSet) {
		HashSet<T> a;
		HashSet<T> b;
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
	public HashSet<T> intersectionSet (HashSet<T> set1, HashSet<T> set2) {
		if(set1 == null)
			return new HashSet<T>();
		else if (set2 == null)
			return new HashSet<T>();
		Set<T> a;
		Set<T> b;
		HashSet<T> returnSet = new HashSet<T>();
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

	public HashSet<T> unionSet (HashSet<T> set1, HashSet<T> set2) {
		if(set1 == null)
			return set2;
		else if (set2 == null)
			return set1;
		HashSet<T> returnSet = new HashSet <T>();
		returnSet.addAll(set1);
		returnSet.addAll(set2);
		return returnSet;
	}

}

