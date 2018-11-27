package utility;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;

public class EdgeHandler {
	public EdgeHandler() {
		
	}
	
	public void handleEdgeAddition(StreamEdge item, NodeMap nodeMap ) {
		//System.out.println("+ " + item.toString());
		String src = item.getSource();
		Integer srcLabel = item.getSrcLabel();
		String dest = item.getDestination();
		Integer dstLabel = item.getDstLabel();
		
		LabeledNode srcNode = new LabeledNode(src,srcLabel);
		LabeledNode dstNode = new LabeledNode(dest,dstLabel);
		
		nodeMap.addEdge(srcNode, dstNode);
		nodeMap.addEdge(dstNode, srcNode);
	}
	
	public void handleEdgeDeletion(StreamEdge oldestEdge, NodeMap nodeMap ) {
		//System.out.println("- " + oldestEdge.toString());
		String src = oldestEdge.getSource();
		Integer srcLabel = oldestEdge.getSrcLabel();
		String dest = oldestEdge.getDestination();
		Integer dstLabel = oldestEdge.getDstLabel();
		
		LabeledNode srcNode = new LabeledNode(src,srcLabel);
		LabeledNode dstNode = new LabeledNode(dest,dstLabel);
					
		//removes from each others neighbor table
		nodeMap.removeEdge(srcNode, dstNode);
		nodeMap.removeEdge(dstNode, srcNode);
	}
}
