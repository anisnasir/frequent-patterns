package graphpattern;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.Quadriplet;

public class FourNodeGraphPatternTest {

	@Test
	public void testSamePatterns() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		StreamEdge edgeC = new StreamEdge("a",1, "d", 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		assertTrue(quadriplet.isQuadriplet());
		FourNodeGraphPattern pattern1 = new FourNodeGraphPattern(quadriplet);
		FourNodeGraphPattern pattern2 = new FourNodeGraphPattern(quadriplet);
		assertTrue(pattern1.equals(pattern2));
	}
	
	@Test
	public void testDifferentPatterns() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		StreamEdge edgeC = new StreamEdge("a",1, "d", 4);
		StreamEdge edgeD = new StreamEdge("b",2, "c", 3);
		StreamEdge edgeE = new StreamEdge("c",3, "d", 4);
		StreamEdge edgeF = new StreamEdge("b",2, "d", 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		assertTrue(quadriplet.isQuadriplet());
		//star
		FourNodeGraphPattern pattern1 = new FourNodeGraphPattern(quadriplet);
		//tailed triangle
		quadriplet.addEdge(edgeD);
		FourNodeGraphPattern pattern2 = new FourNodeGraphPattern(quadriplet);
		assertFalse(pattern1.equals(pattern2));
		//quasi clique
		quadriplet.addEdge(edgeE);
		FourNodeGraphPattern pattern3 = new FourNodeGraphPattern(quadriplet);
		assertFalse(pattern1.equals(pattern3));
		assertFalse(pattern2.equals(pattern3));
		//clique
		quadriplet.addEdge(edgeF);
		FourNodeGraphPattern pattern4 = new FourNodeGraphPattern(quadriplet);
		assertFalse(pattern1.equals(pattern4));
		assertFalse(pattern2.equals(pattern4));
		assertFalse(pattern3.equals(pattern4));
		
		//circle
		quadriplet.removeEdge(edgeC);
		quadriplet.removeEdge(edgeD);
		FourNodeGraphPattern pattern5 = new FourNodeGraphPattern(quadriplet);
		assertFalse(pattern1.equals(pattern5));
		assertFalse(pattern2.equals(pattern5));
		assertFalse(pattern3.equals(pattern5));
		assertFalse(pattern4.equals(pattern5));
		
		quadriplet.removeEdge(edgeA);
		FourNodeGraphPattern pattern6 = new FourNodeGraphPattern(quadriplet);
		assertFalse(pattern1.equals(pattern6));
		assertFalse(pattern2.equals(pattern6));
		assertFalse(pattern3.equals(pattern6));
		assertFalse(pattern4.equals(pattern6));
		assertFalse(pattern5.equals(pattern6));
		
		
	}

}
