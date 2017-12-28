package fullydynamicalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;

import fullydynamictopkgraphpattern.FullyDynamicExhaustiveCounting;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirAlgorithm;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirImprovedFirstAlgorithm;
import fullydynamictopkgraphpattern.FullyDynamicSubgraphReservoirImprovedSecondAlgorithm;
import input.StreamEdge;

public class FullyDynamicSubgraphReservoirImprovedSecondAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirImprovedSecondAlgorithm topk = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");

		int size = 10;
		FullyDynamicSubgraphReservoirImprovedSecondAlgorithm topk = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());

		topk.addEdge(c);

		assertEquals(1, topk.getFrequentPatterns().size());

		topk.removeEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

		//System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		StreamEdge d = new StreamEdge("b", 2, "d", 4, "u");
		StreamEdge e = new StreamEdge("c", 3, "d", 4, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirImprovedSecondAlgorithm topk = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());
		//System.out.println(topk.getFrequentPatterns());

	}
	@Test
	public void removeTriangle() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirImprovedSecondAlgorithm topk = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		//System.out.println(topk.getFrequentPatterns());
	}

	@Test
	public void wedgeTypeTest() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		StreamEdge c = new StreamEdge("b", 2, "c", 3, "u");
		int size = 10;
		FullyDynamicSubgraphReservoirImprovedSecondAlgorithm topk = new FullyDynamicSubgraphReservoirImprovedSecondAlgorithm(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		//System.out.println(topk.getFrequentPatterns());
	}

}
