package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import topkgraphpattern.SubgraphType;

public class QuadripletGenerator {
	public QuadripletGenerator() { 
		cache = new ArrayList<LinkedList<Quadriplet>>();
		rand = new Random();
	}
	List<LinkedList<Quadriplet>> cache;
	boolean isCacheReady = false;
	Random rand;

	public Set<Quadriplet> getAllSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Triplet> srcTwoHopNeighbors, HashSet<Triplet> dstTwoHopNeighbors) {
		Set<Quadriplet> result = new HashSet<Quadriplet>();

		// combine src, dst, dstNeighbors, dstTwoHopNeighbors
		for (Triplet dstTwoHopNeighbor : dstTwoHopNeighbors) {
			if (!dstTwoHopNeighbor.contains(src) && !dstTwoHopNeighbor.contains(dst)) {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				Set<StreamEdge> pathEdges = dstTwoHopNeighbor.getAllEdges();
				for (StreamEdge pathEdge : pathEdges) {
					quadriplet.addEdge(pathEdge);
				}

				List<LabeledNode> allNodes = dstTwoHopNeighbor.getAllNodes();
				for (LabeledNode node : allNodes) {
					if (!node.equals(dst)) {
						if (nodeMap.contains(src, node)) {
							quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
									node.getVertexId(), node.getVertexLabel()));
						}
					}
				}
				result.add(quadriplet);
			}
		}

		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
			for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
				if (!dstNeighbor.equals(srcNeighbor)) {
					Quadriplet quadriplet = new Quadriplet();
					quadriplet.addEdge(edge);
					quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
							srcNeighbor.getVertexId(), srcNeighbor.getVertexLabel()));
					quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
							dstNeighbor.getVertexId(), dstNeighbor.getVertexLabel()));

					if (nodeMap.contains(src, dstNeighbor)) {
						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								dstNeighbor.getVertexId(), dstNeighbor.getVertexLabel()));
					}

					if (nodeMap.contains(dst, srcNeighbor)) {
						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								srcNeighbor.getVertexId(), srcNeighbor.getVertexLabel()));
					}

					if (nodeMap.contains(srcNeighbor, dstNeighbor)) {
						quadriplet.addEdge(new StreamEdge(srcNeighbor.getVertexId(), srcNeighbor.getVertexLabel(),
								dstNeighbor.getVertexId(), dstNeighbor.getVertexLabel()));
					}

					result.add(quadriplet);
				}
			}
		}

		// combine srcTwoHopNeighbors, srcNeighbors, src, and dst
		for (Triplet srcTwoHopNeighbor : srcTwoHopNeighbors) {
			if (!srcTwoHopNeighbor.contains(src) && !srcTwoHopNeighbor.contains(dst)) {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				Set<StreamEdge> pathEdges = srcTwoHopNeighbor.getAllEdges();
				for (StreamEdge pathEdge : pathEdges) {
					quadriplet.addEdge(pathEdge);
				}

				List<LabeledNode> allNodes = srcTwoHopNeighbor.getAllNodes();
				for (LabeledNode node : allNodes) {
					if (!node.equals(src)) {
						if (nodeMap.contains(dst, node)) {
							quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
									node.getVertexId(), node.getVertexLabel()));
						}
					}
				}
				result.add(quadriplet);
			}
		}

		if (srcOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(srcOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					LabeledNode firstNeighbor = neighborList.get(i);
					LabeledNode secondNeighbor = neighborList.get(j);
					if (!nodeMap.contains(firstNeighbor, secondNeighbor)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));

						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
							quadriplet
									.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
											secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						}
						result.add(quadriplet);
					}
				}
			}
		}

		if (dstOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(dstOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					LabeledNode firstNeighbor = neighborList.get(i);
					LabeledNode secondNeighbor = neighborList.get(j);
					if (!nodeMap.contains(firstNeighbor, secondNeighbor)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));

						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));
						if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
							quadriplet
									.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
											secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));
						}
						result.add(quadriplet);
					}
				}
			}
		}

		return result;
	}

	public int getAllSubgraphsCounts(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Triplet> srcTwoHopNeighbors, HashSet<Triplet> dstTwoHopNeighbors) {
		int count = 0;
		// combine src, dst, dstNeighbors, dstTwoHopNeighbors
		for (Triplet dstTwoHopNeighbor : dstTwoHopNeighbors) {
			if (dstTwoHopNeighbor.contains(src) && dstTwoHopNeighbor.contains(dst)) {

			} else {
				count++;
			}
		}

		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
			for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
				if (!dstNeighbor.equals(srcNeighbor)) {
					count++;
				}
			}
		}

		SetFunctions<LabeledNode> setFunctions = new SetFunctions();
		// combine srcTwoHopNeighbors, srcNeighbors, src, and dst
		for (Triplet srcTwoHopNeighbor : srcTwoHopNeighbors) {
			if (srcTwoHopNeighbor.contains(dst)) {

			} else if (setFunctions.intersection(new HashSet<LabeledNode>(srcTwoHopNeighbor.getAllVertices()),
					nodeMap.getNeighbors(dst)) > 0) {

			} else {
				count++;
			}
		}

		if (srcOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(srcOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					LabeledNode firstNeighbor = neighborList.get(i);
					LabeledNode secondNeighbor = neighborList.get(j);
					if (!nodeMap.contains(firstNeighbor, secondNeighbor)) {
						count++;
					}
				}
			}
		}

		if (dstOneHopNeighbor.size() >= 2) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(dstOneHopNeighbor);
			for (int i = 0; i < neighborList.size() - 1; i++) {
				for (int j = i + 1; j < neighborList.size(); j++) {
					LabeledNode firstNeighbor = neighborList.get(i);
					LabeledNode secondNeighbor = neighborList.get(j);
					if (!nodeMap.contains(firstNeighbor, secondNeighbor)) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	public int getNewConnectedSubgraphCount(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<LabeledNode> srcTwoHopNeighbors, HashSet<LabeledNode> dstTwoHopNeighbors) {
		
		int count = 0;
		LinkedList<Quadriplet> classOneList = new LinkedList<Quadriplet>();
		List<LabeledNode> srcNeighbors = new ArrayList<LabeledNode>(srcOneHopNeighbor);
		for(int i = 0 ;i< srcNeighbors.size()-1;i++) {
			LabeledNode firstNeighbor = srcNeighbors.get(i);
			if (!dstOneHopNeighbor.contains(firstNeighbor)) {
				for (int j = i + 1; j < srcNeighbors.size(); j++) {
					LabeledNode secondNeighbor = srcNeighbors.get(j);
					if(!dstOneHopNeighbor.contains(secondNeighbor)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));

						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
							quadriplet
									.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
											secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						}
						classOneList.add(quadriplet);
					}
				}
			}
		}
		count += classOneList.size();
		cache.add(classOneList);
		
		LinkedList<Quadriplet> classTwoList = new LinkedList<Quadriplet>();
		List<LabeledNode> dstNeighbors = new ArrayList<LabeledNode>(dstOneHopNeighbor);
		for(int i = 0 ;i< dstNeighbors.size()-1;i++) {
			LabeledNode firstNeighbor = dstNeighbors.get(i);
			if (!srcOneHopNeighbor.contains(firstNeighbor)) {
				for (int j = i + 1; j < dstNeighbors.size(); j++) {
					LabeledNode secondNeighbor = dstNeighbors.get(j);
					if(!srcOneHopNeighbor.contains(secondNeighbor)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel()));

						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
							quadriplet
									.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
											secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

						}
						classTwoList.add(quadriplet);
					}
				}
			}
		}
		count += classTwoList.size();
		cache.add(classTwoList);
		
		
		LinkedList<Quadriplet> classThreeList = new LinkedList<Quadriplet>();
		for (LabeledNode s : srcOneHopNeighbor) {
			if(!dstOneHopNeighbor.contains(s)) {
				HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s);
				for(LabeledNode t: nNeighbors) {
					if(!dstOneHopNeighbor.contains(t) && !srcOneHopNeighbor.contains(t) && !t.equals(src)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
								s.getVertexId(), s.getVertexLabel()));
						quadriplet.addEdge(new StreamEdge(s.getVertexId(), s.getVertexLabel(),
								t.getVertexId(), t.getVertexLabel()));
						classThreeList.add(quadriplet);
					}
				}
			}
		}
		count += classThreeList.size();
		cache.add(classThreeList);

		LinkedList<Quadriplet> classFourList = new LinkedList<Quadriplet>();
		for (LabeledNode s : dstOneHopNeighbor) {
			if(!srcOneHopNeighbor.contains(s)) {
				HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s);
				for(LabeledNode t: nNeighbors) {
					if(!srcOneHopNeighbor.contains(t) && !dstOneHopNeighbor.contains(t) && !t.equals(dst)) {
						Quadriplet quadriplet = new Quadriplet();
						quadriplet.addEdge(edge);
						quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
								s.getVertexId(), s.getVertexLabel()));
						quadriplet.addEdge(new StreamEdge(s.getVertexId(), s.getVertexLabel(),
								t.getVertexId(), t.getVertexLabel()));
						classFourList.add(quadriplet);
						
					}
				}
			}
		}
		count += classFourList.size();
		cache.add(classFourList);

		LinkedList<Quadriplet> classFiveList = new LinkedList<Quadriplet>();
		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
			if (!dstOneHopNeighbor.contains(srcNeighbor)) {
				for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
					if (!srcOneHopNeighbor.contains(dstNeighbor)) {
						if (!dstNeighbor.equals(srcNeighbor) && !nodeMap.contains(srcNeighbor, dstNeighbor)) {
							Quadriplet quadriplet = new Quadriplet();
							quadriplet.addEdge(edge);
							quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
									srcNeighbor.getVertexId(), srcNeighbor.getVertexLabel()));
							quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(),
									dstNeighbor.getVertexId(), dstNeighbor.getVertexLabel()));
						
							classFiveList.add(quadriplet);
						}
					}
				}
			}
		}
		count += classFiveList.size();
		cache.add(classFiveList);

		this.isCacheReady = true;
		return count;
	}

	public List<Quadriplet> getNewConnectedSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<LabeledNode> srcTwoHopNeighbors, HashSet<LabeledNode> dstTwoHopNeighbors) {
		if(!this.isCacheReady) {
			getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		}
		List<Quadriplet> result = new ArrayList<Quadriplet>();
		for(List<Quadriplet> list: cache) {
			result.addAll(list);
		}
		return result;
		
	}
	public Quadriplet getRandomNewConnectedSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<LabeledNode> srcTwoHopNeighbors, HashSet<LabeledNode> dstTwoHopNeighbors) {

		int classOneCount = cache.get(0).size();
		int classTwoCount = cache.get(1).size();
		int classThreeCount = cache.get(2).size();
		int classFourCount = cache.get(3).size();
		int classFiveCount = cache.get(4).size();

		int count = classOneCount + classTwoCount + classThreeCount + classFourCount + classFiveCount;
		double firstClassProbability = classOneCount / (double) count;
		double secondClassProbability = classTwoCount / (double) count + firstClassProbability;
		double thirdClassProbability = classThreeCount / (double) count + secondClassProbability;
		double fourthClassProbability = classFourCount / (double) count + thirdClassProbability;

		double coin = Math.random();
		List<Quadriplet> result = null;
		if (coin < firstClassProbability) {
			result = cache.get(0);
		} else if (coin < secondClassProbability) {
			result = cache.get(1);
		} else if (coin < thirdClassProbability) {
			result = cache.get(2);
		} else if (coin < fourthClassProbability) {
			result = cache.get(3);
		} else {
			result = cache.get(4);
		}
		
		if(result.size() == 0) {
			return null;
		} else { 
			int index = rand.nextInt(result.size());
			Quadriplet quadriplet = result.get(index);
			result.remove(quadriplet);
			return quadriplet;
			
		}
	}
}
