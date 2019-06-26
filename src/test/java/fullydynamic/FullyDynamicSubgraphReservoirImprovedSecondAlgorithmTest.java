package fullydynamic;

import static org.junit.Assert.*;

import org.junit.Test;

import fullydynamic.FullyDynamicExhaustiveCountingThreeNode;
import fullydynamic.FullyDynamicSubgraphReservoirThreeNode;
import fullydynamic.FullyDynamicSubgraphReservoirThreeNode3;
import fullydynamic.FullyDynamicSubgraphReservoirThreeNode4;
import input.StreamEdge;

public class FullyDynamicSubgraphReservoirImprovedSecondAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode4 topk = new FullyDynamicSubgraphReservoirThreeNode4(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);

		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode4 topk = new FullyDynamicSubgraphReservoirThreeNode4(size, size);
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
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		StreamEdge d = new StreamEdge("b", 2, "d", 4);
		StreamEdge e = new StreamEdge("c", 3, "d", 4);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode4 topk = new FullyDynamicSubgraphReservoirThreeNode4(size, size);
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
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode4 topk = new FullyDynamicSubgraphReservoirThreeNode4(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		//System.out.println(topk.getFrequentPatterns());
	}

	@Test
	public void wedgeTypeTest() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode4 topk = new FullyDynamicSubgraphReservoirThreeNode4(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		//System.out.println(topk.getFrequentPatterns());
	}

}
