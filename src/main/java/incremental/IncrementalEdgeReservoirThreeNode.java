package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class IncrementalEdgeReservoirThreeNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	HashMap<Pattern, Long> frequentPatterns;
	int k;
	int reservoirSize;
	int numEdges;

	public IncrementalEdgeReservoirThreeNode(int size, int k) {
		nodeMap = new NodeMap();
		reservoir = new EdgeReservoir<StreamEdge>();
		utility = new EdgeHandler();
		this.k = k;
		this.reservoirSize = size;
		this.numEdges = 0;
		frequentPatterns = new HashMap<Pattern, Long>();
	}

	public boolean addEdge(StreamEdge edge) {
		numEdges++;
		// System.out.println("+" + edge);

		if (reservoir.getSize() <= reservoirSize) {
			reservoir.add(edge);
			addTriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
		} else if (Math.random() < (reservoirSize / (double) numEdges)) {
			// remove a random edge from reservoir
			StreamEdge oldEdge = reservoir.getRandom();
			reservoir.remove(oldEdge);
			utility.handleEdgeDeletion(oldEdge, nodeMap);
			removeTriplet(oldEdge);

			// add the new edge in the reservoir
			reservoir.add(edge);
			addTriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
		}

		return false;
	}

	void addTriplets(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());

		HashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		for (LabeledNode t : srcNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t, edge, new StreamEdge(src.getVertexId(),
						src.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				addSubgraph(triplet);
			} 
		}

		for (LabeledNode t : dstNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t, edge, new StreamEdge(dst.getVertexId(),
						dst.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				addSubgraph(triplet);
			} else {
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getVertexId(), t.getVertexLabel(),
						src.getVertexId(), src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(t.getVertexId(), t.getVertexLabel(),
						dst.getVertexId(), dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC);
				Triplet tripletTriangle = new Triplet(a, b, c, edgeA, edgeB, edgeC);
				replaceSubgraphs(tripletWedge, tripletTriangle);

			}
		}
	}

	void removeTriplet(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());

		HashSet<LabeledNode> srcNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNode> functions = new SetFunctions<LabeledNode>();
		Set<LabeledNode> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		for (LabeledNode t : srcNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t, edge, new StreamEdge(src.getVertexId(),
						src.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				removeSubgraph(triplet);
			}
		}

		for (LabeledNode t : dstNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t, edge, new StreamEdge(dst.getVertexId(),
						dst.getVertexLabel(), t.getVertexId(), t.getVertexLabel()));
				removeSubgraph(triplet);
			} else {
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t;
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(),
						src.getVertexLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(),
						dst.getVertexLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC);
				Triplet tripletTriangle = new Triplet(a, b, c, edgeA, edgeB, edgeC);
				replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}
	}

	public long getNumberofSubgraphs() {
		// TODO Auto-generated method stub
		return numEdges;
	}

	void addSubgraph(Triplet t) {
		addFrequentPattern(t);
	}

	void removeSubgraph(Triplet t) {
		removeFrequentPattern(t);
	}

	// remove a and add b
	void replaceSubgraphs(Triplet a, Triplet b) {
		removeSubgraph(a);
		removeFrequentPattern(a);
		addSubgraph(b);
		addFrequentPattern(b);

	}

	void addFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
		if (frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1l);
		}
	}

	void removeFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
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
		double wedgeCorrectFactor = correctFactorWedge();
		double triangleCorrectFactor = correctFactorTriangle();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern pattern : patterns) {
			ThreeNodeGraphPattern p = (ThreeNodeGraphPattern) pattern;
			long count = frequentPatterns.get(p);
			double value;
			if (p.isWedge())
				value = count * wedgeCorrectFactor;
			else
				value = count * triangleCorrectFactor;

			correctFrequentPatterns.put(p, (long) value);
		}
		return correctFrequentPatterns;
	}

	private double correctFactorWedge() {
		double result = (numEdges / (double) reservoirSize) * ((numEdges - 1) / (double) (reservoirSize - 1));
		return Math.max(1, result);
	}

	private double correctFactorTriangle() {
		double result = (numEdges / (double) reservoirSize) * ((numEdges - 1) / (double) (reservoirSize - 1)) * ((numEdges - 2) / (double) (reservoirSize - 2));
		return Math.max(result, 1);
	}

	@Override
	public boolean removeEdge(StreamEdge edge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.getSize();
	}

}
