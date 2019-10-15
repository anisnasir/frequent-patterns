package struct;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.Quadriplet;
import topkgraphpattern.SubgraphType;

public class QuadripletTest {

	@Test
	public void validTest() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		LabeledNode d = new LabeledNode(150,4);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		StreamEdge edgeC = new StreamEdge(120,1, 150, 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		assertTrue(quadriplet.isQuadriplet());
	}
	
	@Test
	public void invalidTest1() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		LabeledNode d = new LabeledNode(150,4);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeC = new StreamEdge(140,3, 150, 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeC);
		assertFalse(quadriplet.isQuadriplet());
	}
	
	@Test
	public void invalidTest2() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		assertFalse(quadriplet.isQuadriplet());
	}
	
	@Test
	public void dfsCodeTest() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		LabeledNode d = new LabeledNode(150,4);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		StreamEdge edgeC = new StreamEdge(120,1, 150, 3);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		
		assertEquals(-1442932315, quadriplet.hashCode());
	}
	
	@Test
	public void getTypeTest() {
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		StreamEdge edgeC = new StreamEdge(120,1, 150, 4);
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edgeA);
		quadriplet.addEdge(edgeB);
		quadriplet.addEdge(edgeC);
		
		assertEquals(SubgraphType.STAR, quadriplet.getType());
	}

}
