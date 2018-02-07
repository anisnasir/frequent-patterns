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
	int c1;
	int c2;
	int sum;
	AlgorithmZ skipRS;
	//AlgorithmD skipRP;
	int Zprime;
	Random rand;
	ReservoirSampling<Triplet> sampler;

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
		//skipRP = new AlgorithmD();
		sampler = new ReservoirSampling<Triplet>();
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

		List<Triplet> list = new ArrayList<Triplet>();

		for(LabeledNeighbor t: srcNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), t.getDst().getVertexId(), t.getDst().getVertexLabel(), t.getEdgeLabel()));
				list.add(triplet);
			} else {
				//System.out.println( " neighbor put "  + t );
				srcCommonNeighbor.put(t, t);
			}
		}

		for(LabeledNeighbor t: dstNeighbor) {
			if(!common.contains(t)) {
				Triplet triplet = new Triplet(src, dst, t.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), t.getDst().getVertexId() , t.getDst().getVertexLabel(), t.getEdgeLabel()));
				list.add(triplet);
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
				}

			}
		}

		if(c1+c2 == 0) {
			int i = 0 ;
			int W = list.size();
			//System.out.println("list " + list);
			if(W> 0) {
				while(sum < W) {
					i++;
					int zrs = skipRS.apply(N);
					N = N+zrs+1;
					Ncurrent= Ncurrent+zrs+1;
					sum = sum+zrs+1;
				}
				//System.out.println("i equals "+ i);
				List<Triplet> sample = sampler.selectKItems(list, i);

				for(Triplet t: sample) {
					if(reservoir.size() >= M) {
						Triplet temp = reservoir.getRandom();
						reservoir.remove(temp);
						removeFrequentPattern(temp);
					}
					reservoir.add(t); 
					addFrequentPattern(t);

				}
				sum = sum-W;
			}
		}else {
			for(Triplet t: list) {
				addSubgraph(t);
			}
		}
		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(reservoir.size() + "  N " + N);
		return false;
	}
	/*public boolean addEdge(StreamEdge edge) {
		if(nodeMap.contains(edge)) {
			return false;
		}
		//System.out.println("+" + edge);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(),edge.getDstLabel());

		THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		//update all triangles in the reservoir
		THashSet<Triplet> candidateTriangles = reservoir.getAllTriplets(src);
		ArrayList<Triplet> wedges = new ArrayList<Triplet>();
		//System.out.println("size "  + candidateTriangles.size());
		for(Triplet t: candidateTriangles) {
			if((t.a.equals(dst) || t.b.equals(dst) || t.c.equals(dst))) {
				wedges.add(t);
			}
		}
		if(wedges.size() > 0) {
			for(Triplet t: wedges) {
				Triplet newTriangle = new Triplet(t.a,t.b,t.c,t.edgeA, t.edgeB,edge);
				replaceSubgraphs(t, newTriangle);
			}
		}

		//BottomKSketch<LabeledNeighbor> srcSketch = nodeBottomK.getSketch(src);
		//BottomKSketch<LabeledNeighbor> dstSketch = nodeBottomK.getSketch(dst);
		//int W = srcSketch.unionImprovedCardinality(dstSketch);
		SetFunctions<LabeledNeighbor> fun = new SetFunctions<LabeledNeighbor>();
		THashSet<LabeledNeighbor> union = fun.unionSet(srcNeighbor, dstNeighbor);
		int W = union.size();

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
				THashSet<LabeledNeighbor> set = new THashSet<LabeledNeighbor>();
				int count = 0 ;
				while(count < i) {
					LabeledNeighbor randomVertex = getRandomNeighbor(srcNeighbor, dstNeighbor);
					//System.out.println(randomVertex);
					//System.out.println(srcNeighbor + " " + dstNeighbor);
					if(randomVertex == null) {
						//System.out.println("I am breaking" + randomVertex);
						break;
					}else if (set.contains(randomVertex)) {

					}else {
						set.add(randomVertex);
						THashSet<LabeledNode> randomVertexNeighbor = nodeMap.getNodeNeighbors(randomVertex.getDst());

						if(randomVertexNeighbor.contains(src) && randomVertexNeighbor.contains(dst)) {
							//triangle -> hence, rejected!!!!!

						}else if (randomVertexNeighbor.contains(src)) {
							if(reservoir.size() >= M) {
								Triplet temp = reservoir.getRandom();
								reservoir.remove(temp);
								removeFrequentPattern(temp);
							}
							Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));

							reservoir.add(triplet); 
							addFrequentPattern(triplet);

							count++;

						}else {
							if(reservoir.size() >= M) {
								Triplet temp = reservoir.getRandom();
								reservoir.remove(temp);
								removeFrequentPattern(temp);
							}
							Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));

							reservoir.add(triplet); 
							addFrequentPattern(triplet);

							count++;
						}
					}
				}
				sum = sum-W;
			}
		}else {
			int count = 0 ; 
			while(count < W) {
				LabeledNeighbor randomVertex = getRandomNeighbor(srcNeighbor, dstNeighbor);

				if(randomVertex == null) {
					break;
				} else {
					//System.out.println(srcNeighbor + " " + dstNeighbor);
					THashSet<LabeledNode> randomVertexNeighbor = nodeMap.getNodeNeighbors(randomVertex.getDst());
					if(randomVertexNeighbor.contains(src) && randomVertexNeighbor.contains(dst)) {
						//triangle -> hence, rejected!!!!!
					}else if (randomVertexNeighbor.contains(src)) {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(src.getVertexId(), src.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addSubgraph(triplet);
					}else {
						Triplet triplet = new Triplet(src, dst, randomVertex.getDst(),edge, new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), randomVertex.getDst().getVertexId(), randomVertex.getDst().getVertexLabel(), randomVertex.getEdgeLabel()));
						addSubgraph(triplet);
					}
					count++;
				}
			}
		}
		utility.handleEdgeAddition(edge, nodeMap);
		//System.out.println(reservoir.size() + "  N " + N);
		nodeBottomK.addEdge(src, dst, edge);
		return false;
	}*/
	/*void addSubgraph(Triplet t) {
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
	}*/

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

	public LabeledNeighbor getRandomNeighbor(THashSet<LabeledNeighbor> srcNeighbor, THashSet<LabeledNeighbor> dstNeighbor) {
		int d_u = srcNeighbor.size();
		int d_v = dstNeighbor.size();

		if(d_u+d_v == 0) {
			return null;
		}

		double value = d_u/(double)(d_u+d_v);
		double ran = Math.random();
		if(ran <= value) {
			//select neighbor of u or src
			//System.out.println("src neighbor selected");
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(srcNeighbor);
			int randInt = rand.nextInt(list.size());
			//System.out.println(randInt);
			return list.get(rand.nextInt(list.size()));
		}else {
			//select a neighbor of v or dst
			//System.out.println("dst neighbor selected");
			ArrayList<LabeledNeighbor> list = new ArrayList<LabeledNeighbor>(dstNeighbor);
			int randInt = rand.nextInt(list.size());
			return list.get(randInt);
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


		//THashSet<LabeledNeighbor> srcNeighbor = nodeMap.getNeighbors(src);
		//THashSet<LabeledNeighbor> dstNeighbor = nodeMap.getNeighbors(dst);

		BottomKSketch<LabeledNeighbor> srcSketch = nodeBottomK.getSketch(src);
		BottomKSketch<LabeledNeighbor> dstSketch = nodeBottomK.getSketch(dst);
		int W = srcSketch.unionImprovedCardinality(dstSketch);

		//SetFunctions<LabeledNeighbor> fun = new SetFunctions<LabeledNeighbor>();
		//THashSet<LabeledNeighbor> union = fun.unionSet(srcNeighbor, dstNeighbor);
		//int W = union.size();

		THashSet<Triplet> candidateWedges = reservoir.getAllTriplets(src);
		ArrayList<Triplet> wedges = new ArrayList<Triplet>();
		for(Triplet t: candidateWedges) {
			if((t.a.equals(dst) || t.b.equals(dst) || t.c.equals(dst)) && !t.isTriangle()) {
				wedges.add(t);
			}
		}

		if(wedges.size() >0) {
			int count = wedges.size();
			for(Triplet t: wedges) {
				reservoir.remove(t);
				removeFrequentPattern(t);
				Ncurrent--;
				Zprime = -1;
			}
			c1+=count;
			c2+= (W-count);
		}
		//System.out.println(reservoir.size());

		//update all triangles in the reservoir
		THashSet<Triplet> candidateTriangles = reservoir.getAllTriplets(src);
		ArrayList<Triplet> triangles = new ArrayList<Triplet>();
		//System.out.println("size "  + candidateTriangles.size());
		for(Triplet t: candidateTriangles) {
			if((t.a.equals(dst) || t.b.equals(dst) || t.c.equals(dst)) && t.isTriangle()) {
				triangles.add(t);
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
