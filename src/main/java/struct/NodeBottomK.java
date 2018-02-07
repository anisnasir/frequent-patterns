package struct;

import gnu.trove.map.hash.THashMap;
import input.StreamEdge;
import utility.BottomKSketch;

public class NodeBottomK {
	int k = 20;
	public THashMap<LabeledNode,BottomKSketch<LabeledNeighbor>>  map;
	public NodeBottomK() {
		map = new THashMap<LabeledNode,BottomKSketch<LabeledNeighbor>> ();
	}
	
	void addNode(LabeledNode str) {
		map.put(str, new BottomKSketch<LabeledNeighbor>(k));
	}
	public void addEdge(LabeledNode src, LabeledNode dest, StreamEdge edge) { 
		LabeledNeighbor temp = new LabeledNeighbor(dest,edge.getEdgeLabel());
		if (map.containsKey(src)) {
			BottomKSketch<LabeledNeighbor> neighbors = map.get(src);
			neighbors.offer(temp);
			map.put(src, neighbors);
		}else {
			BottomKSketch<LabeledNeighbor> neighbors = new BottomKSketch<LabeledNeighbor> (k);
			neighbors.offer(temp);
			map.put(src, neighbors);
		}
		
		LabeledNeighbor temp1 = new LabeledNeighbor(src,edge.getEdgeLabel());
		if (map.containsKey(dest)) {
			BottomKSketch<LabeledNeighbor> neighbors = map.get(dest);
			neighbors.offer(temp1);
			map.put(dest, neighbors);
		}else {
			BottomKSketch<LabeledNeighbor> neighbors = new BottomKSketch<LabeledNeighbor> (k);
			neighbors.offer(temp1);
			map.put(dest, neighbors);
		}
	}
	public boolean contains(LabeledNode str) {
		return map.contains(str);
	}
	public BottomKSketch<LabeledNeighbor> getSketch(LabeledNode str) {
		if(map.contains(str))
			return map.get(str);
		else 
			return new BottomKSketch<LabeledNeighbor>(k);
	}
	public void removeEdge(LabeledNode src, LabeledNode dest, StreamEdge edge) { 
		LabeledNeighbor temp = new LabeledNeighbor(dest,edge.getEdgeLabel());
		if (map.containsKey(src)) {
			BottomKSketch<LabeledNeighbor> neighbors = map.get(src);
			neighbors.remove(temp);
			map.put(src, neighbors);
		}
		
		LabeledNeighbor temp1 = new LabeledNeighbor(src,edge.getEdgeLabel());
		if (map.containsKey(dest)) {
			BottomKSketch<LabeledNeighbor> neighbors = map.get(dest);
			neighbors.remove(temp1);
			map.put(dest, neighbors);
		}
	}
	
	public int getUnionSize(LabeledNode src, LabeledNode dest) { 
		BottomKSketch<LabeledNeighbor> srcSketch = map.get(src);
		BottomKSketch<LabeledNeighbor> dstSketch = map.get(dest);
		return srcSketch.unionImprovedCardinality(dstSketch);
	}
	
}
