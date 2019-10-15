package struct;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;

/*
 * Node map store <node identifiers, Set<String> Node Neighbors> in a HashMap
 */
public class NodeMap {
	public THashMap<LabeledNode, THashSet<LabeledNode>> map;

	public NodeMap() {
		map = new THashMap<LabeledNode, THashSet<LabeledNode>>();
	}

	void addNode(LabeledNode str) {
		map.put(str, new THashSet<LabeledNode>());
	}

	public void addEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			THashSet<LabeledNode> neighbors = map.get(src);
			neighbors.add(dest);
		} else {
			THashSet<LabeledNode> neighbors = new THashSet<LabeledNode>();
			neighbors.add(dest);
			map.put(src, neighbors);
		}

	}

	public void removeEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			THashSet<LabeledNode> neighbors = map.get(src);
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
			THashSet<LabeledNode> neighbors = map.get(src);
			LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
			return neighbors.contains(dst);
		}
		return false;

	}
	
	public boolean contains(LabeledNode src, LabeledNode dst) {
		if (map.containsKey(src)) {
			THashSet<LabeledNode> neighbors = map.get(src);
			return neighbors.contains(dst);
		}
		return false;

	}

	public THashSet<LabeledNode> getNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			return map.get(src);
		} else
			return new THashSet<LabeledNode>();
	}

	public THashSet<Triplet> getTwoHopNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			THashSet<LabeledNode> neighbors = map.get(src);
			THashSet<Triplet> result = new THashSet<Triplet>();
			for (LabeledNode neighbor : neighbors) {
				if (map.containsKey(neighbor)) {
					THashSet<LabeledNode> neighborNeighbors = map.get(neighbor);
					for (LabeledNode neighborNeighbor : neighborNeighbors) {
						if (!neighborNeighbor.equals(src) && !neighbors.contains(neighborNeighbor)) {
							Triplet t = null;
							if(neighbors.contains(neighborNeighbor)) {
								//triangle
								t = new Triplet(src, neighbor, neighborNeighbor,
										new StreamEdge(src.getVertexId(), src.getVertexLabel(), neighbor.getVertexId(),
												neighbor.getVertexLabel()),
										new StreamEdge(neighbor.getVertexId(), neighbor.getVertexLabel(),
												neighborNeighbor.getVertexId(), neighborNeighbor.getVertexLabel()),
										new StreamEdge(src.getVertexId(), src.getVertexLabel(),
												neighborNeighbor.getVertexId(), neighborNeighbor.getVertexLabel()));
							} else {
								//wedge
								t = new Triplet(src, neighbor, neighborNeighbor,
										new StreamEdge(src.getVertexId(), src.getVertexLabel(), neighbor.getVertexId(),
												neighbor.getVertexLabel()),
										new StreamEdge(neighbor.getVertexId(), neighbor.getVertexLabel(),
												neighborNeighbor.getVertexId(), neighborNeighbor.getVertexLabel()));
							}
							result.add(t);
						}
					}
				}
			}
			return result;
		} else
			return new THashSet<Triplet>();
	}
	
	public THashSet<LabeledNode> getTwoHopNeighbors(LabeledNode src, THashSet<LabeledNode> neighbors) {
		if (map.containsKey(src)) {
			THashSet<LabeledNode> result = new THashSet<LabeledNode>();
			for (LabeledNode neighbor : neighbors) {
				if (map.containsKey(neighbor)) {
					THashSet<LabeledNode> neighborNeighbors = map.get(neighbor);
					for (LabeledNode neighborNeighbor : neighborNeighbors) {
						if (!neighborNeighbor.equals(src)) {	
							result.add(neighborNeighbor);
						}
					}
				}
			}
			return result;
		} else
			return new THashSet<LabeledNode>();
	}

	public THashSet<LabeledNode> getNodeNeighbors(LabeledNode src) {
		if (map.containsKey(src)) {
			return map.get(src);
		} else
			return new THashSet<LabeledNode>();
	}

}
