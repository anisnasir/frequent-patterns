package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphpattern.FourNodeGraphPattern;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;

public class IncrementalEdgeReservoirFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	HashMap<Pattern, Long> frequentPatterns;
	int k;
	int M;
	int totalNumEdges;
	long numSubgraphs;
	QuadripletGenerator subgraphGenerator;

	public IncrementalEdgeReservoirFourNode(int size, int k) {
		nodeMap = new NodeMap();
		reservoir = new EdgeReservoir<StreamEdge>();
		utility = new EdgeHandler();
		this.k = k;
		this.M = size;
		this.totalNumEdges = 0;
		this.numSubgraphs = 0;
		frequentPatterns = new HashMap<Pattern, Long>();
	}

	public boolean addEdge(StreamEdge edge) {
		totalNumEdges++;
		// System.out.println("+" + edge);

		if (reservoir.getSize() <= M) {
			reservoir.add(edge);
			addQuadriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
		} else if (Math.random() < (M / (double) totalNumEdges)) {
			// remove a random edge from reservoir
			StreamEdge oldEdge = reservoir.getRandom();
			reservoir.remove(oldEdge);
			utility.handleEdgeDeletion(oldEdge, nodeMap);
			removeQuadriplets(oldEdge);

			// add the new edge in the reservoir
			reservoir.add(edge);
			addQuadriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
		}

		return false;
	}

	void addQuadriplets(StreamEdge edge) {
		subgraphGenerator = new QuadripletGenerator();
		
		// System.out.println(nodeMap.map);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);

		// long startTime = System.nanoTime();
		Set<Quadriplet> subgraphs = subgraphGenerator.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
	
		// System.out.println("step 1 " + (System.nanoTime()-startTime));
		for (Quadriplet subgraph : subgraphs) {
			if (subgraph.getType().equals(SubgraphType.LINE) || subgraph.getType().equals(SubgraphType.STAR)) {
				addSubgraph(subgraph);
			} else {
				addSubgraph(subgraph);
				removeSubgraph(subgraph.getQuadripletMinusEdge(edge));
			}
		}
		
	}

	void removeQuadriplets(StreamEdge edge) {	
		subgraphGenerator = new QuadripletGenerator();
		
		// System.out.println(nodeMap.map);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);

		// long startTime = System.nanoTime();
		Set<Quadriplet> subgraphs = subgraphGenerator.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		
		for (Quadriplet subgraph : subgraphs) {
			Quadriplet current = subgraph.getQuadripletMinusEdge(edge);
			removeSubgraph(subgraph);
			if(current.isQuadriplet()) {
				addSubgraph(current);
			}
			
		}
	}

	public long getNumberofSubgraphs() {
		return numSubgraphs;
	}

	void addSubgraph(Quadriplet t) {
		if(t.isQuadriplet()) {
			addFrequentPattern(t);
			numSubgraphs++;
		}
	}

	void removeSubgraph(Quadriplet t) {
		if(t.isQuadriplet()) {
			removeFrequentPattern(t);
			numSubgraphs--;
		}
	}

	// remove a and add b
	void replaceSubgraphs(Quadriplet a, Quadriplet b) {
		removeSubgraph(a);
		removeFrequentPattern(a);
		addSubgraph(b);
		addFrequentPattern(b);

	}

	void addFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if (frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1l);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if (frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			if (count > 1)
				frequentPatterns.put(p, count - 1);
			else
				frequentPatterns.remove(p);
		}
	}

	public HashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	public HashMap<Pattern, Long> correctEstimates() {
		HashMap<Pattern, Long> correctFrequentPatterns = new HashMap<Pattern, Long>();
		//LINE, STAR, TAILED_TRIANGLE, CIRCLE, QUASI_CLIQUE, CLIQUE
		double lineAndStarCorrectFactor = correctFactorLineAndStar();
		double tailedTriangleAndCircleCorrectFactor = correctFactorTailedTriangleAndCircle();
		double quasiCliqueCorrectFactor = correctFactorQuasiClique();
		double cliqueCorrectFactor = correctFactorClique();
		
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern pattern : patterns) {
			FourNodeGraphPattern p = (FourNodeGraphPattern) pattern;
			long count = frequentPatterns.get(p);
			double value;
			if (p.getType().equals(SubgraphType.LINE) || p.getType().equals(SubgraphType.STAR))
				value = count * lineAndStarCorrectFactor;
			else if (p.getType().equals(SubgraphType.TAILED_TRIANGLE) || p.getType().equals(SubgraphType.CIRCLE))
				value = count * tailedTriangleAndCircleCorrectFactor;
			else if (p.getType().equals(SubgraphType.QUASI_CLIQUE))
				value = count * quasiCliqueCorrectFactor;
			else 
				value = count * cliqueCorrectFactor;
			correctFrequentPatterns.put(p, (long) value);
		}
		return correctFrequentPatterns;
	}

	private double correctFactorLineAndStar() {
		double result = (totalNumEdges / (double) M);
		return Math.max(1, Math.pow(result, 3));
	}

	private double correctFactorTailedTriangleAndCircle() {
		double result = (totalNumEdges / (double) M);
		return Math.max(1, Math.pow(result, 4));
	}
	
	private double correctFactorQuasiClique() {
		double result = (totalNumEdges / (double) M);
		return Math.max(1, Math.pow(result, 5));
	}
	
	private double correctFactorClique() {
		double result = (totalNumEdges / (double) M);
		return Math.max(1, Math.pow(result, 6));
	}

	@Override
	public boolean removeEdge(StreamEdge edge) {
		return false;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.getSize();
	}

}
