package fullydynamicalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;

import fullydynamictopkgraphpattern.FullyDynamicExhaustiveCounting;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirAlgorithm;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirFinalAlgorithm;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirImprovedFirstAlgorithm;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirImprovedSecondAlgorithm;
import input.StreamEdge;

public class FullyDynamicSubgraphReservoirFinalAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirFinalAlgorithm topk = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirFinalAlgorithm topk = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);

		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
		topk.removeEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		StreamEdge d = new StreamEdge("b", 2, "d", 4, "u");
		StreamEdge e = new StreamEdge("c", 3, "d", 4, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirFinalAlgorithm topk = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);


		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
		topk.addEdge(c);

		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
		topk.addEdge(d);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(3, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
		topk.addEdge(e);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(4, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
		
	}
	
	@Test
	public void removeTriangle() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirFinalAlgorithm topk = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		System.out.println(topk.getFrequentPatterns());
		
	}

	@Test
	public void wedgeTypeTest() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirFinalAlgorithm topk = new FullyDynamicSubgraphReservoirFinalAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		System.out.println(topk.getFrequentPatterns());
	}

}
