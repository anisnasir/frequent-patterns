package fullydynamictopkgraphpattern;

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
import utility.AlgorithmD;
import utility.AlgorithmZ;
import utility.BottomKSketch;

public class FullyDynamicSubgraphReservoirFinalAlgorithm implements TopkGraphPatterns {
	NodeMap nodeMap;
	EdgeHandler utility;
	AdvancedSubgraphReservoir<Triplet> reservoir;
	THashMap<GraphPattern, Integer> frequentPatterns;
	NodeBottomK nodeBottomK;
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
	ReservoirSampling<LabeledNeighbor> sampler;

	public FullyDynamicSubgraphReservoirFinalAlgorithm(int size, int k ) { 
		this.nodeMap = new NodeMap();
		this.nodeBottomK = new NodeBottomK();
		this.rand = new Random();
		this.rand.setSeed(17);
		utility = new EdgeHandler();
		reservoir = new AdvancedSubgraphReservoir<Triplet>();
		N = 0;
		M = size;
		c1=0;
		Ncurrent = 0 ;
		c2=0;
		sum = 0;
		Zprime=-1;
		frequentPatterns = new THashMap<GraphPattern, Integer>();
		skipRS = new AlgorithmZ(M);
		skipRP = new AlgorithmD();
		sampler = new ReservoirSampling<LabeledNeighbor>();
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
		THashSet<LabeledNeighbor> intersection = fun.intersectionSet(srcNeighbor, dstNeighbor);
		int W = union.size()-intersection.size();
		//System.out.println("W "+ W + " " + srcNeighbor + " "  + dstNeighbor);

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
				//added i wedges to the reservoir
				//we would randomly pick a vertex from the neighborhood of src and dst
				//and add it to the reservoir
				//System.out.println("i " + i + " W " + W);
				if(i < (W/4)) {
					//THashSet<LabeledNeighbor> set = new THashSet<LabeledNeighbor>();
					int count = 0 ;
					while(count < i) {
						LabeledNeighbor randomVertex = getRandomNeighbor(srcNeighbor, dstNeighbor);
						if(randomVertex == null) {
							break;
					//	}else if (set.contains(randomVertex)) {
							//wedge already added
						}else {
					//		set.add(randomVertex);
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
				}else {
					ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>();
					for(LabeledNeighbor a: union) {
						if(!intersection.contains(a)) {
							list.add(a);
						}
					}
					List<LabeledNeighbor> neighbors = sampler.selectKItems(list, i);
					for(LabeledNeighbor n:neighbors) {
						THashSet<LabeledNode> randomVertexNeighbor = nodeMap.getNodeNeighbors(n.getDst());
						if(randomVertexNeighbor.contains(src) && randomVertexNeighbor.contains(dst)) {
							//triangle -> hence, rejected!!!!!
						}else if (randomVertexNeighbor.contains(src)) {
							Triplet triplet = new Triplet(src, dst, n.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), n.getDst().getVertexId(), n.getDst().getVertexLabel(), n.getEdgeLabel()));
							addToReservoir(triplet);
						}else {
							Triplet triplet = new Triplet(src, dst, n.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), n.getDst().getVertexId(), n.getDst().getVertexLabel(), n.getEdgeLabel()));
							addToReservoir(triplet);
						}
					}
					
				}
				sum = sum-W;
			}
		}else {
			int count = 0 ; 
			//THashSet<LabeledNeighbor> set = new THashSet<LabeledNeighbor>();
			while(count < W) {
				LabeledNeighbor randomVertex = getRandomNeighbor(srcNeighbor, dstNeighbor);

				if(randomVertex == null) {
					break;
				//}	else if (set.contains(randomVertex)) {

				} else {
				//	set.add(randomVertex);
					//System.out.println(srcNeighbor + " " + dstNeighbor);
					THashSet<LabeledNode> randomVertexNeighbor = nodeMap.getNodeNeighbors(randomVertex.getDst());
					if(randomVertexNeighbor.contains(src) && randomVertexNeighbor.contains(dst)) {
						//triangle -> hence, rejected!!!!!
					}else if (randomVertexNeighbor.contains(src)) {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addSubgraph(triplet);
						count++;
					}else {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addSubgraph(triplet);
						count++;
					}
				}
			}
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
			if(Zprime < 0) {
				Zprime = skipRP.vitter_d_skip(c1,c1+c2);
			}

			if(Zprime == 0) {
				flag = true;
				c1--;
			}else {
				c2--;
			}

			Zprime--;
		}

		if(flag) {
			reservoir.add(t); 
			addFrequentPattern(t);
			//System.out.println("reservoir size after add method " + reservoir.size());
		}
	}


	public LabeledNeighbor getRandomNeighbor(THashSet<LabeledNeighbor> srcNeighbor, THashSet<LabeledNeighbor> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if(d_u+d_v == 0) {
			return null;
		}

		double value = d_u/(double)(d_u+d_v);
		double ran = Math.random();
		if(ran <= value) {
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(srcNeighbor);
			return list.get(rand.nextInt(list.size()));
		}else {
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(dstNeighbor);
			return list.get(rand.nextInt(list.size()));
		}
	}

	public boolean removeEdge(StreamEdge edge) {
		//System.out.println("-" + edge);
		if(!nodeMap.contains(edge)) {
			return false;
		}
		utility.handleEdgeDeletion(edge, nodeMap);

		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		nodeBottomK.removeEdge(src, dst, edge);


		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		//BottomKSketch<LabeledNeighbor> srcSketch = nodeBottomK.getSketch(src);
		//BottomKSketch<LabeledNeighbor> dstSketch = nodeBottomK.getSketch(dst);
		//int W = srcSketch.unionImprovedCardinality(dstSketch)-srcSketch.intersectionImprovedCardinality(dstSketch);

		SetFunctions<LabeledNeighbor> fun = new SetFunctions<LabeledNeighbor>();
		THashSet<LabeledNeighbor> union = fun.unionSet(srcNeighbor, dstNeighbor);
		int W = union.size()-fun.intersection(srcNeighbor, dstNeighbor);
		Ncurrent-=W;

		//remove all the wedges from the graphs
		THashSet<Triplet> candidateWedges = reservoir.getAllTriplets(src);
		ArrayList<Triplet> wedges = new ArrayList<Triplet>();
		for(Triplet t: candidateWedges) {
			if((t.edgeA.equals(edge) || t.edgeB.equals(edge)) && !t.isTriangle()) {
				wedges.add(t);
			}
		}

		if(wedges.size() >0) {
			int count = wedges.size();
			for(Triplet t: wedges) {
				reservoir.remove(t);
				removeFrequentPattern(t);
				Zprime = -1;
			}
			c1+=count;
			c2+= (W-count);
		}
		//finished removing all the wedges
		//System.out.println(reservoir.size());

		//update all triangles in the reservoir and replace them with the wedges
		THashSet<Triplet> candidateTriangles = reservoir.getAllTriplets(src);
		ArrayList<Triplet> triangles = new ArrayList<Triplet>();
		//System.out.println("size "  + candidateTriangles.size());
		for(Triplet t: candidateTriangles) {
			if(t.isTriangle()) {
				if((t.a.equals(dst) || t.b.equals(dst) || t.c.equals(dst))) {
					triangles.add(t);
				}
			}
		}
		if(triangles.size() > 0) {
			for(Triplet t: triangles) {
				Triplet newWedge = null;
				if(edge.equals(t.edgeA)) {
					newWedge = new Triplet(t.a,t.b,t.c,t.edgeB, t.edgeC);
				}else if (edge.equals(t.edgeB)) {
					newWedge = new Triplet(t.a,t.b,t.c,t.edgeA, t.edgeC);
				}else {
					newWedge = new Triplet(t.a,t.b,t.c,t.edgeA, t.edgeB);
				}
				replaceSubgraphs(t, newWedge);
			}
		}
		//end updating triangles
		return false;
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
