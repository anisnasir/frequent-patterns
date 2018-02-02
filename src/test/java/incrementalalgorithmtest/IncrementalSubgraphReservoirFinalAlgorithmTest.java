package incrementalalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;

import incrementaltopkgraphpattern.IncrementalSubgraphReservoirAlgorithm;
import incrementaltopkgraphpattern.IncrementalSubgraphReservoirFinalAlgorithm;
import incrementaltopkgraphpattern.IncrementalSubgraphReservoirImprovedAlgorithm;
import input.StreamEdge;

public class IncrementalSubgraphReservoirFinalAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		System.out.println(topk.getFrequentPatterns());
		topk.addEdge(b);
		System.out.println(topk.getFrequentPatterns());
		//assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");

		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		//assertEquals(1, topk.getFrequentPatterns().size());

		System.out.println(topk.getFrequentPatterns());
		
		topk.addEdge(c);
		
		//assertEquals(1, topk.getFrequentPatterns().size());

		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		StreamEdge d = new StreamEdge("b", 2, "d", 4, "u");
		StreamEdge e = new StreamEdge("c", 3, "d", 4, "u");
		int size = 10;
		IncrementalSubgraphReservoirFinalAlgorithm topk = new IncrementalSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	
	}
}
