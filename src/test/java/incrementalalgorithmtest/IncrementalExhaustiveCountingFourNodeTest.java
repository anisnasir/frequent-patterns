package incrementalalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;

import incremental.IncrementalExhaustiveCounting;
import incremental.IncrementalExhaustiveCountingFourNode;
import input.StreamEdge;

public class IncrementalExhaustiveCountingFourNodeTest {

	@Test
	public void singleLineAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("c", 3, "d", 4);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void circleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("c", 3, "d", 4);
		StreamEdge d = new StreamEdge("d", 4, "a", 1);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void tailedTriangleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("a", 1, "d", 4);
		StreamEdge d = new StreamEdge("b", 2, "c", 3);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void starAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("a", 1, "d", 4);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void quasiCliqueAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("a", 1, "d", 4);
		StreamEdge d = new StreamEdge("b", 2, "c", 3);
		StreamEdge e = new StreamEdge("b", 2, "d", 4);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
}
