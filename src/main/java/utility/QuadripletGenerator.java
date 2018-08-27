package utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Path;
import struct.Quadriplet;

public class QuadripletGenerator {
	Random rand = new Random();
	public Set<Quadriplet> getAllSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			THashSet<LabeledNeighbor> srcOneHopNeighbor, THashSet<LabeledNeighbor> dstOneHopNeighbor,
			THashSet<Path> srcTwoHopNeighbors, THashSet<Path> dstTwoHopNeighbors) {
		Set<Quadriplet> result = new HashSet<Quadriplet>();

		// combine src, dst, dstNeighbors, dstTwoHopNeighbors
		for (Path dstTwoHopNeighbor : dstTwoHopNeighbors) {
			Quadriplet quadriplet = new Quadriplet();
			quadriplet.addEdge(edge);
			List<StreamEdge> pathEdges = dstTwoHopNeighbor.getPath();
			for (StreamEdge pathEdge : pathEdges) {
				quadriplet.addEdge(pathEdge);
			}
			quadriplet.addEdgesFromNode(nodeMap, src, dst);
			quadriplet.addEdgesFromNode(nodeMap, dst, src);

			if(quadriplet.isQuadriplet())
				result.add(quadriplet);
		}

		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNeighbor srcNeighbor : srcOneHopNeighbor) {
			for (LabeledNeighbor dstNeighbor : dstOneHopNeighbor) {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				quadriplet.addEdge(
						new StreamEdge(src.getVertexId(), src.getVertexLabel(), srcNeighbor.getDst().getVertexId(),
								srcNeighbor.getDst().getVertexLabel(), srcNeighbor.getEdgeLabel()));
				quadriplet.addEdge(
						new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), dstNeighbor.getDst().getVertexId(),
								dstNeighbor.getDst().getVertexLabel(), dstNeighbor.getEdgeLabel()));

				quadriplet.addEdgesFromNode(nodeMap, src, dst);
				quadriplet.addEdgesFromNode(nodeMap, dst, src);

				if(quadriplet.isQuadriplet())
					result.add(quadriplet);
			}
		}

		// combine srcTwoHopNeighbors, srcNeighbors, src, and dst
		for (Path srcTwoHopNeighbor : srcTwoHopNeighbors) {
			Quadriplet quadriplet = new Quadriplet();
			quadriplet.addEdge(edge);
			List<StreamEdge> pathEdges = srcTwoHopNeighbor.getPath();
			for (StreamEdge pathEdge : pathEdges) {
				quadriplet.addEdge(pathEdge);
			}

			quadriplet.addEdgesFromNode(nodeMap, src, dst);
			quadriplet.addEdgesFromNode(nodeMap, dst, src);

			if(quadriplet.isQuadriplet()) {
				result.add(quadriplet);
			}
		}
		return result;
	}

	public int getAllSubgraphsCount(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			THashSet<LabeledNeighbor> srcOneHopNeighbor, THashSet<LabeledNeighbor> dstOneHopNeighbor,
			THashSet<Path> srcTwoHopNeighbors, THashSet<Path> dstTwoHopNeighbors) {

		int count = 0;
		count+=dstTwoHopNeighbors.size();
		count+= (srcOneHopNeighbor.size() * dstOneHopNeighbor.size());
		count+=srcTwoHopNeighbors.size();
		return count;
	}
	
	public Quadriplet getRandom(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			THashSet<LabeledNeighbor> srcOneHopNeighbor, THashSet<LabeledNeighbor> dstOneHopNeighbor,
			THashSet<Path> srcTwoHopNeighbors, THashSet<Path> dstTwoHopNeighbors) {
		Quadriplet quadriplet = new Quadriplet();
		quadriplet.addEdge(edge);
		
		int number1 = rand.nextInt(2);
		if(number1 == 0) { //pick src neighbor
			int number2 = rand.nextInt(2);
			List<LabeledNeighbor> neighbors = new ArrayList<LabeledNeighbor>(srcOneHopNeighbor);
			
			if(number2 == 0) { //srcTwoHop
				List<Path> twoHopNeighbors = new ArrayList<Path>(srcTwoHopNeighbors);
				Path thirdVertex = twoHopNeighbors.get(rand.nextInt(twoHopNeighbors.size()));
				for(StreamEdge e: thirdVertex.getPath()) {
					quadriplet.addEdge(e);
				}
				quadriplet.addEdgesFromNode(nodeMap, src, dst);
				quadriplet.addEdgesFromNode(nodeMap, dst, src);
			}else { //dstOneHope
				LabeledNeighbor nextVertex = neighbors.get(rand.nextInt(neighbors.size()));
				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), nextVertex.getDst().getVertexId(), nextVertex.getDst().getVertexLabel(), nextVertex.getEdgeLabel()));
				
				List<LabeledNeighbor> neighbors2 = new ArrayList<LabeledNeighbor>(dstOneHopNeighbor);
				LabeledNeighbor thirdVertex = neighbors2.get(rand.nextInt(neighbors2.size()));
				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), thirdVertex.getDst().getVertexId(), thirdVertex.getDst().getVertexLabel(), thirdVertex.getEdgeLabel()));	
			}
		}else { //pick dst neighbors
			List<LabeledNeighbor> neighbors = new ArrayList<LabeledNeighbor>(dstOneHopNeighbor);
			int number2 = rand.nextInt(3);
			if(number2 == 0) { //dstTwoHop
				List<Path> twoHopNeighbors = new ArrayList<Path>(dstTwoHopNeighbors);
				Path thirdVertex = twoHopNeighbors.get(rand.nextInt(twoHopNeighbors.size()));
				for(StreamEdge e: thirdVertex.getPath()) {
					quadriplet.addEdge(e);
				}
				quadriplet.addEdgesFromNode(nodeMap, src, dst);
				quadriplet.addEdgesFromNode(nodeMap, dst, src);
			}else { //srcOneHope
				LabeledNeighbor nextVertex = neighbors.get(rand.nextInt(neighbors.size()));
				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), nextVertex.getDst().getVertexId(), nextVertex.getDst().getVertexLabel(), nextVertex.getEdgeLabel()));
				
				List<LabeledNeighbor> neighbors2 = new ArrayList<LabeledNeighbor>(srcOneHopNeighbor);
				LabeledNeighbor thirdVertex = neighbors2.get(rand.nextInt(neighbors2.size()));
				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), thirdVertex.getDst().getVertexId(), thirdVertex.getDst().getVertexLabel(), thirdVertex.getEdgeLabel()));
				
			}
		}
		return quadriplet;
	}

}
