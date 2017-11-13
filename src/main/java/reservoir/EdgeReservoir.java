package reservoir;

import struct.MapArray;
import struct.NodeMap;

public class EdgeReservoir<T> implements Reservoir<T>{
	MapArray<T> list;
	public EdgeReservoir() {
		this.list = new MapArray<T>();
	}
	public boolean add(T a) {
		return list.add(a);
	}
	public boolean remove(T a) {
		return list.remove(a);
	}
	public boolean contains(T a) {
		return list.contains(a);
	}
	public T getRandom() {
		return list.getRandom();
	}
	public int getSize() {
		return list.size();
	}
}
