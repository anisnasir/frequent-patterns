package utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Path;
import struct.Quadriplet;

public class QuadripletGenerator {
	Random rand = new Random();
	public Set<Quadriplet> getAllSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Path> srcTwoHopNeighbors, HashSet<Path> dstTwoHopNeighbors) {
		Set<Quadriplet> result = new HashSet<Quadriplet>();

		// combine src, dst, dstNeighbors, dstTwoHopNeighbors
		for (Path dstTwoHopNeighbor : dstTwoHopNeighbors) {
			Quadriplet quadriplet = new Quadriplet();
			quadriplet.addEdge(edge);
			Set<StreamEdge> pathEdges = dstTwoHopNeighbor.getPath();
			for (StreamEdge pathEdge : pathEdges) {
				quadriplet.addEdge(pathEdge);
			}
			quadriplet.addEdgesForNode(nodeMap, src);
			
			if(quadriplet.isQuadriplet())
				result.add(quadriplet);
		}

		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
			for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				quadriplet.addEdge(
						new StreamEdge(src.getVertexId(), src.getVertexLabel(), srcNeighbor.getVertexId(),
								srcNeighbor.getVertexLabel()));
				quadriplet.addEdge(
						new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), dstNeighbor.getVertexId(),
								dstNeighbor.getVertexLabel()));
				
				quadriplet.addEdgesForNode(nodeMap, srcNeighbor);
				
				if(quadriplet.isQuadriplet())
					result.add(quadriplet);
			}
		}

		// combine srcTwoHopNeighbors, srcNeighbors, src, and dst
		for (Path srcTwoHopNeighbor : srcTwoHopNeighbors) {
			Quadriplet quadriplet = new Quadriplet();
			quadriplet.addEdge(edge);
			Set<StreamEdge> pathEdges = srcTwoHopNeighbor.getPath();
			for (StreamEdge pathEdge : pathEdges) {
				quadriplet.addEdge(pathEdge);
			}
			quadriplet.addEdgesForNode(nodeMap, dst);
			if(quadriplet.isQuadriplet()) {
				result.add(quadriplet);
			}
		}
		
		if (srcOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(srcOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					Quadriplet quadriplet = new Quadriplet();
					quadriplet.addEdge(edge);
					LabeledNode firstNeighbor = neighborList.get(i);
					quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));
					LabeledNode secondNeighbor = neighborList.get(j);
					quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));
					if(quadriplet.isQuadriplet()) {
						result.add(quadriplet);
					}
				}
			}
		}
		
		if (dstOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(dstOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					Quadriplet quadriplet = new Quadriplet();
					quadriplet.addEdge(edge);
					LabeledNode firstNeighbor = neighborList.get(i);
					quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));
					LabeledNode secondNeighbor = neighborList.get(j);
					quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));
					if(quadriplet.isQuadriplet()) {
						result.add(quadriplet);
					}
				}
			}
		}
		
		return result;
	}

	public int getAllSubgraphsCount(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Path> srcTwoHopNeighbors, HashSet<Path> dstTwoHopNeighbors) {

		int count = 0;
		count+=dstTwoHopNeighbors.size();
		count+= (srcOneHopNeighbor.size() * dstOneHopNeighbor.size());
		count+=srcTwoHopNeighbors.size();
		return count;
	}
	
	public Quadriplet getRandom(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Path> srcTwoHopNeighbors, HashSet<Path> dstTwoHopNeighbors) {
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edge);
		
		int number1 = rand.nextInt(2);
		if(number1 == 0) { //pick src neighbor
			int number2 = rand.nextInt(2);
			List<LabeledNode> neighbors = new ArrayList<LabeledNode>(srcOneHopNeighbor);
			
			if(number2 == 0) { //srcTwoHop
				List<Path> twoHopNeighbors = new ArrayList<Path>(srcTwoHopNeighbors);
				Path thirdVertex = twoHopNeighbors.get(rand.nextInt(twoHopNeighbors.size()));
				for(StreamEdge e: thirdVertex.getPath()) {
					quadriplet.addEdge(e);
				}
			}else { //dstOneHope
				LabeledNode nextVertex = neighbors.get(rand.nextInt(neighbors.size()));
				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), nextVertex.getVertexId(), nextVertex.getVertexLabel()));
				
				List<LabeledNode> neighbors2 = new ArrayList<LabeledNode>(dstOneHopNeighbor);
				LabeledNode thirdVertex = neighbors2.get(rand.nextInt(neighbors2.size()));
				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), thirdVertex.getVertexId(), thirdVertex.getVertexLabel()));	
			}
		}else { //pick dst neighbors
			List<LabeledNode> neighbors = new ArrayList<LabeledNode>(dstOneHopNeighbor);
			int number2 = rand.nextInt(3);
			if(number2 == 0) { //dstTwoHop
				List<Path> twoHopNeighbors = new ArrayList<Path>(dstTwoHopNeighbors);
				Path thirdVertex = twoHopNeighbors.get(rand.nextInt(twoHopNeighbors.size()));
				for(StreamEdge e: thirdVertex.getPath()) {
					quadriplet.addEdge(e);
				}
			}else { //srcOneHope
				LabeledNode nextVertex = neighbors.get(rand.nextInt(neighbors.size()));
				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), nextVertex.getVertexId(), nextVertex.getVertexLabel()));
				
				List<LabeledNode> neighbors2 = new ArrayList<LabeledNode>(srcOneHopNeighbor);
				LabeledNode thirdVertex = neighbors2.get(rand.nextInt(neighbors2.size()));
				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), thirdVertex.getVertexId(), thirdVertex.getVertexLabel()));
				
			}
		}
		return quadriplet;
	}

}
