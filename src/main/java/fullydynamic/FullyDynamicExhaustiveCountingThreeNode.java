package fullydynamic;

import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class FullyDynamicExhaustiveCountingThreeNode implements TopkGraphPatterns{
	NodeMap nodeMap;
	EdgeHandler utility;
	THashMap<Triplet, Integer> counter;
	THashMap<Pattern, Integer> frequentPatterns;
	int numSubgraph;
	public FullyDynamicExhaustiveCountingThreeNode() {
		this.nodeMap = new NodeMap();
		utility = new EdgeHandler();
		counter = new THashMap<>();
		numSubgraph = 0 ;
		frequentPatterns = new THashMap<Pattern, Integer>();
	}
	@Override
	public boolean addEdge(StreamEdge edge) {
		//System.out.println("+" + edge);
		if(nodeMap.contains(edge))
			return false;
		
		//System.out.println(nodeMap.map);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

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

	@Override
	public boolean removeEdge(StreamEdge edge) {
		if(!nodeMap.contains(edge))
			return false;
		//System.out.println("-" + edge);
		utility.handleEdgeDeletion(edge, nodeMap);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

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
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1);
		}
	}
	
	void removeFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			int count = frequentPatterns.get(p);
			if(count >1)
				frequentPatterns.put(p, count-1);
			else 
				frequentPatterns.remove(p);
		}
	}
	
	@Override
	public THashMap<Pattern, Integer> getFrequentPatterns() {
		return this.frequentPatterns;
	}
	@Override
	public long getNumberofSubgraphs() {
		return this.numSubgraph;
	}
	@Override
	public int getCurrentReservoirSize() {
		return 0;
	}
	@Override
	public THashMap<Pattern, Integer> correctEstimates() {
		return frequentPatterns;
	}
	
	

}
