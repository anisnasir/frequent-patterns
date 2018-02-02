package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeBottomK;

public class NodeBottomKTest {

	@Test
	public void test() {
		LabeledNode src = new LabeledNode("s1", 1);
		LabeledNode dstA = new LabeledNode("s2", 2);
		LabeledNode dstB = new LabeledNode("s3", 3);
		StreamEdge edgeA = new StreamEdge("s1",1,  "s2", 2, "u");
		StreamEdge edgeB = new StreamEdge("s1",1,  "s3", 3, "u");
		
		NodeBottomK map = new NodeBottomK();
		map.addEdge(src, dstA, edgeA);
		map.addEdge(src, dstB, edgeB);
		
		System.out.println(map.getUnionSize(src, dstA));
		map.removeEdge(src, dstA, edgeA);
			
	
	}

}
