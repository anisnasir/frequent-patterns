package struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import topkgraphpattern.Subgraph;
import topkgraphpattern.SubgraphType;

public class Quadriplet implements Comparable<Quadriplet>, Subgraph {
	@Override
	public String toString() {
		return "Quadriplet [subgraph=" + subgraph + ", numEdges=" + numEdges + "]";
	}

	THashMap<LabeledNode, THashSet<LabeledNeighbor>> subgraph;
	Set<StreamEdge> edges;
	int numEdges;

	public Quadriplet() {
		subgraph = new THashMap<LabeledNode, THashSet<LabeledNeighbor>>();
		edges = new HashSet<StreamEdge>();
		numEdges = 0;
	}

	public THashMap<LabeledNode, THashSet<LabeledNeighbor>> getSubgraph() {
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
			String edgeLabel = edge.getEdgeLabel();

			if (subgraph.contains(src)) {
				subgraph.get(src).add(new LabeledNeighbor(dst, edgeLabel));
			} else {
				THashSet<LabeledNeighbor> neighborSet = new THashSet<LabeledNeighbor>();
				neighborSet.add(new LabeledNeighbor(dst, edgeLabel));
				subgraph.put(src, neighborSet);
			}

			if (subgraph.contains(dst)) {
				subgraph.get(dst).add(new LabeledNeighbor(src, edgeLabel));
			} else {
				THashSet<LabeledNeighbor> neighborSet = new THashSet<LabeledNeighbor>();
				neighborSet.add(new LabeledNeighbor(src, edgeLabel));
				subgraph.put(dst, neighborSet);
			}
			numEdges++;
			edges.add(edge);
		}
	}

	public boolean isQuadriplet() {
		Set<LabeledNode> nodeSet = subgraph.keySet();
		if (nodeSet.size() != 4) {
			return false;
		}

		return isConnected(nodeSet);
	}

	public void addEdgesFromNode(NodeMap nodeMap, LabeledNode src, LabeledNode filteredNode) {
		THashSet<LabeledNeighbor> neighbors = nodeMap.getNeighbors(src);
		Set<LabeledNode> subgraphNodeSet = subgraph.keySet();
		for (LabeledNeighbor neighbor : neighbors) {
			if (subgraphNodeSet.contains(neighbor.getDst())) {
				if (!neighbor.getDst().equals(filteredNode)) {
					addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), neighbor.getDst().getVertexId(),
							neighbor.getDst().getVertexLabel(), neighbor.getEdgeLabel()));
				}
			}
		}
	}

	private boolean isConnected(Set<LabeledNode> nodeSet) {
		Stack<LabeledNode> stack = new Stack<LabeledNode>();
		Set<LabeledNode> visited = new HashSet<LabeledNode>();
		LabeledNode node = nodeSet.iterator().next();
		stack.push(node);
		while (!stack.isEmpty()) {
			LabeledNode visiting = stack.pop();
			if (!visited.contains(visiting)) {
				visited.add(visiting);
				THashSet<LabeledNeighbor> neighbors = subgraph.get(visiting);
				neighbors.parallelStream().forEach(vertex -> stack.push(vertex.getDst()));
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
				DFSEdge temp = new DFSEdge("", -1, key.getVertexId(), key.getVertexLabel(), "");
				stack.push(temp);

				while (!stack.isEmpty()) {
					DFSEdge dfsEdge = stack.pop();
					sb.append(dfsEdge.toString());
					LabeledNode currentNode = new LabeledNode(dfsEdge.getVertex2Id(), dfsEdge.getVertex2Label());
					if (!visited.contains(currentNode)) {
						visited.add(currentNode);
						List<LabeledNeighbor> neighbors = new ArrayList<LabeledNeighbor>(subgraph.get(currentNode));
						Collections.sort(neighbors);
						for (LabeledNeighbor neighbor : neighbors) {
							LabeledNode neighborrr = neighbor.getDst();
							if (!visited.contains(neighborrr)) {
								DFSEdge neighborEdge = new DFSEdge(currentNode.getVertexId(),
										currentNode.getVertexLabel(), neighbor.getDst().getVertexId(),
										neighbor.getDst().getVertexLabel(), neighbor.getEdgeLabel());
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

	private boolean equals(Quadriplet a) {
		for (LabeledNode key : subgraph.keySet()) {
			if (!a.subgraph.contains(key)) {
				return false;
			} else {
				THashSet<LabeledNeighbor> aNeighbor = subgraph.get(key);
				THashSet<LabeledNeighbor> bNeighbor = a.subgraph.get(key);
				for (LabeledNeighbor n : aNeighbor) {
					if (!bNeighbor.contains(n)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public int hashCode() {
		return DFSCode();
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
			THashSet<LabeledNeighbor> neighbors = subgraph.get(key);
			for (LabeledNeighbor neighbor : neighbors) {
				StreamEdge newEdge = new StreamEdge(key.getVertexId(), key.getVertexLabel(),
						neighbor.getDst().getVertexId(), neighbor.getDst().getVertexLabel(), neighbor.getEdgeLabel());
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
			THashSet<LabeledNeighbor> neighbors = subgraph.get(key);
			for (LabeledNeighbor neighbor : neighbors) {
				StreamEdge newEdge = new StreamEdge(key.getVertexId(), key.getVertexLabel(),
						neighbor.getDst().getVertexId(), neighbor.getDst().getVertexLabel(), neighbor.getEdgeLabel());
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

}
