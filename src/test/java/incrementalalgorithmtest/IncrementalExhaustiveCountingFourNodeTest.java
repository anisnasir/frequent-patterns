package incrementalalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Test;
import incrementaltopkgraphpattern.IncrementalExhaustiveCounting;
import incrementaltopkgraphpattern.IncrementalExhaustiveCountingFourNode;
import input.StreamEdge;

public class IncrementalExhaustiveCountingFourNodeTest {

	@Test
	public void singleLineAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("b", 2, "c", 3, "u");
		StreamEdge c = new StreamEdge("c", 3, "d", 4, "u");
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		assertEquals(1, topk.getFrequentPatterns().size());

	}
	@Test
	public void CircleAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("b", 2, "c", 3, "u");
		StreamEdge c = new StreamEdge("c", 3, "d", 4, "u");
		StreamEdge d = new StreamEdge("d", 4, "a", 1, "u");
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		System.out.println(topk.getFrequentPatterns());
		topk.addEdge(d);
		System.out.println(topk.getFrequentPatterns());
		assertEquals(1, topk.getFrequentPatterns().size());
	}
}
