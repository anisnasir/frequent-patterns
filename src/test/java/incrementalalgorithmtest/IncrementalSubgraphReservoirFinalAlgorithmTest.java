package incrementalalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;

import incremental.IncrementalSubgraphReservoirAlgorithm;
import incremental.IncrementalSubgraphReservoirFinalAlgorithm;
import incremental.IncrementalSubgraphReservoirImprovedAlgorithm;
import input.StreamEdge;

public class IncrementalSubgraphReservoirFinalAlgorithmTest {
	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		System.out.println(topk.getFrequentPatterns());
		topk.addEdge(b);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);

		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
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
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		StreamEdge d = new StreamEdge("b", 2, "d", 4);
		StreamEdge e = new StreamEdge("c", 3, "d", 4);
		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
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
