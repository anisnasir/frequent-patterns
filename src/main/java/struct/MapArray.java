package struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

import gnu.trove.map.hash.THashMap;


/**
 * Data structure that implements add, contains, deleteRandom, remove in
 * constant time.
 * 
 * @author anisnasir
 *
 * @param <T>
 */
public class MapArray<T> {
	private THashMap<T, Integer> valueIndexes;
	private ArrayList<T> values;
	private Random rand;

	public MapArray() {
		valueIndexes = new THashMap<>();
		values = new ArrayList<>();
		rand = new Random(System.currentTimeMillis());
	}

	public boolean isEmpty() {
		return (values.size() == 0);
	}

	public boolean add(T value) {
		if (!contains(value)) {
			int lastIndex = values.size();
			valueIndexes.put(value, lastIndex);
			values.add(value);
			return true;
		} else
			return false;
	}

	public boolean contains(T value) {
		if (value == null) {
			throw new NullPointerException();
		}
		return valueIndexes.containsKey(value);
	}

	public T getRandom() {
		if (valueIndexes.isEmpty()) {
			throw new NoSuchElementException();
		}
		int randomIndex = rand.nextInt(values.size());
		return values.get(randomIndex);
	}

	public T deleteRandom() {
		if (valueIndexes.isEmpty()) {
			throw new NoSuchElementException();
		}
		int randomIndex = rand.nextInt(values.size());
		return deleteValue(randomIndex);
	}

	public boolean remove(T value) {
		if (!contains(value)) {
			throw new NoSuchElementException();
		}
		int index = valueIndexes.get(value);
		deleteValue(index);
		return true;
	}

	private T deleteValue(int currentIndex) {
		// remove the current element in the array, swap with the last,
		// and update the new index value in the map.
		T currentValue = values.get(currentIndex);
		int lastIndex = values.size() - 1;
		T lastVal = values.get(lastIndex);
		Collections.swap(values, currentIndex, lastIndex);
		// removing the last element is constant
		values.remove(lastIndex);
		valueIndexes.put(lastVal, currentIndex);
		valueIndexes.remove(currentValue);
		return currentValue;
	}

	public int size() {
		if (values.size() != valueIndexes.size()) {
			// should never happen.
			throw new IllegalStateException();
		}
		return values.size();
	}

}
