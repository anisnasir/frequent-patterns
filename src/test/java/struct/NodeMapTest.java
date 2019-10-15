package struct;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;

public class NodeMapTest {

	@Test
	public void test() {
		NodeMap nodeMap = new NodeMap();
		LabeledNode n1 = new LabeledNode(120, 1);
		LabeledNode n2 = new LabeledNode(130, 2);
		LabeledNode n3 = new LabeledNode(140, 3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2); //ab
		
		nodeMap.addEdge(n1, n2);
		assertEquals(1, nodeMap.getNeighbors(n1).size());
		assertEquals(1, nodeMap.getNodeNeighbors(n1).size());
		assertEquals(0, nodeMap.getNeighbors(n3).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n3).size());
		
		nodeMap.removeEdge(n1, n2);
		assertEquals(0, nodeMap.getNeighbors(n1).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n1).size());
		assertEquals(0, nodeMap.getNeighbors(n3).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n3).size());
	}

}
