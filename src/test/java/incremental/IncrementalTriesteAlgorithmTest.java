package incremental;

import static org.junit.Assert.*;

import org.junit.Test;

import incremental.IncrementalEdgeReservoirThreeNode;
import input.StreamEdge;

public class IncrementalTriesteAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		int size = 10;
		IncrementalEdgeReservoirThreeNode topk = new IncrementalEdgeReservoirThreeNode(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);

		int size = 10;
		IncrementalEdgeReservoirThreeNode topk = new IncrementalEdgeReservoirThreeNode(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		StreamEdge d = new StreamEdge(130, 2, 150, 4);
		StreamEdge e = new StreamEdge(140, 3, 150, 4);
		int size = 10;
		IncrementalEdgeReservoirThreeNode topk = new IncrementalEdgeReservoirThreeNode(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());
		//System.out.println(topk.getFrequentPatterns());
	
	}
	

}
