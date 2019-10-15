package struct;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.Triplet;

public class TripletTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); //b-a-c wedge
		StreamEdge edgeC = new StreamEdge(130,2, 140,3);
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
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); // b-a-c
		StreamEdge edgeC = new StreamEdge(130,2, 140,3);
		Triplet t1 = new Triplet(a,b,c,edgeA,edgeC); // a-b-c
		assertEquals(false, t.equals(t1));
		//System.out.println(t.hashCode() + " " + t1.hashCode());
		HashSet<Triplet> map = new HashSet<Triplet>();
		map.add(t);
		map.add(t1);
		
	
	}
	@Test
	public void threeWedgeTest() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2); //ab
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3); //ac
		StreamEdge edgeC = new StreamEdge(130,2 , 140 , 3); //bc
		
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
