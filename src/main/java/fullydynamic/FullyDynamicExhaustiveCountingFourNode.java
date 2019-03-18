package fullydynamic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import graphpattern.FourNodeGraphPattern;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;

public class FullyDynamicExhaustiveCountingFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	HashMap<Pattern, Integer> frequentPatterns;
	int numSubgraph;
	QuadripletGenerator subgraphGenerator;

	public FullyDynamicExhaustiveCountingFourNode() {
		this.nodeMap = new NodeMap();
		utility = new EdgeHandler();
		numSubgraph = 0;
		frequentPatterns = new HashMap<Pattern, Integer>();
	}

	@Override
	public boolean addEdge(StreamEdge edge) {
		// System.out.println("+" + edge);
		if (nodeMap.contains(edge)) {
			return false;
		}

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

		utility.handleEdgeAddition(edge, nodeMap);
		// System.out.println(counter);
		return false;
	}

	@Override
	public boolean removeEdge(StreamEdge edge) {
		if (!nodeMap.contains(edge))
			return false;
		// System.out.println("-" + edge);
		utility.handleEdgeDeletion(edge, nodeMap);
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
			removeSubgraph(subgraph);
			Quadriplet quadripletMinusEdge = subgraph.getQuadripletMinusEdge(edge);
			if (quadripletMinusEdge.isQuadriplet()) {
				addSubgraph(quadripletMinusEdge);
			}
		}

		// System.out.println(counter);
		return true;
	}

	void removeSubgraph(Quadriplet t) {
		/*
		 * if(counter.containsKey(t)) { int count = counter.get(t); if(count > 1)
		 * counter.put(t, count-1); else counter.remove(t); numSubgraph--;
		 * removeFrequentPattern(t); } else { System.out.println("remove error " + t);
		 * System.out.println(counter); System.exit(1); }
		 */
		// removeFrequentPattern(t);
		numSubgraph--;
		removeFrequentPattern(t);

	}

	void addSubgraph(Quadriplet t) {
		/*
		 * if(counter.containsKey(t)) { int count = counter.get(t); counter.put(t,
		 * count+1); }else { counter.put(t, 1); }
		 */
		addFrequentPattern(t);
		numSubgraph++;
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
	public HashMap<Pattern, Integer> getFrequentPatterns() {
		return this.frequentPatterns;
	}

	@Override
	public int getNumberofSubgraphs() {
		return this.numSubgraph;
	}

	@Override
	public int getCurrentReservoirSize() {
		return 0;
	}

}
