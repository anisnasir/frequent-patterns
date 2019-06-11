package fullydynamic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class FullyDynamicExhaustiveCounting implements TopkGraphPatterns{
	NodeMap nodeMap;
	EdgeHandler utility;
	HashMap<Triplet, Integer> counter;
	HashMap<Pattern, Long> frequentPatterns;
	int numSubgraph;
	public FullyDynamicExhaustiveCounting() {
		this.nodeMap = new NodeMap();
		utility = new EdgeHandler();
		counter = new HashMap<Triplet, Integer>();
		numSubgraph = 0 ;
		frequentPatterns = new HashMap<Pattern, Long>();
	}
	public boolean addEdge(StreamEdge edge) {
		//System.out.println("+" + edge);
		if(nodeMap.contains(edge))
			return false;
		
		//System.out.println(nodeMap.map);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		HashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);
		
		//iterate through source neighbors;
		for(LabeledNode t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				addSubgraph(triplet);
			}
		}

		//iteration through destination neighbors
		for(LabeledNode t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getVertexId(),t.getVertexLabel()));
				addSubgraph(triplet);
			}else {
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getVertexId(),t.getVertexLabel(),  src.getVertexId(),src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(t.getVertexId(), t.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				removeSubgraph(tripletWedge);

				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				addSubgraph(tripletTriangle);

			}
		}

		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(counter);
		return false;
	}

	public boolean removeEdge(StreamEdge edge) {
		if(!nodeMap.contains(edge))
			return false;
		//System.out.println("-" + edge);
		utility.handleEdgeDeletion(edge, nodeMap);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		HashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);
		
		//iterate through source neighbors
		for(LabeledNode t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(src.getVertexId(),src.getVertexLabel(), t.getVertexId(),t.getVertexLabel()));
				removeSubgraph(triplet);
			}
		}

		//iterate through destination neighbors
		for(LabeledNode t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t, edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				removeSubgraph(triplet);
			}else {
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId() , c.getVertexLabel(), src.getVertexId(), src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(),dst.getVertexLabel());
				
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				removeSubgraph(tripletTriangle);
				
				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				addSubgraph(tripletWedge);
			}
		}
		//System.out.println(counter);
		return true;
	}
	void removeSubgraph(Triplet t) {
		/*if(counter.containsKey(t)) {
			int count = counter.get(t);
			if(count > 1)
				counter.put(t, count-1);
			else 
				counter.remove(t);
			numSubgraph--;
			removeFrequentPattern(t);
		}
		else {
			System.out.println("remove error " + t);
			System.out.println(counter);
			System.exit(1);
		}*/
		//removeFrequentPattern(t);
		numSubgraph--;
		removeFrequentPattern(t);
		
	}
	
	void addSubgraph(Triplet t) {
	/*	if(counter.containsKey(t)) {
			int count = counter.get(t);
			counter.put(t, count+1);
		}else {
			counter.put(t, 1);
		}
		*/
		addFrequentPattern(t);
		numSubgraph++;
	}
	
	void addFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1l);
		}
	}
	
	void removeFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			if(count >1)
				frequentPatterns.put(p, count-1);
			else 
				frequentPatterns.remove(p);
		}
	}
	
	public HashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}
	public long getNumberofSubgraphs() {
		return this.numSubgraph;
	}
	@Override
	public int getCurrentReservoirSize() {
		return 0;
	}
	@Override
	public HashMap<Pattern, Long> correctEstimates() {
		return frequentPatterns;
	}
	
	

}
