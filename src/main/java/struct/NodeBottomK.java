package struct;

import java.util.HashMap;

import input.StreamEdge;
import utility.BottomKSketch;

public class NodeBottomK {
	int k = 20;
	public HashMap<LabeledNode, BottomKSketch<LabeledNode>> map;

	public NodeBottomK() {
		map = new HashMap<LabeledNode, BottomKSketch<LabeledNode>>();
	}

	void addNode(LabeledNode str) {
		map.put(str, new BottomKSketch<LabeledNode>(k));
	}

	public void addEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			BottomKSketch<LabeledNode> neighbors = map.get(src);
			neighbors.offer(dest);
			map.put(src, neighbors);
		} else {
			BottomKSketch<LabeledNode> neighbors = new BottomKSketch<LabeledNode>(k);
			neighbors.offer(dest);
			map.put(src, neighbors);
		}

		if (map.containsKey(dest)) {
			BottomKSketch<LabeledNode> neighbors = map.get(dest);
			neighbors.offer(src);
			map.put(dest, neighbors);
		} else {
			BottomKSketch<LabeledNode> neighbors = new BottomKSketch<LabeledNode>(k);
			neighbors.offer(src);
			map.put(dest, neighbors);
		}
	}

	public boolean contains(LabeledNode str) {
		return map.containsKey(str);
	}

	public BottomKSketch<LabeledNode> getSketch(LabeledNode str) {
		if (map.containsKey(str))
			return map.get(str);
		else
			return new BottomKSketch<LabeledNode>(k);
	}

	public void removeEdge(LabeledNode src, LabeledNode dest) {
		if (map.containsKey(src)) {
			BottomKSketch<LabeledNode> neighbors = map.get(src);
			neighbors.remove(dest);
			map.put(src, neighbors);
		}

		if (map.containsKey(dest)) {
			BottomKSketch<LabeledNode> neighbors = map.get(dest);
			neighbors.remove(src);
			map.put(dest, neighbors);
		}
	}

	public int getUnionSize(LabeledNode src, LabeledNode dest) {
		BottomKSketch<LabeledNode> srcSketch = map.get(src);
		BottomKSketch<LabeledNode> dstSketch = map.get(dest);
		return srcSketch.unionImprovedCardinality(dstSketch);
	}

}
