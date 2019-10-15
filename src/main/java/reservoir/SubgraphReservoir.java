package reservoir;

import struct.MapArray;

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

	@Override
	public boolean add(T value) {
		return list.add(value);
	}

	@Override
	public boolean contains(T value) {
		return list.contains(value);
	}

	@Override
	public T getRandom() {
		return list.getRandom();
	}

	public T deleteRandom() {
		return list.deleteRandom();
	}

	@Override
	public boolean remove(T value) {
		return list.remove(value);
	}

	public int size() {
		return list.size();
	}
}


