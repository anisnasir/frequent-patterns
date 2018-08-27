package incrementaltopkgraphpattern;

import java.util.List;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.FourNodeGraphPattern;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Path;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.Subgraph;
import topkgraphpattern.SubgraphType;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;
import utility.SetFunctions;

public class IncrementalExhaustiveCountingFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	THashMap<Pattern, Integer> frequentPatterns;
	int numSubgraph;
	QuadripletGenerator subgraphGenerator;

	public IncrementalExhaustiveCountingFourNode() {
		utility = new EdgeHandler();
		numSubgraph = 0;
		frequentPatterns = new THashMap<Pattern, Integer>();
		this.nodeMap = new NodeMap();
		subgraphGenerator = new QuadripletGenerator();
	}

	public boolean addEdge(StreamEdge edge) {
		// System.out.println("+" + edge);
		if (nodeMap.contains(edge)) {
			return false;
		}

		// System.out.println(nodeMap.map);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNeighbor> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		THashSet<Path> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		THashSet<LabeledNeighbor> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		THashSet<Path> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		
		Set<Quadriplet> subgraphs = subgraphGenerator.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		
		for(Quadriplet subgraph: subgraphs) {
			if(subgraph.getType() == SubgraphType.LINE || subgraph.getType() == SubgraphType.STAR) {
				addSubgraph(subgraph);
			} else {
				addSubgraph(subgraph);
				removeSubgraph(subgraph.getQuadripletMinusEdge(edge));
			}
		}

		utility.handleEdgeAddition(edge, nodeMap);
		// System.out.println(counter);
		return false;
	}

	void removeSubgraph(Quadriplet t) {
		numSubgraph--;
		removeFrequentPattern(t);
	}

	void addSubgraph(Quadriplet t) {
		addFrequentPattern(t);
		numSubgraph++;
	}

	void addFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if (frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		System.out.println(frequentPatterns);
		if (frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			if (count > 1)
				frequentPatterns.put(p, count - 1);
			else
				frequentPatterns.remove(p);
		} else {
			System.out.println("cannot find pattern");
		}
	}

	public THashMap<Pattern, Integer> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	public int getNumberofSubgraphs() {
		return this.numSubgraph;
	}

	@Override
	public boolean removeEdge(StreamEdge edge) {
		// This method is not implemented for incremental algorithms
		return false;
	}

}
