package struct;

import java.util.HashMap;
import java.util.HashSet;

import input.StreamEdge;

/*
 * Node map store <node identifiers, Set<String> Node Neighbors> in a HashMap
 */
public class NodeMap {
	public HashMap<LabeledNode, HashSet<LabeledNode>> map;

	public NodeMap() {
		map = new HashMap<LabeledNode, HashSet<LabeledNode>>();
	}

	void addNode(LabeledNode str) {
		map.put(str, new HashSet<LabeledNode>());
	}

	public void addEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			HashSet<LabeledNode> neighbors = map.get(src);
			neighbors.add(dest);
			map.put(src, neighbors);
		} else {
			HashSet<LabeledNode> neighbors = new HashSet<LabeledNode>();
			neighbors.add(dest);
			map.put(src, neighbors);
		}

	}

	public void removeEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			HashSet<LabeledNode> neighbors = map.get(src);
			neighbors.remove(dest);

			if (!neighbors.isEmpty()) {
				map.put(src, neighbors);
			} else {
				map.remove(src);
			}
		}
	}

	public boolean contains(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		if (map.containsKey(src)) {
			HashSet<LabeledNode> neighbors = map.get(src);
			LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
			return neighbors.contains(dst);
		}
		return false;

	}

	public HashSet<LabeledNode> getNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			return map.get(src);
		} else
			return new HashSet<LabeledNode>();
	}

	public HashSet<Path> getTwoHopNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			HashSet<LabeledNode> neighbors = map.get(src);
			HashSet<Path> result = new HashSet<Path>();
			for (LabeledNode neighbor : neighbors) {
				if (map.containsKey(neighbor)) {
					HashSet<LabeledNode> neighborNeighbors = map.get(neighbor);
					for (LabeledNode neighborNeighbor : neighborNeighbors) {
						if (!neighborNeighbor.equals(src)) {
							Path path = new Path();
							path.addEdge(new StreamEdge(src.getVertexId(), src.getVertexLabel(), neighbor.getVertexId(),
									neighbor.getVertexLabel()));
							path.addEdge(new StreamEdge(neighbor.getVertexId(), neighbor.getVertexLabel(),
									neighborNeighbor.getVertexId(), neighborNeighbor.getVertexLabel()));
							result.add(path);
						}
					}
				}
			}
			return result;
		} else
			return new HashSet<Path>();
	}

	public HashSet<LabeledNode> getNodeNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			return map.get(src);
		} else
			return new HashSet<LabeledNode>();
	}

}
