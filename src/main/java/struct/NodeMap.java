package struct;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;



/*
 * Node map store <node identifiers, Set<String> Node Neighbors> in a HashMap
 */
public class NodeMap {
	public THashMap<LabeledNode,THashSet<LabeledNeighbor>>  map;
	public NodeMap() {
		map = new THashMap<LabeledNode,THashSet<LabeledNeighbor>> ();
	}
	
	void addNode(LabeledNode str) {
		map.put(str, new THashSet<LabeledNeighbor>());
	}
	public void addEdge(LabeledNode src, LabeledNode dest, StreamEdge edge) { 
		LabeledNeighbor temp = new LabeledNeighbor(dest,edge.getEdgeLabel());
		if (map.containsKey(src)) {
			THashSet<LabeledNeighbor> neighbors = map.get(src);
			neighbors.add(temp);
			map.put(src, neighbors);
		}else {
			THashSet<LabeledNeighbor> neighbors = new THashSet<LabeledNeighbor> ();
			neighbors.add(temp);
			map.put(src, neighbors);
		}
		
	}
	public void removeEdge(LabeledNode src, LabeledNode dest, StreamEdge edge) { 
		if(map.containsKey(src))
		{
			LabeledNeighbor temp = new LabeledNeighbor(dest,edge.getEdgeLabel());
			THashSet<LabeledNeighbor> neighbors = map.get(src);
			neighbors.remove(temp);
			if(!neighbors.isEmpty()) {
				map.put(src, neighbors);
			}
			else {
				map.remove(src);
			}
		}
	}
	
	public boolean contains(StreamEdge edge) {
		LabeledNode src = new LabeledNode(edge.getSource(),edge.getSrcLabel());
		if(map.containsKey(src)) {
			THashSet<LabeledNeighbor> neighbors = map.get(src);
			LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
			String edgeLabel = edge.getEdgeLabel();
			LabeledNeighbor temp = new LabeledNeighbor(dst,edgeLabel);
			return neighbors.contains(temp);
		}
		return false;
		
	}
	
	public THashSet<LabeledNeighbor> getNeighbors(LabeledNode src) {
		if(map.containsKey(src)) {
			return map.get(src);
		}else 
			return new THashSet<LabeledNeighbor>();
	}

}
