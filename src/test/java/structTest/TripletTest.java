package structTest;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import topkgraphpattern.Triplet;

public class TripletTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2, "u");
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3, "u");
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); //b-a-c wedge
		StreamEdge edgeC = new StreamEdge("b",2, "c",3, "u");
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB,edgeC); // a-b-c triangle
		//assertEquals(false, t.equals(t1));

		//System.out.println(t.hashCode() + " " + t1.hashCode());
		
		HashSet<Triplet> map = new HashSet<Triplet>();
		map.add(t);
		map.add(t1);
		System.out.println("----tst finished----");
	}
	
	@Test
	public void anotherTest() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2, "u");
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3, "u");
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); // b-a-c
		StreamEdge edgeC = new StreamEdge("b",2, "c",3, "C");
		Triplet t1 = new Triplet(a,b,c,edgeA,edgeC); // a-b-c
		//assertEquals(false, t.equals(t1));
		//System.out.println(t.hashCode() + " " + t1.hashCode());
		HashSet<Triplet> map = new HashSet<Triplet>();
		map.add(t);
		map.add(t1);
		
		System.out.println("----tst finished----");

		
		
	}

}
