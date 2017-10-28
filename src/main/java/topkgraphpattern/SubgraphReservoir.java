package topkgraphpattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;

import gnu.trove.map.hash.THashMap;

/**
 * SubgraphReservoir class that implements the interfact Reservoir
 * The class uses two data structures map and arrayList
 * map supports contains operation in O(1) time 
 * array allows get random in constant size; 
 * * @author Anis
 *
 * @param <T>
 */
public class SubgraphReservoir<T> implements Reservoir<T>{
	private MapArray<T> list;


	public SubgraphReservoir() {
		list = new MapArray<T>();
	}

	public boolean add(T value) {
		return list.add(value);
	}

	public boolean contains(T value) {
		return list.contains(value);
	}

	public T getRandom() {
		return list.getRandom();
	}

	public T deleteRandom() {
		return list.deleteRandom();
	}

	public boolean remove(T value) {
		return list.remove(value);
	}

	public int size() {
		return list.size();
	}
}


