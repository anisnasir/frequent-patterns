package fullydynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class FullyDynamicEdgeReservoirThreeNode implements TopkGraphPatterns{
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	THashMap<Pattern, Long> frequentPatterns;
	int k ;
	int M;
	int N;
	int Ncurrent;
	int numSubgraphs;
	int c1;
	int c2;
	HypergeometricDistribution hyper;
	public FullyDynamicEdgeReservoirThreeNode(int size, int k) {
		nodeMap = new NodeMap();
		reservoir = new EdgeReservoir<StreamEdge>();
		utility = new EdgeHandler();
		this.k = k;
		this.M = size;
		this.N = 0 ;
		this.Ncurrent = 0 ;
		this.c1 = 0;
		this.c2 = 0;
		this.numSubgraphs  = 0 ;
		frequentPatterns = new THashMap<Pattern, Long>();
	}
	@Override
	public boolean addEdge(StreamEdge edge) {
		N++;
		Ncurrent++;
		//System.out.println("+" + edge);

		if(c1 + c2 == 0) {
			if(reservoir.getSize() < M) {
				reservoir.add(edge);
				addTriplets(edge);
				utility.handleEdgeAddition(edge, nodeMap);
			}else if ( Math.random() < (M/(double)N)) {
				//remove a random edge from reservoir
				StreamEdge oldEdge = reservoir.getRandom();
				reservoir.remove(oldEdge);
				utility.handleEdgeDeletion(oldEdge, nodeMap);
				removeTriplet(oldEdge);

				//add the new edge in the reservoir
				reservoir.add(edge);
				addTriplets(edge);
				utility.handleEdgeAddition(edge, nodeMap);	
			}
		}else if (Math.random() < (c1/(double)(c1+c2))) {
			//add to reservoir without removing any edge: compensation
			reservoir.add(edge);
			addTriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
			c1--;
		}else {
			c2--;
		}

		return true;
	}

	void addTriplets(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		for(LabeledNode t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				addSubgraph(triplet);
			}
		}

		for(LabeledNode t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getVertexId() , t.getVertexLabel()));
				addSubgraph(triplet);
			}else {
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getVertexId() , t.getVertexLabel(), src.getVertexId(), src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(t.getVertexId(), t.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				replaceSubgraphs(tripletWedge, tripletTriangle);

			}
		}
	}
	void removeTriplet(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());


		THashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		HashMap<LabeledNode, LabeledNode> srcCommonNeighbor = new HashMap<LabeledNode, LabeledNode>();

		for(LabeledNode t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getVertexId() , t.getVertexLabel()));
				removeSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNode t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(dst.getVertexId(),dst.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				removeSubgraph(triplet);
			}else {
				LabeledNode srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(), src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}
	}

	@Override
	public long getNumberofSubgraphs() {
		return this.numSubgraphs;
	}

	void addSubgraph(Triplet t) {
		addFrequentPattern(t);
		numSubgraphs++;
	}

	void removeSubgraph(Triplet t) {
		removeFrequentPattern(t);
		numSubgraphs--;
	}

	//remove a and add b
	void replaceSubgraphs(Triplet a, Triplet b) {
		removeSubgraph(a);
		addSubgraph(b);

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

	@Override
	public THashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	void initializeHypergeometricDistribution() {
		int n = Math.min(this.M, Ncurrent + c1 + c2);
		hyper = new HypergeometricDistribution(Ncurrent + c1 + c2, Ncurrent, n);
	}
	
	@Override
	public THashMap<Pattern, Long> correctEstimates() {
		THashMap<Pattern, Long> correctFrequentPatterns = new THashMap<>();
		initializeHypergeometricDistribution();
		double wedgeCorrectFactor = correctFactorWedge();
		double triangleCorrectFactor = correctFactorTriangle();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for(Pattern p: patterns) {
			long count = frequentPatterns.get(p);
			double value;
			if(p.getType() == SubgraphType.WEDGE)
				value = count*wedgeCorrectFactor;
			else 
				value = count*triangleCorrectFactor;
			
			correctFrequentPatterns.put(p, (long)value);
		}
		return correctFrequentPatterns;
	}
	
	private double correctFactorWedge() { 
		double result = (Ncurrent/(double)M)*((Ncurrent-1)/(double)(M-1));
		result = result/(1-hyper.cumulativeProbability(0, 1));
		return Math.max(1,result);
	}
	
	private double correctFactorTriangle() { 
		double result = (Ncurrent/(double)M)*((Ncurrent-1)/(double)(M-1))*((Ncurrent-2)/(double)(M-2));
		result = result/(1-hyper.cumulativeProbability(0, 2));
		return Math.max(result, 1);
	}
	
	@Override
	public boolean removeEdge(StreamEdge edge) {
		if(reservoir.contains(edge)) {
			utility.handleEdgeDeletion(edge, nodeMap);
			removeEdgeHelper(edge);
			c1++;
		}else {
			c2++;
		}
		Ncurrent--;
		return true;
	}
	public boolean removeEdgeHelper(StreamEdge edge) {
		//System.out.println("-" + edge);

		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		HashMap<LabeledNode, LabeledNode> srcCommonNeighbor = new HashMap<LabeledNode, LabeledNode>();

		for(LabeledNode t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getVertexId() , t.getVertexLabel()));
				removeSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNode t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t,edge, new StreamEdge(dst.getVertexId(),dst.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				removeSubgraph(triplet);
			}else {
				LabeledNode srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(), src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}

		//System.out.println(reservoir.size());
		return false;
	}
	@Override
	public int getCurrentReservoirSize() {
		return reservoir.getSize();
	}

}
