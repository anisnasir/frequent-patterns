package incremental;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.FourNodeGraphPattern;
import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;
import utility.AlgorithmZ;

public class IncrementalSubgraphReservoirFourNode2 implements TopkGraphPatterns {
	NodeMap nodeMap;
	//NodeBottomK nodeBottomK;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Quadriplet> reservoir;
	Random rand;
	QuadripletGenerator subgraphGenerator;

	THashMap<Pattern, Integer> frequentPatterns;
	long numberSubgraphs; // total number of subgraphs
	int reservoirSize; // maximum reservoir size
	int sum;
	AlgorithmZ skipRS;

	public IncrementalSubgraphReservoirFourNode2(int size, int k) {
		this.nodeMap = new NodeMap();
		//this.nodeBottomK = new NodeBottomK();
		rand = new Random();
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Quadriplet>();
		numberSubgraphs = 0;
		reservoirSize = size;
		frequentPatterns = new THashMap<Pattern, Integer>();
		sum = 0;
		skipRS = new AlgorithmZ(reservoirSize);
	}

	@Override
	public boolean addEdge(StreamEdge edge) {
		if (nodeMap.contains(edge)) {
			return false;
		}
		QuadripletGenerator subgraphGenerator = new QuadripletGenerator();
		// System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		THashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		int subgraphCount = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
		
		//System.out.println("time taken 1. " + (System.nanoTime()-startTime));
		// replaces the existing wedges in the reservoir with the triangles
		THashSet<Quadriplet> candidateSubgraphs = reservoir.getAllSubgraphs(src);
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
			}
		}	
		int W = subgraphCount;

		// System.out.println("W " + W);
		if (W > 0) {
			int i = 0;
			while (sum < W) {
				i++;
				int zrs = skipRS.apply((int)numberSubgraphs);
				numberSubgraphs = numberSubgraphs + zrs + 1;
				sum = sum + zrs + 1;
			}
			int count = 0;
			while (count < i) {
				Quadriplet randomSubgraph = subgraphGenerator.getRandomNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
				if(randomSubgraph!=null) {
					addToReservoir(randomSubgraph);
					count++;
				}
				
			}
			//System.out.println("time taken 2. " + (System.nanoTime()-startTime));
			sum = sum - W;
		}

		utility.handleEdgeAddition(edge, nodeMap);
		return false;

	}

	void addToReservoir(Quadriplet quadriplet) {
		if (reservoir.size() >= reservoirSize) {
			Quadriplet removedQuadriplet = reservoir.getRandom();
			reservoir.remove(removedQuadriplet);
			removeFrequentPattern(removedQuadriplet);
		}
		reservoir.add(quadriplet);
		addFrequentPattern(quadriplet);

	}

	public HashSet<LabeledNode> getNeighbors(HashSet<LabeledNode> randomVertexNeighborWithEdgeLabels) {
		HashSet<LabeledNode> results = new HashSet<LabeledNode>();
		for (LabeledNode a : randomVertexNeighborWithEdgeLabels) {
			results.add(a);
		}
		return results;
	}

	@Override
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

	@Override
	public THashMap<Pattern, Integer> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	@Override
	public THashMap<Pattern, Integer> correctEstimates() {
		THashMap<Pattern, Integer> correctFrequentPatterns = new THashMap<Pattern, Integer>();
		double correctFactor = correctFactor();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern p : patterns) {
			long count = frequentPatterns.get(p);
			double value = count * correctFactor;
			correctFrequentPatterns.put(p, (int) value);
		}
		return correctFrequentPatterns;
	}

	private double correctFactor() {
		return Math.max(1, ((double) numberSubgraphs / reservoirSize));
	}

	@Override
	public long getNumberofSubgraphs() {
		return numberSubgraphs;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.size();
	}
}
