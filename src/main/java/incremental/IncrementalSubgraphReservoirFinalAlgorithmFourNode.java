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

	HashMap<Pattern, Long> frequentPatterns;
	long numSubgraphs; // total number of subgraphs
	int reservoirSize; // maximum reservoir size
	AlgorithmZ skipRS;
	int seed = 227;
	Random generator = new Random(seed);

	public IncrementalSubgraphReservoirFinalAlgorithmFourNode(int size, int k) {
		this.nodeMap = new NodeMap();
		//this.nodeBottomK = new NodeBottomK();
		rand = new Random();
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Quadriplet>();
		numSubgraphs = 0;
		reservoirSize = size;
		frequentPatterns = new HashMap<Pattern, Long>();
		skipRS = new AlgorithmZ(reservoirSize);
	}

	public boolean addEdge(StreamEdge edge) {
		if (nodeMap.contains(edge)) {
			return false;
		}
		QuadripletGenerator subgraphGenerator = new QuadripletGenerator();
		// System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		
		//System.out.println("time taken 1. " + (System.nanoTime()-startTime));
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
			}
		}	
		int W = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);

		// System.out.println("W " + W);
		if (W > 0) {
			List<Quadriplet> newSubgraphs = subgraphGenerator.getNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor,
					dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
			
			for(Quadriplet quadriplet: newSubgraphs) {
				if(numSubgraphs < this.reservoirSize) {
					//System.out.println("first phase of reservoir: " + numSubgraphs + "  <  "  + this.reservoirSize);
					addToReservoir(quadriplet);
				} else if (generator.nextDouble() < (this.reservoirSize / (double) this.numSubgraphs)){
					
					//System.out.println("second phase of reservoir: " + Math.random() + "< (" + this.reservoirSize + "/ (double) " + this.numSubgraphs);
					Quadriplet removedQuadriplet = reservoir.getRandom();
					reservoir.remove(removedQuadriplet);
					removeFrequentPattern(removedQuadriplet);
					addToReservoir(quadriplet);
				}
				numSubgraphs++;
			}
		}

		utility.handleEdgeAddition(edge, nodeMap);
		return false;

	}

	void addToReservoir(Quadriplet quadriplet) {
		reservoir.add(quadriplet);
		addFrequentPattern(quadriplet);

	}

	public boolean removeEdge(StreamEdge edge) {
		return false;
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
		Long count = frequentPatterns.get(p);
		if (count == null) {
			frequentPatterns.put(p, 1l);
		} else {
			frequentPatterns.put(p, count+1);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		Long count = frequentPatterns.get(p);
		if (count > 1) {
			frequentPatterns.put(p, count - 1);
		} else {
			frequentPatterns.remove(p);
		}
	}

	public HashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	public HashMap<Pattern, Long> correctEstimates() {
		HashMap<Pattern, Long> correctFrequentPatterns = new HashMap<Pattern, Long>();
		double correctFactor = correctFactor();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern p : patterns) {
			long count = frequentPatterns.get(p);
			double value = count * correctFactor;
			correctFrequentPatterns.put(p, (long) value);
		}
		return correctFrequentPatterns;
	}

	private double correctFactor() {
		return Math.max(1, ((double) numSubgraphs / reservoirSize));
	}

	public long getNumberofSubgraphs() {
		return (int) numSubgraphs;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.size();
	}
}
