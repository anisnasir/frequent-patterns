package utility;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.set.hash.THashSet;

public class SetFunctions<T> {
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
	
	public int intersection (HashSet<T> set1, HashSet<T> set2) {
		Set<T> a;
		Set<T> b;
		int counter = 0;
		if(set1 == null || set2 == null)
			return 0;
		if (set1.size() <= set2.size()) {
			a = set1;
			b = set2; 
		} else {
			a = set2;
			b = set1;
		}
		for (T e : a) {
			if (b.contains(e)) {
				counter++;
			} 
		}
		return counter;
	}
	
}
