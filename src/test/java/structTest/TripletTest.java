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
		assertEquals(false, t.equals(t1));

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
		assertEquals(false, t.equals(t1));
		//System.out.println(t.hashCode() + " " + t1.hashCode());
		HashSet<Triplet> map = new HashSet<Triplet>();
		map.add(t);
		map.add(t1);
		
	
	}
	@Test
	public void threeWedgeTest() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2, "u"); //ab
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3, "u"); //ac
		StreamEdge edgeC = new StreamEdge("b",2 , "c" , 3, "u"); //bc
		
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB); // b-a-c
		Triplet t2 = new Triplet(a,b,c,edgeA,edgeC); // a-b-c
		Triplet t3 = new Triplet(a,b,c,edgeB,edgeC); // c-a-b
		Triplet t4 = new Triplet(a,b,c, edgeA,edgeB,edgeC); //a-b-c-a
		
		assertEquals(false, t1.equals(t2));
		assertEquals(false, t2.equals(t3));
		assertEquals(false, t1.equals(t3));
		assertEquals(false, t4.equals(t1));
		assertEquals(false, t4.equals(t2));
		assertEquals(false, t4.equals(t3));
		
		assertEquals(false, (t1.hashCode() == t2.hashCode()));
		assertEquals(false, (t2.hashCode() == t3.hashCode()));
		assertEquals(false, (t1.hashCode() == t3.hashCode()));
		assertEquals(false, (t4.hashCode() == t1.hashCode()));
		assertEquals(false, (t4.hashCode() == t2.hashCode()));
		assertEquals(false, (t4.hashCode() == t3.hashCode()));
	
	
	}

}
