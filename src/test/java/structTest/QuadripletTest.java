package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.SubgraphType;

public class QuadripletTest {

	@Test
	public void validTest() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		StreamEdge edgeC = new StreamEdge("a",1, "d", 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		assertTrue(quadriplet.isQuadriplet());
	}
	
	@Test
	public void invalidTest1() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeC = new StreamEdge("c",3, "d", 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeC);
		assertFalse(quadriplet.isQuadriplet());
	}
	
	@Test
	public void invalidTest2() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		assertFalse(quadriplet.isQuadriplet());
	}
	
	@Test
	public void dfsCodeTest() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		StreamEdge edgeC = new StreamEdge("a",1, "d", 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		
		assertEquals(1818592971, quadriplet.hashCode());
	}
	
	@Test
	public void getTypeTest() {
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		StreamEdge edgeC = new StreamEdge("a",1, "d", 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		
		assertEquals(SubgraphType.STAR, quadriplet.getType());
	}

}
