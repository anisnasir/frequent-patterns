package fullydynamictopkgraphpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import reservoir.SubgraphReservoir;
import struct.GraphPattern;
import struct.LabeledNeighbor;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Triplet;
import topkgraphpattern.TopkGraphPatterns;
import utility.EdgeHandler;
import utility.SetFunctions;

public class FullyDynamicSubgraphReservoirAlgorithm implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	SubgraphReservoir<Triplet> reservoir;
	THashMap<GraphPattern, Integer> frequentPatterns;
	int N; // total number of subgraphs
	int M; // maximum reservoir size
	int Ncurrent;
	int c1;
	int c2;
	public FullyDynamicSubgraphReservoirAlgorithm(int size, int k ) { 
		this.nodeMap = new NodeMap();
		utility = new EdgeHandler();
		reservoir = new SubgraphReservoir<Triplet>();
		N = 0;
		M = size;
		c1=0;
		Ncurrent = 0 ;
		c2=0;
		frequentPatterns = new THashMap<GraphPattern, Integer>();
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
		
		//System.out.println("src neighbor" + srcNeighbor);
		//System.out.println("dst neighbor " + dstNeighbor);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();
		
		//System.out.println("common " +  common);

		for(LabeledNeighbor t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			} else {
				//System.out.println( " neighbor put "  + t );
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNeighbor t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getDst().getVertexId() , t.getDst().getVertexLabel(), t.getEdgeLabel()));
				addSubgraph(triplet);
			}else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(t.getDst().getVertexId() , t.getDst().getVertexLabel(), src.getVertexId(), src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(t.getDst().getVertexId(), t.getDst().getVertexLabel(), dst.getVertexId(), dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				if(reservoir.contains(tripletWedge)) {
					Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
					replaceSubgraphs(tripletWedge, tripletTriangle);
					//System.out.println("triangle added" + tripletTriangle);
				}

			}
		}
		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(reservoir.size() + "  N " + N);
		return false;
	}
	public boolean removeEdge(StreamEdge edge) {
		//System.out.println("-" + edge);
		if(!nodeMap.contains(edge)) {
			return false;
		}
		utility.handleEdgeDeletion(edge, nodeMap);

		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());


		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		SetFunctions<LabeledNeighbor> functions = new SetFunctions<LabeledNeighbor>();
		Set<LabeledNeighbor> common = functions.intersectionSet(srcNeighbor, dstNeighbor);

		THashMap<LabeledNeighbor, LabeledNeighbor> srcCommonNeighbor = new THashMap<LabeledNeighbor, LabeledNeighbor>();

		for(LabeledNeighbor t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getDst().getVertexId() , t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			} else {
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNeighbor t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(dst.getVertexId(),dst.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				removeSubgraph(triplet);
			}else {
				LabeledNeighbor srcComNeighbor = srcCommonNeighbor.get(t);
				LabeledNode a = src;
				LabeledNode b = dst;
				LabeledNode c = t.getDst();
				StreamEdge edgeA = edge;
				StreamEdge edgeB = new StreamEdge(c.getVertexId(), c.getVertexLabel(), src.getVertexId(), src.getVertexLabel(), srcComNeighbor.getEdgeLabel());
				StreamEdge edgeC = new StreamEdge(c.getVertexId(), c.getVertexLabel(), dst.getVertexId(), dst.getVertexLabel(), t.getEdgeLabel());

				Triplet tripletWedge = new Triplet(a, b, c, edgeB, edgeC );
				Triplet tripletTriangle = new Triplet(a, b, c,edgeA, edgeB, edgeC );
				if(reservoir.contains(tripletTriangle))
					replaceSubgraphs(tripletTriangle, tripletWedge);

			}
		}

		//System.out.println(reservoir.size());
		return false;
	}
	void removeSubgraph(Triplet t) {
		if(reservoir.contains(t)) {
			//System.out.println("remove called from remove subgraph");
			reservoir.remove(t);
			removeFrequentPattern(t);
			c1++;
		}else 
			c2++;
		
		Ncurrent--;
	}

	void addSubgraph(Triplet t) {
		N++;
		Ncurrent++;
		
		boolean flag = false;
		if (c1+c2 ==0) {
			if(reservoir.size() < M ) {
				flag = true;
			}else if (Math.random() < (M/(double)N)) {
				flag = true;
				//System.out.println("remove called from add subgraph");
				Triplet temp = reservoir.getRandom();
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
		return Math.max(1, ((double)Ncurrent/M));
	}
	
	public int getNumberofSubgraphs() {
		return Ncurrent;
	}
}
