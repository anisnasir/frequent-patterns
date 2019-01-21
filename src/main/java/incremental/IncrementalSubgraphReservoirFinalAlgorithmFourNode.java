package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import graphpattern.FourNodeGraphPattern;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import reservoir.SubgraphReservoir;
import struct.LabeledNode;
import struct.NodeBottomK;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.Subgraph;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;
import utility.ReservoirSampling;
import utility.SetFunctions;
import utility.AlgorithmZ;
import utility.BottomKSketch;

public class IncrementalSubgraphReservoirFinalAlgorithmFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	//NodeBottomK nodeBottomK;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Quadriplet> reservoir;
	Random rand;
	QuadripletGenerator subgraphGenerator;

	HashMap<Pattern, Integer> frequentPatterns;
	int N; // total number of subgraphs
	int M; // maximum reservoir size
	int sum;
	AlgorithmZ skipRS;

	public IncrementalSubgraphReservoirFinalAlgorithmFourNode(int size, int k) {
		this.nodeMap = new NodeMap();
		//this.nodeBottomK = new NodeBottomK();
		rand = new Random();
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Quadriplet>();
		N = 0;
		M = size;
		frequentPatterns = new HashMap<Pattern, Integer>();
		sum = 0;
		skipRS = new AlgorithmZ(M);
		subgraphGenerator = new QuadripletGenerator();
	}

	public boolean addEdge(StreamEdge edge) {
		if (nodeMap.contains(edge)) {
			return false;
		}
		// System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		long startTime = System.nanoTime();
		int[] subgraphCountArray = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
		System.out.println("step 1 " + (System.nanoTime()-startTime));
		int subgraphCount = 0;
		for(int count:subgraphCountArray) {
			subgraphCount+=count;
		}
		

		startTime = System.nanoTime();
		// replaces the existing wedges in the reservoir with the triangles
		HashSet<Quadriplet> candidateSubgraphs = reservoir.getAllSubgraphs(src);
		ArrayList<Quadriplet> oldSubgraphs = new ArrayList<Quadriplet>();
		// System.out.println("size " + candidateTriangles.size());
		for (Quadriplet t : candidateSubgraphs) {
			if (t.getAllVertices().contains(dst)) {
				oldSubgraphs.add(t);
			}
		}
		if (oldSubgraphs.size() > 0) {
			for (Quadriplet t : oldSubgraphs) {
				Quadriplet newQuadriplet = t.getQuadripletPlusEdge(edge);
				replaceSubgraphs(t, newQuadriplet);
				subgraphCount--;
			}
		}	
		System.out.println("step 2 " + (System.nanoTime()-startTime));
		int W = subgraphCount;

		// System.out.println("W " + W);
		if (W > 0) {
			startTime = System.nanoTime();
			int i = 0;
			while (sum < W) {
				i++;
				int zrs = skipRS.apply(N);
				N = N + zrs + 1;
				sum = sum + zrs + 1;
			}
			System.out.println("step 3 " + (System.nanoTime()-startTime));
			HashSet<LabeledNode> set = new HashSet<LabeledNode>();
			int count = 0;
			startTime = System.nanoTime();
			while (count < i) {
				Quadriplet randomSubgraph = subgraphGenerator.getRandomNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor, subgraphCountArray);
				if(randomSubgraph!=null) {
					addToReservoir(randomSubgraph);
					count++;
				}
			}
			System.out.println("step 4 " + (System.nanoTime()-startTime));
			sum = sum - W;
		}

		utility.handleEdgeAddition(edge, nodeMap);
		return false;

	}

	void addToReservoir(Quadriplet triplet) {
		if (reservoir.size() >= M) {
			Quadriplet temp = reservoir.getRandom();
			reservoir.remove(temp);
			removeFrequentPattern(temp);
		}
		reservoir.add(triplet);
		addFrequentPattern(triplet);

	}

	public HashSet<LabeledNode> getNeighbors(HashSet<LabeledNode> randomVertexNeighborWithEdgeLabels) {
		HashSet<LabeledNode> results = new HashSet<LabeledNode>();
		for (LabeledNode a : randomVertexNeighborWithEdgeLabels) {
			results.add(a);
		}
		return results;
	}

	public boolean removeEdge(StreamEdge edge) {
		return false;
	}

	public LabeledNode getRandomNeighbor(HashSet<LabeledNode> srcNeighbor,
			HashSet<LabeledNode> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if (d_u + d_v == 0) {
			return null;
		}

		double value = d_u / (double) (d_u + d_v);
		if (Math.random() < value) {
			// select neighbor of u or src
			ArrayList<LabeledNode> list = new ArrayList<LabeledNode>(srcNeighbor);
			return list.get(rand.nextInt(list.size()));
		} else {
			// select a neighbor of v or dst
			ArrayList<LabeledNode> list = new ArrayList<LabeledNode>(dstNeighbor);
			return list.get(rand.nextInt(list.size()));
		}
	}

	// remove a and add b
	void replaceSubgraphs(Quadriplet a, Quadriplet b) {
		reservoir.remove(a);
		removeFrequentPattern(a);
		reservoir.add(b);
		addFrequentPattern(b);

	}

	void addFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if (frequentPatterns.containsKey(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if (frequentPatterns.containsKey(p)) {
			int count = frequentPatterns.get(p);
			if (count > 1)
				frequentPatterns.put(p, count - 1);
			else
				frequentPatterns.remove(p);
		}
	}

	public HashMap<Pattern, Integer> getFrequentPatterns() {
		correctEstimates();
		return this.frequentPatterns;
	}

	private void correctEstimates() {
		double correctFactor = correctFactor();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern p : patterns) {
			int count = frequentPatterns.get(p);
			double value = count * correctFactor;
			frequentPatterns.put(p, (int) value);
		}
	}

	private double correctFactor() {
		return Math.max(1, ((double) N / M));
	}

	public int getNumberofSubgraphs() {
		return N;
	}
}
