package fullydynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

import graphpattern.FourNodeGraphPattern;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.LabeledNode;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;
import utility.SetFunctions;

public class FullyDynamicEdgeReservoirAlgorithmFourNode implements TopkGraphPatterns{
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	HashMap<Pattern, Long> frequentPatterns;
	int k ;
	int M;
	int N;
	int Ncurrent;
	int numSubgraphs;
	int c1;
	int c2;
	//HypergeometricDistribution hyper;
	QuadripletGenerator subgraphGenerator;
	
	public FullyDynamicEdgeReservoirAlgorithmFourNode(int size, int k) {
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
		frequentPatterns = new HashMap<Pattern, Long>();
	}
	public boolean addEdge(StreamEdge edge) {
		N++;
		Ncurrent++;
		//System.out.println("+" + edge);

		if(c1 + c2 == 0) {
			if(reservoir.getSize() < M) {
				reservoir.add(edge);
				addQuadriplets(edge);
				utility.handleEdgeAddition(edge, nodeMap);
			}else if ( Math.random() < (M/(double)N)) {
				//remove a random edge from reservoir
				StreamEdge oldEdge = reservoir.getRandom();
				reservoir.remove(oldEdge);
				utility.handleEdgeDeletion(oldEdge, nodeMap);
				removeQuadriplets(oldEdge);

				//add the new edge in the reservoir
				reservoir.add(edge);
				addQuadriplets(edge);
				utility.handleEdgeAddition(edge, nodeMap);	
			}
		}else if (Math.random() < (c1/(double)(c1+c2))) {
			//add to reservoir without removing any edge: compensation
			reservoir.add(edge);
			addQuadriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
			c1--;
		}else {
			c2--;
		}

		return true;
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
			if (subgraph.getType() == SubgraphType.LINE || subgraph.getType() == SubgraphType.STAR) {
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
		return this.numSubgraphs;
	}

	void addSubgraph(Quadriplet t) {
		addFrequentPattern(t);
		numSubgraphs++;
	}

	void removeSubgraph(Quadriplet t) {
		removeFrequentPattern(t);
		numSubgraphs--;
	}

	//remove a and add b
	void replaceSubgraphs(Quadriplet a, Quadriplet b) {
		removeSubgraph(a);
		addSubgraph(b);

	}
	void addFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1l);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
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

	/*void initializeHypergeometricDistribution() {
		int n = Math.min(this.M, Ncurrent + c1 + c2);
		hyper = new HypergeometricDistribution(Ncurrent + c1 + c2, Ncurrent, n);
	}*/
	
	public void correctEstimates() {
		//LINE, STAR, TAILED_TRIANGLE, CIRCLE, QUASI_CLIQUE, CLIQUE
		//initializeHypergeometricDistribution();
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
			frequentPatterns.put(p, (long) value);
		}
	}

	private double correctFactorLineAndStar() {
		double result = (N / (double) M);
		result =  Math.pow(result, 3);
		//result = result/(1-hyper.cumulativeProbability(0, 2));
		return Math.max(1, result);
	}

	private double correctFactorTailedTriangleAndCircle() {
		double result = (N / (double) M);
		result = Math.pow(result, 4);
		//result = result/(1-hyper.cumulativeProbability(0, 3));
		return Math.max(1, result);
	}
	
	private double correctFactorQuasiClique() {
		double result = (N / (double) M);
		result = Math.pow(result, 5);
		//result = result/(1-hyper.cumulativeProbability(0, 4));
		return Math.max(1, result);
	}
	
	private double correctFactorClique() {
		double result = (N / (double) M);
		result = Math.pow(result, 6);
		//result = result/(1-hyper.cumulativeProbability(0, 5));
		return Math.max(1, result);
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

		// System.out.println(nodeMap.map);
		subgraphGenerator = new QuadripletGenerator();
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
			Quadriplet quadripletMinusEdge = subgraph.getQuadripletMinusEdge(edge);
			removeSubgraph(subgraph);
			if(quadripletMinusEdge.isQuadriplet()) {
				addSubgraph(quadripletMinusEdge);
			}
		}

		//System.out.println(reservoir.size());
		return true;
	}
	@Override
	public int getCurrentReservoirSize() {
		return reservoir.getSize();
	}

}
