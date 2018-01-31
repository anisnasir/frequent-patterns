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

	public HashSet<String> intersectionSet (Set<String> set1, Set<String> set2) {
		if(set1 == null)
			return new HashSet<String>();
		else if (set2 == null)
			return new HashSet<String>();
		Set<String> a;
		Set<String> b;
		HashSet<String> returnSet = new HashSet<String>();
		if (set1.size() <= set2.size()) {
			a = set1;
			b = set2; 
		} else {
			a = set2;
			b = set1;
		}
		for (String e : a) {
			if (b.contains(e)) {
				returnSet.add(e);
			} 
		}
		return returnSet;
	}

	public int intersection (Set<String> set1, Set<String> set2) {
		Set<String> a;
		Set<String> b;
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
		for (String e : a) {
			if (b.contains(e)) {
				counter++;
			} 
		}
		return counter;
	}

	public int intersection (THashSet<Double> set, THashSet<Double> tHashSet) {
		THashSet<Double> a;
		THashSet<Double> b;
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
		for (Double e : a) {
			if (b.contains(e)) {
				counter++;
			} 
		}
		return counter;
	}

	public THashSet<String> unionSet (THashSet<String> set1, THashSet<String> set2) {
		if(set1 == null)
			return set2;
		else if (set2 == null)
			return set1;
		THashSet<String> returnSet = new THashSet <String>();
		returnSet.addAll(set1);
		returnSet.addAll(set2);
		return returnSet;
	}

}

