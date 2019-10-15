package incremental;

import static org.junit.Assert.*;

import org.junit.Test;

import incremental.IncrementalSubgraphReservoirThreeNode2;
import input.StreamEdge;

public class IncrementalSubgraphReservoirFinalAlgorithmTest {
	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		int size = 10;
		IncrementalSubgraphReservoirThreeNode2 topk = new IncrementalSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		System.out.println(topk.getFrequentPatterns());
		topk.addEdge(b);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);

		int size = 10;
		IncrementalSubgraphReservoirThreeNode2 topk = new IncrementalSubgraphReservoirThreeNode2(size, size);
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
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		StreamEdge d = new StreamEdge(130, 2, 150, 4);
		StreamEdge e = new StreamEdge(140, 3, 150, 4);
		int size = 10;
		IncrementalSubgraphReservoirThreeNode2 topk = new IncrementalSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		//System.out.println(topk.getFrequentPatterns());
		topk.addEdge(b);
		//System.out.println(topk.getFrequentPatterns());
		topk.addEdge(c);
		//System.out.println(topk.getFrequentPatterns());
		topk.addEdge(d);
		//System.out.println(topk.getFrequentPatterns());
		topk.addEdge(e);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(4, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	
	}
}
