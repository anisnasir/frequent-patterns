package fullydynamic;

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
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.QuadripletGenerator;
import utility.ReservoirSampling;
import utility.SetFunctions;
import utility.AlgorithmD;
import utility.AlgorithmZ;
import utility.BottomKSketch;

public class FullyDynamicSubgraphReservoirFinalAlgorithmFourNode implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Quadriplet> reservoir;
	HashMap<Pattern, Long> frequentPatterns;
	int N; // total number of subgraphs
	int M; // maximum reservoir size
	int Ncurrent;
	public int c1;
	public int c2;
	int sum;
	AlgorithmZ skipRS;
	AlgorithmD skipRP;
	int Zprime;
	Random rand;
	ReservoirSampling<LabeledNode> sampler;

	public FullyDynamicSubgraphReservoirFinalAlgorithmFourNode(int size, int k ) { 
		this.nodeMap = new NodeMap();
		this.rand = new Random();
		this.rand.setSeed(17);
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Quadriplet>();
		N = 0;
		M = size;
		c1=0;
		Ncurrent = 0 ;
		c2=0;
		sum = 0;
		Zprime=-1;
		frequentPatterns = new HashMap<Pattern, Long>();
		skipRS = new AlgorithmZ(M);
		//skipRP = new AlgorithmD();
		sampler = new ReservoirSampling<LabeledNode>();
	}

	public boolean addEdge(StreamEdge edge) {
		if(nodeMap.contains(edge)) {
			return false;
		}

		QuadripletGenerator subgraphGenerator = new QuadripletGenerator();
		//System.out.println("+" + edge);
		// System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		
		int subgraphCount = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);

		//replaces the existing wedges in the reservoir with the triangles
		HashSet<Quadriplet> candidateSubgraphs = reservoir.getAllSubgraphs(src);
		ArrayList<Quadriplet> oldSubgraphs = new ArrayList<Quadriplet>();
		//System.out.println("size "  + candidateTriangles.size());
		for(Quadriplet t: candidateSubgraphs) {
			List<LabeledNode> allVertices = t.getAllVertices();
			if(allVertices.contains(dst)) {
				oldSubgraphs.add(t);
			}
		}
		if(oldSubgraphs.size() > 0) {
			for(Quadriplet t: oldSubgraphs) {
				replaceSubgraphAddingNewEdge(t, edge);
			}
		}
		
		int W = subgraphCount;
		if(c1+c2 == 0) {
			//System.out.println("W "  + W);
			if(W> 0) {
				int i = 0 ;
				while(sum <W) {
					i++;
					int zrs = skipRS.apply(N);
					N = N+zrs+1;
					Ncurrent = Ncurrent+zrs+1;
					sum = sum+zrs+1;
				}
				
				int count = 0;
				while (count < i) {
					Quadriplet randomSubgraph = subgraphGenerator.getRandomNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
					if(randomSubgraph!=null) {
						addToReservoir(randomSubgraph);
						count++;
					}
				}
				sum = sum-W;
			}
		}else {
			int count = 0 ;
			HashSet<Quadriplet> set = new HashSet<Quadriplet>();
			while(count < W) {
				Quadriplet randomSubgraph = subgraphGenerator.getRandomNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);

				if (set.contains(randomSubgraph)) {
					count++;
				} else {
					set.add(randomSubgraph);
					addSubgraph(randomSubgraph);
				}
			}
		}
		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(reservoir.size() + "  N " + N);
		return false;
	}
	void addToReservoir(Quadriplet triplet) { 
		if(reservoir.size() >= M) {
			Quadriplet temp = reservoir.getRandom();
			reservoir.remove(temp);
			removeFrequentPattern(temp);
		}
		reservoir.add(triplet); 
		addFrequentPattern(triplet);

	}

	void addSubgraph(Quadriplet t) {
		N++;
		Ncurrent++;

		boolean flag = false;
		if (c1+c2 ==0) {
			if(reservoir.size() < M ) {
				flag = true;
			}else if (Math.random() < (M/(double)N)) {
				flag = true;
				//System.out.println("remove called from add subgraph");
				Quadriplet temp = reservoir.getRandom();
				reservoir.remove(temp);
				removeFrequentPattern(temp);
			}
		}else {
			int d = c1+c2;
			if (Math.random() < (c1/(double)(d))) {
				flag = true;
				c1--;
			}else {
				c2--;
			}
		}

		if(flag) {
			reservoir.add(t); 
			addFrequentPattern(t);
			//System.out.println("reservoir size after add method " + reservoir.size());
		}
	}

	public LabeledNode getRandomNeighbor(HashSet<LabeledNode> srcNeighbor, HashSet<LabeledNode> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if(d_u+d_v == 0) {
			return null;
		}

		double value = d_u/(double)(d_u+d_v);
		double ran = Math.random();
		if(ran <= value) {
			ArrayList<LabeledNode> list = new ArrayList<LabeledNode>(srcNeighbor);
			return list.get(rand.nextInt(list.size()));
		}else {
			ArrayList<LabeledNode> list = new ArrayList<LabeledNode>(dstNeighbor);
			return list.get(rand.nextInt(list.size()));
		}
	}

	public boolean removeEdge(StreamEdge edge) {
		//System.out.println("-" + edge);
		if(!nodeMap.contains(edge)) {
			return false;
		}
		utility.handleEdgeDeletion(edge, nodeMap);

		QuadripletGenerator subgraphGenerator = new QuadripletGenerator();
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<LabeledNode> srcTwoHopNeighbor = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<LabeledNode> dstTwoHopNeighbor = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		
		int subgraphCount = subgraphGenerator.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor,
				dstOneHopNeighbor, srcTwoHopNeighbor, dstTwoHopNeighbor);
		
		int W = subgraphCount;
		
		Ncurrent -= W;

		//remove all the wedges from the graphs
		HashSet<Quadriplet> candidateWedges = reservoir.getAllSubgraphs(src);
		ArrayList<Quadriplet> existingSubgraphs = new ArrayList<Quadriplet>();
		for(Quadriplet t: candidateWedges) {
			if(t.getAllVertices().contains(dst)) {
				existingSubgraphs.add(t);
			}
		}

		if(existingSubgraphs.size() >0) {
			int count = 0;
			for(Quadriplet t: existingSubgraphs) {
				if(replaceSubgraphRemovingNewEdge(t, edge)) {
					count++;
				}
				Zprime = -1;
			}
			c1+=count;
			c2+= (W-count);
		}
		
		//end updating triangles
		return false;
	}

	//remove a and add b
	void replaceSubgraphAddingNewEdge(Quadriplet a, StreamEdge edge) {
		reservoir.remove(a);
		removeFrequentPattern(a);
		a.addEdge(edge);
		reservoir.add(a);
		addFrequentPattern(a);
	}
	
	// remove a and add b
	boolean replaceSubgraphRemovingNewEdge(Quadriplet a, StreamEdge edge) {
		reservoir.remove(a);
		removeFrequentPattern(a);
		a.removeEdge(edge);
		if (a.isQuadriplet()) {
			reservoir.add(a);
			addFrequentPattern(a);
			return false;
		}
		return true;
	}

	void addFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			frequentPatterns.put(p, count+1);
		}else {
			frequentPatterns.put(p, 1l);
		}
	}

	void removeFrequentPattern(Quadriplet t) {
		FourNodeGraphPattern p = new FourNodeGraphPattern(t);
		if(frequentPatterns.containsKey(p)) {
			long count = frequentPatterns.get(p);
			if(count >1)
				frequentPatterns.put(p, count-1);
			else 
				frequentPatterns.remove(p);
		}
	}

	public HashMap<Pattern, Long> getFrequentPatterns() {
		return this.frequentPatterns;
	}
	public void correctEstimates() {
		double correctFactor = correctFactor();
		List<Pattern> patterns = new ArrayList<Pattern>(frequentPatterns.keySet());
		for(Pattern p: patterns) {
			long count = frequentPatterns.get(p);
			double value = count*correctFactor;
			frequentPatterns.put(p, (long)value);
		}
	}
	private double correctFactor() { 
		return Math.max(1, ((double)Ncurrent/M));
	}

	public int getNumberofSubgraphs() {
		return Ncurrent;
	}

	@Override
	public int getCurrentReservoirSize() {
		return reservoir.size();
	}
}
