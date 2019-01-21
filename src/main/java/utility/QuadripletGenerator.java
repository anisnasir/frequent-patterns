package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
		
	}
	Random rand = new Random();

	public Set<Quadriplet> getAllSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<Triplet> srcTwoHopNeighbors, HashSet<Triplet> dstTwoHopNeighbors) {
		Set<Quadriplet> result = new HashSet<Quadriplet>();

		// combine src, dst, dstNeighbors, dstTwoHopNeighbors
		for (Triplet dstTwoHopNeighbor : dstTwoHopNeighbors) {
			if (dstTwoHopNeighbor.contains(src) && dstTwoHopNeighbor.contains(dst)) {

			} else {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				List<StreamEdge> pathEdges = dstTwoHopNeighbor.getAllEdges();
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

		SetFunctions<LabeledNode> setFunctions = new SetFunctions();
		// combine srcTwoHopNeighbors, srcNeighbors, src, and dst
		for (Triplet srcTwoHopNeighbor : srcTwoHopNeighbors) {
			if (srcTwoHopNeighbor.contains(dst)) {

			} else if (setFunctions.intersection(new HashSet<LabeledNode>(srcTwoHopNeighbor.getAllVertices()),
					nodeMap.getNeighbors(dst)) > 0) {

			} else {
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				List<StreamEdge> pathEdges = srcTwoHopNeighbor.getAllEdges();
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
	
	public int[] getNewConnectedSubgraphCount(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<LabeledNode> srcTwoHopNeighbors, HashSet<LabeledNode> dstTwoHopNeighbors) {
		SetFunctions<LabeledNode> setFunctions = new SetFunctions<LabeledNode>();
		int[] results = new int[5];
		HashSet<LabeledNode> commonNeighbor = setFunctions.intersectionSet(srcOneHopNeighbor, dstOneHopNeighbor);
		results[0] = choose(srcOneHopNeighbor.size() - commonNeighbor.size(), 2);
		results[1] = choose(dstOneHopNeighbor.size() - commonNeighbor.size(), 2);
		
		int classThreeCount = 0;
		HashSet<LabeledNode> visitedSrc = new HashSet<LabeledNode>();
		for (LabeledNode s : srcOneHopNeighbor) {
			if(!dstOneHopNeighbor.contains(s)) {
				HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s);
				for(LabeledNode t: nNeighbors) {
					if(!dstOneHopNeighbor.contains(t) && !visitedSrc.contains(t) && !t.equals(src)) {
						visitedSrc.add(t);
						classThreeCount++;
					}
				}
			}
		}
		results[2]=classThreeCount;

		int classFourCount = 0;
		HashSet<LabeledNode> visitedDst = new HashSet<LabeledNode>();
		for (LabeledNode s1 : dstOneHopNeighbor) {
			if(!srcOneHopNeighbor.contains(s1)) {
				HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s1);
				for(LabeledNode t: nNeighbors) {
					if(!srcOneHopNeighbor.contains(t) && !visitedDst.contains(t) && !t.equals(dst)) {
						visitedDst.add(t);
						classFourCount++;
					}
				}
			}
		}
		results[3] = classFourCount;

		int classFiveCount = 0;
		// combine srcNeighbors, src, dst, dstNeighbors
		for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
			if(!dstOneHopNeighbor.contains(srcNeighbor) && !dstTwoHopNeighbors.contains(srcNeighbor)) {
				for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
					if (!srcOneHopNeighbor.contains(dstNeighbor) && !srcTwoHopNeighbors.contains(dstNeighbor)) {
						classFiveCount++;
					}
				}
			}
		}
		results[4] = classFiveCount;

		return results;
	}

	public Quadriplet getRandomNewConnectedSubgraphs(NodeMap nodeMap, StreamEdge edge, LabeledNode src, LabeledNode dst,
			HashSet<LabeledNode> srcOneHopNeighbor, HashSet<LabeledNode> dstOneHopNeighbor,
			HashSet<LabeledNode> srcTwoHopNeighbors, HashSet<LabeledNode> dstTwoHopNeighbors, int[] subgraphCountArray) {

		int classOneCount = subgraphCountArray[0];
		int classTwoCount = subgraphCountArray[1];
		int classThreeCount = subgraphCountArray[2];
		int classFourCount = subgraphCountArray[3];
		int classFiveCount = subgraphCountArray[4];

		int count = classOneCount + classTwoCount + classThreeCount + classFourCount + classFiveCount;
		double firstClassProbability = classOneCount / (double) count;
		double secondClassProbability = classTwoCount / (double) count + firstClassProbability;
		double thirdClassProbability = classThreeCount / (double) count + secondClassProbability;
		double fourthClassProbability = classFourCount / (double) count + thirdClassProbability;

		double coin = Math.random();

		if (coin < firstClassProbability) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(srcOneHopNeighbor);
			List<LabeledNode> uniqueNeighbors = new ArrayList<LabeledNode>();
			for (int i = 0; i < neighborList.size(); i++) {
				LabeledNode neighbor = neighborList.get(i);
				if (!dstOneHopNeighbor.contains(neighbor)) {
					uniqueNeighbors.add(neighbor);
				}
			}
			if (uniqueNeighbors.size() < 2) {
				return null;
			} else {
				Collections.shuffle(uniqueNeighbors);
				LabeledNode firstNeighbor = uniqueNeighbors.get(0);
				LabeledNode secondNeighbor = uniqueNeighbors.get(1);
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), firstNeighbor.getVertexId(),
						firstNeighbor.getVertexLabel()));

				quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), secondNeighbor.getVertexId(),
						secondNeighbor.getVertexLabel()));

				if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
					quadriplet.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
							secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

				}
				return quadriplet;
			}
		} else if (coin < secondClassProbability) {
			List<LabeledNode> neighborList = new ArrayList<LabeledNode>(dstOneHopNeighbor);
			List<LabeledNode> uniqueNeighbors = new ArrayList<LabeledNode>();
			for (int i = 0; i < neighborList.size(); i++) {
				LabeledNode neighbor = neighborList.get(i);
				if (!srcOneHopNeighbor.contains(neighbor)) {
					uniqueNeighbors.add(neighbor);
				}
			}
			if (uniqueNeighbors.size() < 2) {
				return null;
			} else {
				Collections.shuffle(uniqueNeighbors);
				LabeledNode firstNeighbor = uniqueNeighbors.get(0);
				LabeledNode secondNeighbor = uniqueNeighbors.get(1);
				Quadriplet quadriplet = new Quadriplet();
				quadriplet.addEdge(edge);
				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), firstNeighbor.getVertexId(),
						firstNeighbor.getVertexLabel()));

				quadriplet.addEdge(new StreamEdge(dst.getVertexId(), dst.getVertexLabel(), secondNeighbor.getVertexId(),
						secondNeighbor.getVertexLabel()));

				if (nodeMap.contains(firstNeighbor, secondNeighbor)) {
					quadriplet.addEdge(new StreamEdge(firstNeighbor.getVertexId(), firstNeighbor.getVertexLabel(),
							secondNeighbor.getVertexId(), secondNeighbor.getVertexLabel()));

				}
				return quadriplet;
			}
		} else if (coin < thirdClassProbability) {
			List<Quadriplet> result = new ArrayList<Quadriplet>();
			HashSet<LabeledNode> visitedSrc = new HashSet<LabeledNode>();
			for (LabeledNode s : srcOneHopNeighbor) {
				if(!dstOneHopNeighbor.contains(s)) {
					HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s);
					for(LabeledNode t: nNeighbors) {
						if(!dstOneHopNeighbor.contains(t) && !visitedSrc.contains(t) && !t.equals(src)) {
							visitedSrc.add(t);
							Quadriplet quadriplet = new Quadriplet();
							quadriplet.addEdge(edge);
							quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
									s.getVertexId(), s.getVertexLabel()));
							quadriplet.addEdge(new StreamEdge(s.getVertexId(), s.getVertexLabel(),
									t.getVertexId(), t.getVertexLabel()));
							result.add(quadriplet);
						}
					}
				}
			}
			return result.size() != 0 ? result.get(rand.nextInt(result.size())) : null;
		} else if (coin < fourthClassProbability) {
			List<Quadriplet> result = new ArrayList<Quadriplet>();
			HashSet<LabeledNode> visitedDst = new HashSet<LabeledNode>();
			for (LabeledNode s : dstOneHopNeighbor) {
				if(!srcOneHopNeighbor.contains(s)) {
					HashSet<LabeledNode> nNeighbors = nodeMap.getNeighbors(s);
					for(LabeledNode t: nNeighbors) {
						if(!srcOneHopNeighbor.contains(t) && !visitedDst.contains(t) && !t.equals(dst)) {
							visitedDst.add(t);
							Quadriplet quadriplet = new Quadriplet();
							quadriplet.addEdge(edge);
							quadriplet.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(),
									s.getVertexId(), s.getVertexLabel()));
							quadriplet.addEdge(new StreamEdge(s.getVertexId(), s.getVertexLabel(),
									t.getVertexId(), t.getVertexLabel()));
							result.add(quadriplet);
							
						}
					}
				}
			}
			return result.size() != 0 ? result.get(rand.nextInt(result.size())) : null;
		} else {
			List<Quadriplet> result = new ArrayList<Quadriplet>();
			// combine srcNeighbors, src, dst, dstNeighbors
			for (LabeledNode srcNeighbor : srcOneHopNeighbor) {
				if (!dstOneHopNeighbor.contains(srcNeighbor) && !dstTwoHopNeighbors.contains(srcNeighbor)) {
					for (LabeledNode dstNeighbor : dstOneHopNeighbor) {
						if (!srcOneHopNeighbor.contains(dstNeighbor) && !srcTwoHopNeighbors.contains(dstNeighbor)) {
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
									quadriplet.addEdge(
											new StreamEdge(srcNeighbor.getVertexId(), srcNeighbor.getVertexLabel(),
													dstNeighbor.getVertexId(), dstNeighbor.getVertexLabel()));
								}
								if (quadriplet.getType().equals(SubgraphType.LINE))
									result.add(quadriplet);
							}
						}
					}
				}
			}
			return result.size() != 0 ? result.get(rand.nextInt(result.size())) : null;
		}
	}

	private int choose(long total, long choose) {
		if (total < choose)
			return 0;
		if (choose == 0 || choose == total)
			return 1;
		return choose(total - 1, choose - 1) + choose(total - 1, choose);
	}

}
