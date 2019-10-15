package fullydynamic;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;

public class FullyDynamicSubgraphReservoirFinalAlgorithmFourNodeTest {

	@Test
	public void singleLineAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		FullyDynamicSubgraphReservoirFourNode topk = new FullyDynamicSubgraphReservoirFourNode(10, 10);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void circleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		StreamEdge d = new StreamEdge(150, 4, 120, 1);
		FullyDynamicSubgraphReservoirFourNode topk = new FullyDynamicSubgraphReservoirFourNode(10, 10);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void tailedTriangleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(120, 1, 150, 4);
		StreamEdge d = new StreamEdge(130, 2, 140, 3);
		FullyDynamicSubgraphReservoirFourNode topk = new FullyDynamicSubgraphReservoirFourNode(10, 10);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void starAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(120, 1, 150, 4);
		FullyDynamicSubgraphReservoirFourNode topk = new FullyDynamicSubgraphReservoirFourNode(10, 10);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void quasiCliqueAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(120, 1, 150, 4);
		StreamEdge d = new StreamEdge(130, 2, 140, 3);
		StreamEdge e = new StreamEdge(130, 2, 150, 4);
		FullyDynamicSubgraphReservoirFourNode topk = new FullyDynamicSubgraphReservoirFourNode(10, 10);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
}
