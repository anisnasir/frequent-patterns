package struct;

import org.junit.Test;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeBottomK;

public class NodeBottomKTest {

	@Test
	public void test() {
		LabeledNode src = new LabeledNode(120, 1);
		LabeledNode dstA = new LabeledNode(130, 2);
		LabeledNode dstB = new LabeledNode(140, 3);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1,  140, 3);
		
		NodeBottomK map = new NodeBottomK();
		map.addEdge(src, dstA);
		map.addEdge(src, dstB);
		
		System.out.println(map.getUnionSize(src, dstA));
		map.removeEdge(src, dstA);
			
	
	}

}
