package incrementaltopkgraphpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import reservoir.SubgraphReservoir;
import struct.GraphPattern;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeBottomK;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.ReservoirSampling;
import utility.SetFunctions;
import utility.AlgorithmZ;
import utility.BottomKSketch;

public class IncrementalSubgraphReservoirFinalAlgorithm implements TopkGraphPatterns {
	NodeMap nodeMap;
	NodeBottomK nodeBottomK;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Triplet> reservoir;
	Random rand;

	THashMap<GraphPattern, Integer> frequentPatterns;
	int N; // total number of subgraphs
	int M; // maximum reservoir size
	int sum;
	AlgorithmZ skipRS;
	public IncrementalSubgraphReservoirFinalAlgorithm(int size, int k ) { 
		this.nodeMap = new NodeMap();
		this.nodeBottomK = new NodeBottomK();
		rand = new Random();
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Triplet>();
		N = 0;
		M = size;
		frequentPatterns = new THashMap<GraphPattern, Integer>();
		sum = 0;
		skipRS = new AlgorithmZ(M);
	}

	public boolean addEdge(StreamEdge edge) {
		if(nodeMap.contains(edge)) {
			return false;
		}
		//System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		//replaces the existing wedges in the reservoir with the triangles
		THashSet<Triplet> candidateTriangles = reservoir.getAllTriplets(src);
		ArrayList<Triplet> oldWedges = new ArrayList<Triplet>();
		//System.out.println("size "  + candidateTriangles.size());
		for(Triplet t: candidateTriangles) {
			if((t.a.equals(dst) || t.b.equals(dst) || t.c.equals(dst)) && !t.isTriangle()) {
				oldWedges.add(t);
			}
		}
		if(oldWedges.size() > 0) {
			for(Triplet t: oldWedges) {
				Triplet newTriangle = new Triplet(t.a,t.b,t.c,t.edgeA, t.edgeB,edge);
				replaceSubgraphs(t, newTriangle);
			}
		}

		//BottomKSketch<LabeledNeighbor> srcSketch = nodeBottomK.getSketch(src);
		//BottomKSketch<LabeledNeighbor> dstSketch = nodeBottomK.getSketch(dst);
		//int W = srcSketch.unionImprovedCardinality(dstSketch)-srcSketch.intersectionImprovedCardinality(dstSketch);
		SetFunctions<LabeledNeighbor> fun = new SetFunctions<LabeledNeighbor>();
		THashSet<LabeledNeighbor> union = fun.unionSet(srcNeighbor, dstNeighbor);
		int W = union.size()-fun.intersection(srcNeighbor, dstNeighbor);
		//System.out.println("W "+ W + " " + srcNeighbor + " "  + dstNeighbor);

		//System.out.println("W "  + W);
		if(W> 0) {
			int i = 0 ;
			while(sum <W) {
				i++;
				int zrs = skipRS.apply(N);
				N = N+zrs+1;
				sum = sum+zrs+1;
			}
			//added i wedges to the reservoir
			//we would randomly pick a vertex from the neighborhood of src and dst
			//and add it to the reservoir
			//System.out.println("i " + i + " W " + W);
			THashSet<LabeledNeighbor> set = new THashSet<LabeledNeighbor>();
			int count = 0 ;
			while(count < i) {
				LabeledNeighbor randomVertex = getRandomNeighbor(srcNeighbor, dstNeighbor);
				if(randomVertex == null) {
					break;
				}else if (set.contains(randomVertex)) {
					//wedge already added
					
				}else {
					set.add(randomVertex);
					THashSet<LabeledNode> randomVertexNeighbor = nodeMap.getNodeNeighbors(randomVertex.getDst());
					if(randomVertexNeighbor.contains(src) && randomVertexNeighbor.contains(dst)) {
						//triangle -> hence, rejected!!!!!
					}else if (randomVertexNeighbor.contains(src)) {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addToReservoir(triplet);
						count++;
					}else {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addToReservoir(triplet);
						count++;
					}
				}
			}
			sum = sum-W;
		}

		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(reservoir.size() + "  N " + N);
		nodeBottomK.addEdge(src, dst, edge);
		return false;
	}
	void addToReservoir(Triplet triplet) { 
		if(reservoir.size() >= M) {
			Triplet temp = reservoir.getRandom();
			reservoir.remove(temp);
			removeFrequentPattern(temp);
		}
		reservoir.add(triplet); 
		addFrequentPattern(triplet);

	}
	public THashSet<LabeledNode> getNeighbors(THashSet<LabeledNeighbor> randomVertexNeighborWithEdgeLabels) {
		THashSet<LabeledNode> results = new THashSet<LabeledNode>();
		for(LabeledNeighbor a: randomVertexNeighborWithEdgeLabels) {
			results.add(a.getDst());
		}
		return results;
	}
	public boolean removeEdge(StreamEdge edge) {
		return false;
	}

	public LabeledNeighbor getRandomNeighbor(THashSet<LabeledNeighbor> srcNeighbor, THashSet<LabeledNeighbor> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if(d_u+d_v == 0) {
			return null;
		}

		double value = d_u/(double)(d_u+d_v);
		if(Math.random() < value) {
			//select neighbor of u or src
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(srcNeighbor);
			return list.get(rand.nextInt(list.size()));
		}else {
			//select a neighbor of v or dst
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(dstNeighbor);
			return list.get(rand.nextInt(list.size()));
		}
	}

	//remove a and add b
	void replaceSubgraphs(Triplet a, Triplet b) {
		reservoir.remove(a);
		removeFrequentPattern(a);
		reservoir.add(b);
		addFrequentPattern(b);

	}

	void addFrequentPattern(Triplet t) {
		GraphPattern p = new GraphPattern(t);
		if(frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1);
		}
	}

	void removeFrequentPattern(Triplet t) {
		GraphPattern p = new GraphPattern(t);
		if(frequentPatterns.contains(p)) {
			int count = frequentPatterns.get(p);
			if(count >1)
				frequentPatterns.put(p, count-1);
			else 
				frequentPatterns.remove(p);
		}
	}

	public THashMap<GraphPattern, Integer> getFrequentPatterns() {
		correctEstimates();
		return this.frequentPatterns;
	}
	private void correctEstimates() {
		double correctFactor = correctFactor();
		List<GraphPattern> patterns = new ArrayList<GraphPattern>(frequentPatterns.keySet());
		for(GraphPattern p: patterns) {
			int count = frequentPatterns.get(p);
			double value = count*correctFactor;
			frequentPatterns.put(p, (int)value);
		}
	}
	private double correctFactor() { 
		return Math.max(1, ((double)N/M));
	}

	public int getNumberofSubgraphs() {
		return N;
	}
}
