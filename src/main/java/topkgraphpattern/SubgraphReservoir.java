package topkgraphpattern;

import java.util.ArrayList;
import java.util.Collections;
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
	ArrayList<T> arr;   // A resizable array

	// A hash where keys are array elements and vlaues are
	// indexes in arr[]
	THashMap<T, Integer>  hash;

	// Constructor (creates arr[] and hash)
	public SubgraphReservoir()
	{
		arr = new ArrayList<T>();
		hash = new THashMap<T, Integer>();
	}

	// A Theta(1) function to add an element to MyDS
	// data structure
	public boolean add(T x)
	{
		// If ekement is already present, then noting to do
		if (hash.get(x) != null) {
			return false;
		}

		// Else put element at the end of arr[]
		int s = arr.size();
		arr.add(x);

		// And put in hash also
		hash.put(x, s);
		return true;
	}

	// A Theta(1) function to remove an element from MyDS
	// data structure
	public boolean remove(T x)
	{
		// Check if element is present
		Integer index = hash.get(x);
		if (index == null)
			return false;

		// If present, then remove element from hash
		hash.remove(x);

		// Swap element with last element so that remove from
		// arr[] can be done in O(1) time
		int size = arr.size();
		T last = arr.get(size-1);
		if(index > (size-1)) {
			System.out.println("error: index " + index + " is greater than size "+ (size-1));
			System.exit(1);
		}
			
		Collections.swap(arr, index,  size-1);

		// Remove last element (This is O(1))
		arr.remove(size-1);

		// Update hash table for new index of last element
		hash.put(last, index);
		return true;
	}

	// Returns a random element from MyDS
	public T getRandom()
	{
		// Find a random index from 0 to size - 1
		Random rand = new Random();  // Choose a different seed
		int index = rand.nextInt(arr.size());

		// Return element at randomly picked index
		return arr.get(index);
	}

	// Returns index of element if element is present, otherwise null
	public boolean contains(T x)
	{
		return hash.contains(x);
	}
	
	public int size() {
		return arr.size();
	}
}


