package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import topkgraphpattern.GraphPattern;
import topkgraphpattern.Triplet;

public class PatternTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2, "A");
		StreamEdge edgeB = new StreamEdge("b",2, "c", 3, "B");
		Triplet t = new Triplet(a,b,c,edgeA, edgeB);
		StreamEdge edgeC = new StreamEdge("b",2, "c",3, "C");
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB,edgeC);
		
		GraphPattern p1 = new GraphPattern (t);
		GraphPattern p2 = new GraphPattern(t1);
		System.out.println(p1);
		assertEquals(false, (p1.hashCode() == p2.hashCode()));
	}

}
