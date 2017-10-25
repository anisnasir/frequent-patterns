package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import struct.LabeledNeighbor;
import struct.LabeledNode;

public class LabeledNeighborTest {

	@Test
	public void test() {
		LabeledNode dst = new LabeledNode("s1", 1);
		String edgeLabel = "undirected";
		LabeledNeighbor neighbor = new LabeledNeighbor(dst,edgeLabel);
		
		LabeledNode dst1 = new LabeledNode("s1", 1);
		String edgeLabel1 = "undirected";
		LabeledNeighbor neighbor1 = new LabeledNeighbor(dst1,edgeLabel1);
		
		assertEquals(true, neighbor.equals(neighbor1));
		
	}

}
