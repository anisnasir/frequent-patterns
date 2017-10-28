package topkgraphpattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;

import gnu.trove.map.hash.THashMap;
import struct.LabeledNode;

/**
 * SubgraphReservoir class that implements the interfact Reservoir
 * The class uses two data structures map and arrayList
 * map supports contains operation in O(1) time 
 * array allows get random in constant size; 
 * * @author Anis
 *
 * @param <T>
 */
public class AdvancedSubgraphReservoir<T> implements Reservoir<T>{
	private THashMap<LabeledNode, HashSet<T>> vertexSubgraphMap;
	MapArray<T> list;
	
	public AdvancedSubgraphReservoir() {
		vertexSubgraphMap = new THashMap<LabeledNode, HashSet<T>>();
		list = new MapArray<T>();
		
	}

	public boolean add(T value) {
		if (!contains(value)) {
			Triplet t = (Triplet)value;
			LabeledNode a = t.a;
			LabeledNode b = t.b;
			LabeledNode c = t.c;
			add(a,value);
			add(b,value);
			add(c,value);
			
			list.add(value);
			return true;
		}else 
			return false;
	}
	public void add(LabeledNode a, T value) {
		if(vertexSubgraphMap.contains(a)) {
			HashSet<T> set = vertexSubgraphMap.get(a);
			set.add(value);
			vertexSubgraphMap.put(a, set);
		}else {
			HashSet<T> set = new HashSet<T>();
			set.add(value);
			vertexSubgraphMap.put(a, set);
		}
	}

	public boolean contains(T value) {
		if (value == null) {
			throw new NullPointerException();
		}
		return list.contains(value);
		
	}

	public T getRandom() {
		return list.getRandom();
	}

	public T deleteRandom() {
		return list.deleteRandom();
	}

	public boolean remove(T value) {
		if (!contains(value)) {
			throw new NoSuchElementException();
		}
		if(list.contains(value)) {
			list.remove(value);
			
			Triplet t = (Triplet)value;
			LabeledNode a = t.a;
			LabeledNode b = t.b;
			LabeledNode c = t.c;
			
			remove(a,value);
			remove(b,value);
			remove(c,value);
			
			return true;
		}else {
			return false;
		}
	}

	public void remove(LabeledNode a, T value) {
		if(vertexSubgraphMap.contains(a)) {
			HashSet<T> set = vertexSubgraphMap.get(a);
			set.remove(value);
			vertexSubgraphMap.put(a, set);
		}
	}
	

	public int size() {
		return list.size();
	}
}


