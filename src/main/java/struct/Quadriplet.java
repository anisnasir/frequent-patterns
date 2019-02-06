package struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import input.StreamEdge;
import topkgraphpattern.Subgraph;
import topkgraphpattern.SubgraphType;

public class Quadriplet implements Comparable<Quadriplet>, Subgraph {
	@Override
	public String toString() {
		return "Quadriplet [subgraph=" + subgraph + ", numEdges=" + numEdges + "]";
	}

	Map<LabeledNode, TreeSet<LabeledNode>> subgraph;
	Set<StreamEdge> edges;
	int numEdges;

	public Quadriplet() {
		subgraph = new TreeMap<LabeledNode, TreeSet<LabeledNode>>();
		edges = new HashSet<StreamEdge>();
		numEdges = 0;
	}

	public Map<LabeledNode, TreeSet<LabeledNode>> getSubgraph() {
		return subgraph;
	}

	public int getNumEdges() {
		return this.numEdges;
	}

	public Quadriplet(Quadriplet t) {
		subgraph = t.subgraph;
		numEdges = t.numEdges;
	}

	public void addEdge(StreamEdge edge) {
		if (!edges.contains(edge)) {
			LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
			LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
			
			if (subgraph.containsKey(src)) {
				subgraph.get(src).add(dst);
			} else {
				TreeSet<LabeledNode> neighborSet = new TreeSet<LabeledNode>();
				neighborSet.add(dst);
				subgraph.put(src, neighborSet);
			}

			if (subgraph.containsKey(dst)) {
				subgraph.get(dst).add(src);
			} else {
				TreeSet<LabeledNode> neighborSet = new TreeSet<LabeledNode>();
				neighborSet.add(src);
				subgraph.put(dst, neighborSet);
			}
			numEdges++;
			edges.add(edge);
		}
	}
	
	public void removeEdge(StreamEdge edge) {
		if (edges.contains(edge)) {
			LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
			LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
			
			if (subgraph.containsKey(src)) {
				TreeSet<LabeledNode> treeSet = subgraph.get(src);
				treeSet.remove(dst);
				subgraph.put(src, treeSet);
			} 

			if (subgraph.containsKey(dst)) {
				TreeSet<LabeledNode> treeSet = subgraph.get(dst);
				treeSet.remove(src);
				subgraph.put(dst, treeSet);
			} 
			numEdges--;
			edges.remove(edge);
		}
	}

	public boolean isQuadriplet() {
		Set<LabeledNode> nodeSet = subgraph.keySet();
		if (nodeSet.size() != 4) {
			return false;
		}

		return isConnected(nodeSet);
	}

	public boolean isConnected(Set<LabeledNode> nodeSet) {
		Stack<LabeledNode> stack = new Stack<LabeledNode>();
		Set<LabeledNode> visited = new HashSet<LabeledNode>();
		LabeledNode node = nodeSet.iterator().next();
		stack.push(node);
		while (!stack.isEmpty()) {
			LabeledNode visiting = stack.pop();
			if (!visited.contains(visiting)) {
				visited.add(visiting);
				TreeSet<LabeledNode> neighbors = subgraph.get(visiting);
				neighbors.parallelStream().forEach(vertex -> stack.push(vertex));
			}
		}
		return (visited.size() == 4);
	}

