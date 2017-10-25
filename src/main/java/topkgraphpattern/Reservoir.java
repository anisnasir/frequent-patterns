package topkgraphpattern;

public interface Reservoir<T> {
	public boolean add(T a);
	public boolean remove(T a);
	public T getRandom();
	public boolean contains(T a);
	
}
