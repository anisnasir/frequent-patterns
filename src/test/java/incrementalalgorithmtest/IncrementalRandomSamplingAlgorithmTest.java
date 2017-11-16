package incrementalalgorithmtest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import incrementaltopkgraphpattern.IncrementalRandomSamplingAlgorithm;
import input.StreamEdge;

public class IncrementalRandomSamplingAlgorithmTest {

	int k;
	double p;
	
	@Before
	public void initialize() { 
		k = 10;
		p = 1.0;
	}
	@Test
	public void singleWedgeAddition() {
		StreamEdge a = new StreamEdge("a", 1, "b", 2, "u");
		StreamEdge b = new StreamEdge("a", 1, "c", 3, "u");
		
		IncrementalRandomSamplingAlgorithm topk = new IncrementalRandomSamplingAlgorithm(p, k);
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
		IncrementalRandomSamplingAlgorithm topk = new IncrementalRandomSamplingAlgorithm(p, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		
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
		IncrementalRandomSamplingAlgorithm topk = new IncrementalRandomSamplingAlgorithm(p, size);
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(4, topk.getFrequentPatterns().size());
		//System.out.println(topk.getFrequentPatterns());
	
	}
	

}