	private int DFSCode() {
		List<LabeledNode> keyList = new ArrayList<LabeledNode>(subgraph.keySet());
		Collections.sort(keyList);
		Set<LabeledNode> visited = new HashSet<LabeledNode>();
		StringBuilder sb = new StringBuilder();

		Stack<DFSEdge> stack = new Stack<DFSEdge>();

		for (LabeledNode key : keyList) {
			if (!visited.contains(key)) {
				DFSEdge temp = new DFSEdge("", -1, key.getVertexId(), key.getVertexLabel());
				stack.push(temp);

				while (!stack.isEmpty()) {
					DFSEdge dfsEdge = stack.pop();
					sb.append(dfsEdge.toString());
					LabeledNode currentNode = new LabeledNode(dfsEdge.getVertex2Id(), dfsEdge.getVertex2Label());
					if (!visited.contains(currentNode)) {
						visited.add(currentNode);
						List<LabeledNode> neighbors = new ArrayList<LabeledNode>(subgraph.get(currentNode));
						Collections.sort(neighbors);
						for (LabeledNode neighbor : neighbors) {
							LabeledNode neighborrr = neighbor;
							if (!visited.contains(neighborrr)) {
								DFSEdge neighborEdge = new DFSEdge(currentNode.getVertexId(),
										currentNode.getVertexLabel(), neighbor.getVertexId(),
										neighbor.getVertexLabel());
								stack.push(neighborEdge);
							}
						}
					}
				}
			}
		}
		// System.out.println(sb.toString());
		return sb.toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		Quadriplet a = (Quadriplet)o;
		for (LabeledNode key : subgraph.keySet()) {
			if (!a.subgraph.containsKey(key)) {
				return false;
			} else {
				TreeSet<LabeledNode> aNeighbor = subgraph.get(key);
				TreeSet<LabeledNode> bNeighbor = a.subgraph.get(key);
				for (LabeledNode n : aNeighbor) {
					if (!bNeighbor.contains(n)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public int hashCode() {
		int value =  DFSCode();
		return value;
	}

	@Override
	public int compareTo(Quadriplet o) {
		if (this.equals(o)) {
			return 0;
		} else {
			int diff = numEdges - o.numEdges;
			if (diff != 0) {
				return diff;
			} else {
				return this.getMaxDegree() - o.getMaxDegree();
			}
		}
	}

	public SubgraphType getType() {
		if (this.numEdges == 6) {
			return SubgraphType.CLIQUE;
		} else if (this.numEdges == 5) {
			return SubgraphType.QUASI_CLIQUE;
		} else if (this.numEdges == 4) {
			if (getMaxDegree() == 2) {
				return SubgraphType.CIRCLE;
			} else {
				return SubgraphType.TAILED_TRIANGLE;
			}
		} else {
			if (getMaxDegree() == 2) {
				return SubgraphType.LINE;
			} else {
				return SubgraphType.STAR;
			}
		}
	}

	public Quadriplet getQuadripletMinusEdge(StreamEdge edge) {
		Quadriplet newQuadriplet = new Quadriplet();
		Set<LabeledNode> keySet = subgraph.keySet();
		for (LabeledNode key : keySet) {
			TreeSet<LabeledNode> neighbors = subgraph.get(key);
			for (LabeledNode neighbor : neighbors) {
				StreamEdge newEdge = new StreamEdge(key.getVertexId(), key.getVertexLabel(),
						neighbor.getVertexId(), neighbor.getVertexLabel());
				if (!newEdge.equals(edge)) {
					newQuadriplet.addEdge(newEdge);
				}

			}
		}
		return newQuadriplet;
	}

	public Quadriplet getQuadripletPlusEdge(StreamEdge edge) {
		Quadriplet newQuadriplet = new Quadriplet();
		Set<LabeledNode> keySet = subgraph.keySet();
		for (LabeledNode key : keySet) {
			TreeSet<LabeledNode> neighbors = subgraph.get(key);
			for (LabeledNode neighbor : neighbors) {
				StreamEdge newEdge = new StreamEdge(key.getVertexId(), key.getVertexLabel(),
						neighbor.getVertexId(), neighbor.getVertexLabel());
				newQuadriplet.addEdge(newEdge);
			}
		}
		newQuadriplet.addEdge(edge);
		return newQuadriplet;
	}

	public int getMaxDegree() {
		int maxDegree = 0;
		Set<LabeledNode> vertices = subgraph.keySet();
		for (LabeledNode vertex : vertices) {
			int degree = subgraph.get(vertex).size();
			if (degree > maxDegree) {
				maxDegree = degree;
			}
		}
		return maxDegree;
	}

	@Override
	public List<LabeledNode> getAllVertices() {
		return new ArrayList<LabeledNode>(subgraph.keySet());
	}

	@Override
	public Set<StreamEdge> getAllEdges() {
		return edges;
	}

}
