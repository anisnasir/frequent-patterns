package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservoirSampling<T>
{
	public ReservoirSampling() { 
		
	}
	// A function to randomly select k items from stream[0..n-1].
	public List<T> selectKItems(List<T> stream, int k) {
		List<T> reservoir = new ArrayList<T>();
		int i;   // index for elements in stream[]
		int n = stream.size();
		
		// reservoir[] is the output array. Initialize it with
		// first k elements from stream[]
		for (i = 0; i < k; i++)
			reservoir.add(stream.get(i));

		Random r = new Random();

		// Iterate from the (k+1)th element to nth element
		for (; i < n; i++)
		{
			// Pick a random index from 0 to i.
			int j = r.nextInt(i + 1);

			// If the randomly  picked index is smaller than k,
			// then replace the element present at the index
			// with new element from stream
			if(j < k)
				reservoir.set(j, stream.get(i));         
		}

		return reservoir;
	}
}
//This code is contributed by Sumit Ghosh