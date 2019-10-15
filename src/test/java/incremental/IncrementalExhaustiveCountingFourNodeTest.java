package incremental;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import incremental.IncrementalExhaustiveCountingFourNode;
import input.StreamEdge;

public class IncrementalExhaustiveCountingFourNodeTest {

	@Test
	public void singleLineAddition() {
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
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
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
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
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
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
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
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
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		topk.addEdge(a);
		topk.addEdge(b);
		topk.addEdge(c);
		topk.addEdge(d);
		topk.addEdge(e);
		assertEquals(1, topk.getFrequentPatterns().size());
	}
	
	@Test
	public void randomPatternCount() {
		List<StreamEdge> list = new ArrayList<StreamEdge>();
		list.add(new StreamEdge(130, 2, 220, 3));
		list.add(new StreamEdge(130, 2, 230, 4));
		list.add(new StreamEdge(130, 2, 240, 5));
		list.add(new StreamEdge(130, 2, 250, 6));
		list.add(new StreamEdge(220, 3, 260, 7));
		list.add(new StreamEdge(220, 3, 270, 8));
		list.add(new StreamEdge(230, 4, 280, 9));
		list.add(new StreamEdge(240, 5, 250, 6));
		list.add(new StreamEdge(240, 5, 290, 10));
		list.add(new StreamEdge(250, 6, 300, 11));
		list.add(new StreamEdge(170, 15, 180, 16));
		list.add(new StreamEdge(170, 15, 140, 12));
		list.add(new StreamEdge(180, 16, 140, 12));
		list.add(new StreamEdge(120, 1, 140, 12));
		list.add(new StreamEdge(190, 17, 150, 13));
		list.add(new StreamEdge(200, 18, 150, 13));
		list.add(new StreamEdge(160, 14, 150, 13));
		list.add(new StreamEdge(120, 1, 150, 13));
		list.add(new StreamEdge(160, 14, 120, 1));
		list.add(new StreamEdge(160, 14, 200, 18));
		list.add(new StreamEdge(160, 14, 210, 19));
		
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		for(StreamEdge streamEdge: list) {
			topk.addEdge(streamEdge);
		}
		
		
		int count = topk.getFrequentPatterns().size();
		topk.addEdge(new StreamEdge(120, 1, 130, 2));
		System.out.println("count: " + count);
		assertEquals(32, topk.getFrequentPatterns().size()-count);
	}
	
	@Test
	public void randomPatternCount2() {
		List<StreamEdge> list = new ArrayList<StreamEdge>();
		list.add(new StreamEdge(130, 1, 140, 2));
		list.add(new StreamEdge(130, 1, 150, 3));
		list.add(new StreamEdge(130, 1, 160, 4));
		list.add(new StreamEdge(140, 2, 150, 3));
		list.add(new StreamEdge(140, 2, 160, 4));
		list.add(new StreamEdge(150, 3, 160, 4));
		
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		for(StreamEdge streamEdge: list) {
			topk.addEdge(streamEdge);
		}
		
		
		int count = topk.getFrequentPatterns().size();
		topk.addEdge(new StreamEdge(120, 0, 130, 1));
		System.out.println("count: " + count);
		assertEquals(3, topk.getFrequentPatterns().size()-count);
	}
	
	@Test
	public void randomPatternCount3() {
		List<StreamEdge> list = new ArrayList<StreamEdge>();
		list.add(new StreamEdge(130, 1, 140, 2));
		list.add(new StreamEdge(130, 1, 150, 3));
		list.add(new StreamEdge(140, 2, 150, 3));
		list.add(new StreamEdge(140, 2, 160, 4));
		list.add(new StreamEdge(150, 3, 160, 4));
		
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		for(StreamEdge streamEdge: list) {
			topk.addEdge(streamEdge);
		}
		
		
		int count = topk.getFrequentPatterns().size();
		topk.addEdge(new StreamEdge(120, 0, 130, 1));
		System.out.println("count: " + count);
		assertEquals(3, topk.getFrequentPatterns().size()-count);
	}
	
	@Test
	public void randomPatternCount4() {
		List<StreamEdge> list = new ArrayList<StreamEdge>();
		list.add(new StreamEdge(130, 1, 140, 2));
		list.add(new StreamEdge(130, 1, 150, 3));
		list.add(new StreamEdge(140, 2, 160, 4));
		list.add(new StreamEdge(150, 3, 160, 4));
		
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		for(StreamEdge streamEdge: list) {
			topk.addEdge(streamEdge);
		}
		
		
		int count = topk.getFrequentPatterns().size();
		topk.addEdge(new StreamEdge(120, 0, 130, 1));
		System.out.println("count: " + count);
		assertEquals(3, topk.getFrequentPatterns().size()-count);
	}
	
	@Test
	public void randomPatternCount5() {
		List<StreamEdge> list = new ArrayList<StreamEdge>();
		list.add(new StreamEdge(130, 1, 150, 3));
		list.add(new StreamEdge(140, 2, 160, 4));
		list.add(new StreamEdge(150, 3, 160, 4));
		
		IncrementalExhaustiveCountingFourNode topk = new IncrementalExhaustiveCountingFourNode();
		for(StreamEdge streamEdge: list) {
			topk.addEdge(streamEdge);
		}
		
		
		int count = topk.getFrequentPatterns().size();
		topk.addEdge(new StreamEdge(120, 0, 130, 1));
		System.out.println("count: " + count);
		assertEquals(1, topk.getFrequentPatterns().size()-count);
	}
	
}
