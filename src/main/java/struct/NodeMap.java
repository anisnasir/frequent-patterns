package struct;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import input.StreamEdge;



/*
 * Node map store <node identifiers, Set<String> Node Neighbors> in a HashMap
 */
public class NodeMap {
	public THashMap<LabeledNode,THashSet<LabeledNeighbor>>  map;
	public THashMap<LabeledNode,THashSet<LabeledNode>>  onlyNodeMap;
	public NodeMap() {
		map = new THashMap<LabeledNode,THashSet<LabeledNeighbor>> ();
		onlyNodeMap = new THashMap<LabeledNode,THashSet<LabeledNode>> ();
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
			
			THashSet<LabeledNode> nodeNeighbors = onlyNodeMap.get(src);
			nodeNeighbors.add(dest);
			onlyNodeMap.put(src, nodeNeighbors);
		}else {
			THashSet<LabeledNeighbor> neighbors = new THashSet<LabeledNeighbor> ();
			neighbors.add(temp);
			map.put(src, neighbors);
			
			THashSet<LabeledNode> nodeNeighbors = new THashSet<LabeledNode> ();
			nodeNeighbors.add(dest);
			onlyNodeMap.put(src, nodeNeighbors);
		}
		
	}
	public void removeEdge(LabeledNode src, LabeledNode dest, StreamEdge edge) { 
		if(map.containsKey(src))
		{
			LabeledNeighbor temp = new LabeledNeighbor(dest,edge.getEdgeLabel());
			THashSet<LabeledNeighbor> neighbors = map.get(src);
			neighbors.remove(temp);
			
			THashSet<LabeledNode> nodeNeighbors = onlyNodeMap.get(src);
			nodeNeighbors.remove(dest);
			
			if(!neighbors.isEmpty()) {
				map.put(src, neighbors);
				onlyNodeMap.put(src, nodeNeighbors);
			}
			else {
				map.remove(src);
				onlyNodeMap.remove(src);
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
	
	public THashSet<LabeledNode> getNodeNeighbors(LabeledNode src) {
		if(map.containsKey(src)) {
			return onlyNodeMap.get(src);
		}else 
			return new THashSet<LabeledNode>();
	}

}
