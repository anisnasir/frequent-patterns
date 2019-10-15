package incremental;

import java.util.ArrayList;
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

public class IncrementalSubgraphReservoirFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	//NodeBottomK nodeBottomK;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Quadriplet> reservoir;
	Random rand;
	QuadripletGenerator subgraphGenerator;

	THashMap<Pattern, Long> frequentPatterns;
	long numSubgraphs; // total number of subgraphs
	int reservoirSize; // maximum reservoir size
	AlgorithmZ skipRS;
	int seed = 227;
	Random generator = new Random(seed);

	public IncrementalSubgraphReservoirFourNode(int size, int k) {
		this.nodeMap = new NodeMap();
		//this.nodeBottomK = new NodeBottomK();
		rand = new Random();
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Quadriplet>();
		numSubgraphs = 0;
		reservoirSize = size;
		frequentPatterns = new THashMap<Pattern, Long>();
		skipRS = new AlgorithmZ(reservoirSize);
	}

	@Override
	public boolean addEdge(StreamEdge edge) {
		if (nodeMap.contains(edge)) {
			return false;
		}
		QuadripletGenerator subgraphGenerator = new QuadripletGenerator();
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		THashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		// replaces the existing wedges in the reservoir with the triangles
		THashSet<Quadriplet> candidateSubgraphs = reservoir.getAllSubgraphs(src);
		ArrayList<Quadriplet> oldSubgraphs = new ArrayList<Quadriplet>();
		for (Quadriplet t : candidateSubgraphs) {
			if (t.getAllVertices().contains(dst)) {
				oldSubgraphs.add(t);
			}
		}
		if (!oldSubgraphs.isEmpty()) {
			for (int i = 0 ; i < oldSubgraphs.size(); i++ ) {
				Quadriplet t = oldSubgraphs.get(i);
				Quadriplet newQuadriplet = t.getQuadripletPlusEdge(edge);
				replaceSubgraphs(t, newQuadriplet);
			}
		}	
		int W = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);

		if (W > 0) {
			List<Quadriplet> newSubgraphs = subgraphGenerator.getNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor,
					dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
			
			for(int i = 0 ; i < newSubgraphs.size() ;i++) {
				Quadriplet quadriplet = newSubgraphs.get(i);
				numSubgraphs++;
				if(numSubgraphs <= this.reservoirSize) {
					//System.out.println("first phase of reservoir: " + numSubgraphs + "  <  "  + this.reservoirSize);
					addToReservoir(quadriplet);
				} else if (generator.nextDouble() < (this.reservoirSize / (double) this.numSubgraphs)){
					
					//System.out.println("second phase of reservoir: " + Math.random() + "< (" + this.reservoirSize + "/ (double) " + this.numSubgraphs);
					Quadriplet removedQuadriplet = reservoir.getRandom();
					reservoir.remove(removedQuadriplet);
					removeFrequentPattern(removedQuadriplet);
					addToReservoir(quadriplet);
				}
			}
		}

		utility.handleEdgeAddition(edge, nodeMap);
		return false;

	}

	void addToReservoir(Quadriplet quadriplet) {
		reservoir.add(quadriplet);
		addFrequentPattern(quadriplet);

	}

	@Override
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

	@Override
	public THashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	@Override
	public THashMap<Pattern, Long> correctEstimates() {
		THashMap<Pattern, Long> correctFrequentPatterns = new THashMap<Pattern, Long>();
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

	@Override
	public long getNumberofSubgraphs() {
		return numSubgraphs;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.size();
	}
	
	public AdvancedSubgraphReservoir<Quadriplet> getReservoir() {
		return reservoir;
	}
}
