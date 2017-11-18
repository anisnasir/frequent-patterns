package fullydynamictopkgraphpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.GraphPattern;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class FullyDynamicTriesteAlgorithm implements TopkGraphPatterns{
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	THashMap<GraphPattern, Integer> frequentPatterns;
	int k ;
	int M;
	int N;
	int Ncurrent;
	int c1;
	int c2;
	HypergeometricDistribution hyper;
	public FullyDynamicTriesteAlgorithm(int size, int k) {
		nodeMap = new NodeMap();
		reservoir = new EdgeReservoir<StreamEdge>();
		utility = new EdgeHandler();
		this.k = k;
		this.M = size;
		this.N = 0 ;
		this.Ncurrent = 0 ;
		this.c1 = 0;
		this.c2 = 0;
		frequentPatterns = new THashMap<GraphPattern, Integer>();
	}
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

		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();

		for(LabeledNeighbor t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNeighbor t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getDst().getVertexId() , t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			}else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getDst().getVertexId() , t.getDst().getVertexLabel(), src.getVertexId(), src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(t.getDst().getVertexId(), t.getDst().getVertexLabel(), dst.getVertexId(), dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				replaceSubgraphs(tripletWedge, tripletTriangle);

			}
		}
	}
	void removeTriplet(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());


		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();

		for(LabeledNeighbor t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getDst().getVertexId() , t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNeighbor t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(dst.getVertexId(),dst.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			}else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(), src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}
	}

	public int getNumberofSubgraphs() {
		return Ncurrent;
	}

	void addSubgraph(Triplet t) {
		addFrequentPattern(t);
	}

	void removeSubgraph(Triplet t) {
		removeFrequentPattern(t);
	}

	//remove a and add b
	void replaceSubgraphs(Triplet a, Triplet b) {
		removeSubgraph(a);
		addSubgraph(b);

	}
	void addFrequentPattern(Triplet t) {
		GraphPattern p = new GraphPattern(t);
		if(frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Triplet t) {
		GraphPattern p = new GraphPattern(t);
		if(frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			if(count >1)
				frequentPatterns.put(p, count-1);
			else 
				frequentPatterns.remove(p);
		}
	}

	public THashMap<GraphPattern, Integer> getFrequentPatterns() {
		correctEstimates();
		return this.frequentPatterns;
	}
	void initializeHypergeometricDistribution() {
		int n =Math.min(this.M, Ncurrent+c1+ c2);
		hyper = new HypergeometricDistribution(Ncurrent+c1+c2, Ncurrent,n);
	}
	private void correctEstimates() {
		initializeHypergeometricDistribution();
		double wedgeCorrectFactor = correctFactorWedge();
		double triangleCorrectFactor = correctFactorTriangle();
		List<GraphPattern> patterns = new ArrayList<GraphPattern>(frequentPatterns.keySet());
		for(GraphPattern p: patterns) {
			int count = frequentPatterns.get(p);
			double value;
			if(p.isWedge())
				value = count*wedgeCorrectFactor;
			else 
				value = count*triangleCorrectFactor;
			
			frequentPatterns.put(p, (int)value);
		}
	}
	
	private double correctFactorWedge() { 
		double result = (N/(double)M)*((N-1)/(double)(M-1));
		result = result/hyper.cumulativeProbability(0, 1);
		return Math.max(1,result);
	}
	
	private double correctFactorTriangle() { 
		double result = (N/(double)M)*((N-1)/(double)(M-1))*((N-2)/(double)(M-2));
		result = result/hyper.cumulativeProbability(0, 2);
		return Math.max(result, 1);
	}
	
	@Override
	public boolean removeEdge(StreamEdge edge) {
		if(reservoir.contains(edge)) {
			utility.handleEdgeDeletion(edge, nodeMap);
			c1++;
		}else {
			c2++;
		}
		Ncurrent--;
		return true;
	}

}
