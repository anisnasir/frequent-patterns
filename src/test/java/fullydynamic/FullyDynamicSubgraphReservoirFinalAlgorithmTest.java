package fullydynamic;

import static org.junit.Assert.*;

import org.junit.Test;

import fullydynamic.FullyDynamicSubgraphReservoirThreeNode2;
import input.StreamEdge;

public class FullyDynamicSubgraphReservoirFinalAlgorithmTest {

	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		assertEquals(1, topk.getFrequentPatterns().size());
		System.out.println(topk.getFrequentPatterns());
	}
	@Test
	public void triangleAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);

		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void triangleDeletion() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);

		topk.removeEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void wedgeDeletion() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);

		topk.removeEdge(c);
		topk.removeEdge(b);

		System.out.println(topk.c1 + " "  + topk.c2);

	}
	@Test
	public void multipleTripletAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		StreamEdge d = new StreamEdge(130, 2, 150, 4);
		StreamEdge e = new StreamEdge(140, 3, 150, 4);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);

		assertEquals(1, topk.getFrequentPatterns().size());
		topk.addEdge(c);

		assertEquals(1, topk.getFrequentPatterns().size());
		topk.addEdge(d);
		assertEquals(3, topk.getFrequentPatterns().size());
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());		
	}
	
	@Test
	public void removeTriangle() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		System.out.println(topk.getFrequentPatterns());
		
	}

	@Test
	public void wedgeTypeTest() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		int size = 10;
		FullyDynamicSubgraphReservoirThreeNode2 topk = new FullyDynamicSubgraphReservoirThreeNode2(size, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.removeEdge(c);
		//topk.removeEdge(a);
		System.out.println(topk.getFrequentPatterns());
	}

}
