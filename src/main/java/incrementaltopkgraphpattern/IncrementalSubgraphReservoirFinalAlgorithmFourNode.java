package incrementaltopkgraphpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import graphpattern.FourNodeGraphPattern;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import reservoir.SubgraphReservoir;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeBottomK;
import struct.NodeMap;
import struct.Path;
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

	THashMap<Pattern, Integer> frequentPatterns;
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
		frequentPatterns = new THashMap<Pattern, Integer>();
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
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNeighbor> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		THashSet<Path> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		THashSet<LabeledNeighbor> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		THashSet<Path> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);

		int subgraphCount = subgraphGenerator.getAllSubgraphsCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);

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
				subgraphCount--;
			}
		}

		// BottomKSketch<LabeledNeighbor> srcSketch = nodeBottomK.getSketch(src);
		// BottomKSketch<LabeledNeighbor> dstSketch = nodeBottomK.getSketch(dst);
		// int W =
		// srcSketch.unionImprovedCardinality(dstSketch)-srcSketch.intersectionImprovedCardinality(dstSketch);
		// SetFunctions<LabeledNeighbor> fun = new SetFunctions<LabeledNeighbor>();
		// THashSet<LabeledNeighbor> union = fun.unionSet(srcNeighbor, dstNeighbor);
		// int W = union.size()-fun.intersection(srcNeighbor, dstNeighbor);
		// System.out.println("W "+ W + " " + srcNeighbor + " " + dstNeighbor);

		int W = subgraphCount;

		// System.out.println("W " + W);
		if (W > 0) {
			int i = 0;
			while (sum < W) {
				i++;
				int zrs = skipRS.apply(N);
				N = N + zrs + 1;
				sum = sum + zrs + 1;
			}
			// added i wedges to the reservoir
			// we would randomly pick a vertex from the neighborhood of src and dst
			// and add it to the reservoir
			// System.out.println("i " + i + " W " + W);
			THashSet<LabeledNeighbor> set = new THashSet<LabeledNeighbor>();
			int count = 0;
			while (count < i) {
				Quadriplet randomSubgraph = subgraphGenerator.getRandom(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
				addToReservoir(randomSubgraph);
				count++;
			}
			sum = sum - W;
		}

		utility.handleEdgeAddition(edge, nodeMap);
		// System.out.println(reservoir.size() + " N " + N);
		//nodeBottomK.addEdge(src, dst, edge);
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

	public THashSet<LabeledNode> getNeighbors(THashSet<LabeledNeighbor> randomVertexNeighborWithEdgeLabels) {
		THashSet<LabeledNode> results = new THashSet<LabeledNode>();
		for (LabeledNeighbor a : randomVertexNeighborWithEdgeLabels) {
			results.add(a.getDst());
		}
		return results;
	}

	public boolean removeEdge(StreamEdge edge) {
		return false;
	}

	public LabeledNeighbor getRandomNeighbor(THashSet<LabeledNeighbor> srcNeighbor,
			THashSet<LabeledNeighbor> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if (d_u + d_v == 0) {
			return null;
		}

		double value = d_u / (double) (d_u + d_v);
		if (Math.random() < value) {
			// select neighbor of u or src
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(srcNeighbor);
			return list.get(rand.nextInt(list.size()));
		} else {
			// select a neighbor of v or dst
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(dstNeighbor);
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
		if (frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count + 1);
		} else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
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
