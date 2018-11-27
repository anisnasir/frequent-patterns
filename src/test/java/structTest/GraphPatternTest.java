package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import struct.LabeledNode;
import struct.Triplet;

public class GraphPatternTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2); //ab
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3); //ac
		StreamEdge edgeC = new StreamEdge("b",2 , "c" , 3); //bc
		
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB); // b-a-c  
		Triplet t2 = new Triplet(a,b,c,edgeA,edgeC); // a-b-c   
		Triplet t3 = new Triplet(a,b,c,edgeB,edgeC); // c-a-b  
		Triplet t4 = new Triplet(a,b,c, edgeA,edgeB,edgeC); //a-b-c-a
		
		ThreeNodeGraphPattern p1 = new ThreeNodeGraphPattern (t1);
		ThreeNodeGraphPattern p2 = new ThreeNodeGraphPattern(t2);
		ThreeNodeGraphPattern p3 = new ThreeNodeGraphPattern(t3);
		ThreeNodeGraphPattern p4 = new ThreeNodeGraphPattern(t4);
		
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		
		assertEquals(false, p1.equals(p2));
		assertEquals(false, p2.equals(p3));
		assertEquals(false, p1.equals(p3));
		assertEquals(false, p4.equals(p1));
		assertEquals(false, p4.equals(p2));
		assertEquals(false, p4.equals(p3));
		
		assertEquals(false, (p1.hashCode() == p2.hashCode()));
		assertEquals(false, (p2.hashCode() == p3.hashCode()));
		assertEquals(false, (p1.hashCode() == p3.hashCode()));
		assertEquals(false, (p4.hashCode() == p1.hashCode()));
		assertEquals(false, (p4.hashCode() == p2.hashCode()));
		assertEquals(false, (p4.hashCode() == p3.hashCode()));
	}
	
	@Test
	public void sameLabelTest() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",1);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 1); //ab
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3); //ac
		StreamEdge edgeC = new StreamEdge("b",1 , "c" , 3); //bc
		
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB); // b-a-c  a - b b - c 
		Triplet t2 = new Triplet(a,b,c,edgeA,edgeC); // a-b-c   
		Triplet t3 = new Triplet(a,b,c,edgeB,edgeC); // c-a-b  
		Triplet t4 = new Triplet(a,b,c, edgeA,edgeB,edgeC); //a-b-c-a
		Triplet t5 = new Triplet(a,b,c,edgeC,edgeB); // c-a-b  
		
		ThreeNodeGraphPattern p1 = new ThreeNodeGraphPattern (t1);
		ThreeNodeGraphPattern p2 = new ThreeNodeGraphPattern(t2);
		ThreeNodeGraphPattern p3 = new ThreeNodeGraphPattern(t3);
		ThreeNodeGraphPattern p4 = new ThreeNodeGraphPattern(t4);
		ThreeNodeGraphPattern p5 = new ThreeNodeGraphPattern(t5);
		
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		
		assertEquals(true, p1.equals(p2));
		assertEquals(false, p2.equals(p3));
		assertEquals(false, p1.equals(p3));
		assertEquals(false, p4.equals(p1));
		assertEquals(false, p4.equals(p2));
		assertEquals(false, p4.equals(p3));
		assertEquals(true, p3.equals(p5));
		
		assertEquals(true, (p1.hashCode() == p2.hashCode()));
		assertEquals(false, (p2.hashCode() == p3.hashCode()));
		assertEquals(false, (p1.hashCode() == p3.hashCode()));
		assertEquals(false, (p4.hashCode() == p1.hashCode()));
		assertEquals(false, (p4.hashCode() == p2.hashCode()));
		assertEquals(false, (p4.hashCode() == p3.hashCode()));
	}

}
