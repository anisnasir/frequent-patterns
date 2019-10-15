package incremental;

import static org.junit.Assert.*;

import org.junit.Test;

import incremental.IncrementalSubgraphReservoirThreeNode3;
import input.StreamEdge;

public class IncrementalSubgraphReservoirImprovedAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		int size = 10;
		IncrementalSubgraphReservoirThreeNode3 topk = new IncrementalSubgraphReservoirThreeNode3(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	/*@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2, "u");
		StreamEdge b = new StreamEdge(120, 1, 140, 3, "u");
		StreamEdge c = new StreamEdge(130, 2, 140, 3, "u");

		int size = 10;
		IncrementalSubgraphReservoirImprovedAlgorithm topk = new IncrementalSubgraphReservoirImprovedAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());

		System.out.println(topk.getFrequentPatterns());
		
		topk.addEdge(c);
		
		assertEquals(1, topk.getFrequentPatterns().size());

		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2, "u");
		StreamEdge b = new StreamEdge(120, 1, 140, 3, "u");
		StreamEdge c = new StreamEdge(130, 2, 140, 3, "u");
		StreamEdge d = new StreamEdge(130, 2, "d", 4, "u");
		StreamEdge e = new StreamEdge(140, 3, "d", 4, "u");
		int size = 10;
		IncrementalSubgraphReservoirImprovedAlgorithm topk = new IncrementalSubgraphReservoirImprovedAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	
	}*/
}
