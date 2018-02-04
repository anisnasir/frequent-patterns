package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;

public class NodeMapTest {

	@Test
	public void test() {
		NodeMap nodeMap = new NodeMap();
		LabeledNode n1 = new LabeledNode("s1", 1);
		LabeledNode n2 = new LabeledNode("s2", 2);
		LabeledNode n3 = new LabeledNode("s3", 3);
		StreamEdge edgeA = new StreamEdge("s1",1,  "s2", 2, "u"); //ab
		
		nodeMap.addEdge(n1, n2, edgeA);
		assertEquals(1, nodeMap.getNeighbors(n1).size());
		assertEquals(1, nodeMap.getNodeNeighbors(n1).size());
		assertEquals(0, nodeMap.getNeighbors(n3).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n3).size());
		
		nodeMap.removeEdge(n1, n2, edgeA);
		assertEquals(0, nodeMap.getNeighbors(n1).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n1).size());
		assertEquals(0, nodeMap.getNeighbors(n3).size());
		assertEquals(0, nodeMap.getNodeNeighbors(n3).size());
	}

}
