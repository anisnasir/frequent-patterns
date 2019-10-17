package utility;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;

public class EdgeHandler {
	public EdgeHandler() {
		
	}
	
	public void handleEdgeAddition(StreamEdge item, NodeMap nodeMap ) {
		//System.out.println("+ " + item.toString());
		int src = item.getSource();
		int srcLabel = item.getSrcLabel();
		int dest = item.getDestination();
		int dstLabel = item.getDstLabel();
		
		LabeledNode srcNode = new LabeledNode(src,srcLabel);
		LabeledNode dstNode = new LabeledNode(dest,dstLabel);
		
		nodeMap.addEdge(srcNode, dstNode);
		nodeMap.addEdge(dstNode, srcNode);
	}
	
	public void handleEdgeDeletion(StreamEdge oldestEdge, NodeMap nodeMap ) {
		//System.out.println("- " + oldestEdge.toString());
		int src = oldestEdge.getSource();
		int srcLabel = oldestEdge.getSrcLabel();
		int dest = oldestEdge.getDestination();
		int dstLabel = oldestEdge.getDstLabel();
		
		LabeledNode srcNode = new LabeledNode(src,srcLabel);
		LabeledNode dstNode = new LabeledNode(dest,dstLabel);
					
		//removes from each others neighbor table
		nodeMap.removeEdge(srcNode, dstNode);
		nodeMap.removeEdge(dstNode, srcNode);
	}
	
}
