package incrementaltopkgraphpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.EdgeReservoir;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.Subgraph;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class IncrementalTriesteAlgorithm implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	EdgeReservoir<StreamEdge> reservoir;
	THashMap<Pattern, Integer> frequentPatterns;
	int k;
	int M;
	int N;

	public IncrementalTriesteAlgorithm(int size, int k) {
		nodeMap = new NodeMap();
		reservoir = new EdgeReservoir<StreamEdge>();
		utility = new EdgeHandler();
		this.k = k;
		this.M = size;
		this.N = 0;
		frequentPatterns = new THashMap<Pattern, Integer>();
	}

	public boolean addEdge(StreamEdge edge) {
		N++;
		// System.out.println("+" + edge);

		if (reservoir.getSize() < M) {
			reservoir.add(edge);
			addTriplets(edge);
			utility.handleEdgeAddition(edge, nodeMap);
		} else if (Math.random() < (M / (double) N)) {
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

		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();

		for (LabeledNeighbor t : srcNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(), edge, new StreamEdge(src.getVertexId(),
						src.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for (LabeledNeighbor t : dstNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(), edge, new StreamEdge(dst.getVertexId(),
						dst.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			} else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getDst().getVertexId(), t.getDst().getVertexLabel(),
						src.getVertexId(), src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(t.getDst().getVertexId(), t.getDst().getVertexLabel(),
						dst.getVertexId(), dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC);
				Triplet tripletTriangle = new Triplet(a, b, c, edgeA, edgeB, edgeC);
				replaceSubgraphs(tripletWedge, tripletTriangle);

			}
		}
	}

	void removeTriplet(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());

		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();

		for (LabeledNeighbor t : srcNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(), edge, new StreamEdge(src.getVertexId(),
						src.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for (LabeledNeighbor t : dstNeighbor) {
			if (!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(), edge, new StreamEdge(dst.getVertexId(),
						dst.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			} else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(),
						src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(),
						dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC);
				Triplet tripletTriangle = new Triplet(a, b, c, edgeA, edgeB, edgeC);
				replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}
	}

	public int getNumberofSubgraphs() {
		// TODO Auto-generated method stub
		return N;
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
		if (frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Triplet t) {
		ThreeNodeGraphPattern p = new ThreeNodeGraphPattern(t);
		if (frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			if (count > 1)
				frequentPatterns.put(p, count - 1);
			else
				frequentPatterns.remove(p);
		}
	}

	public THashMap<Pattern, Integer> getFrequentPatterns() {
		correctEstimates();
		return this.frequentPatterns;
	}

	private void correctEstimates() {
		double wedgeCorrectFactor = correctFactorWedge();
		double triangleCorrectFactor = correctFactorTriangle();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for (Pattern pattern : patterns) {
			ThreeNodeGraphPattern p = (ThreeNodeGraphPattern) pattern;
			int count = frequentPatterns.get(p);
			double value;
			if (p.isWedge())
				value = count * wedgeCorrectFactor;
			else
				value = count * triangleCorrectFactor;

			frequentPatterns.put(p, (int) value);
		}
	}

	private double correctFactorWedge() {
		double result = (N / (double) M) * ((N - 1) / (double) (M - 1));
		return Math.max(1, result);
	}

	private double correctFactorTriangle() {
		double result = (N / (double) M) * ((N - 1) / (double) (M - 1)) * ((N - 2) / (double) (M - 2));
		return Math.max(result, 1);
	}

	@Override
	public boolean removeEdge(StreamEdge edge) {
		// TODO Auto-generated method stub
		return false;
	}

}
