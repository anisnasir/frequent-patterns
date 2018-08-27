package graphpattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import input.StreamEdge;
import struct.LabelDegree;
import struct.LabeledNode;
import struct.Quadriplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;

public class FourNodeGraphPattern implements Comparable<FourNodeGraphPattern>, Pattern {
	public List<LabelDegree> list;
	SubgraphType type;
	int numEdges;
	int maxDegree;

	public FourNodeGraphPattern(Quadriplet t) {
		list = new ArrayList<LabelDegree>();
		Set<LabeledNode> vertices = t.getSubgraph().keySet();
		for (LabeledNode vertex : vertices) {
			list.add(new LabelDegree(vertex.getVertexLabel(), t.getSubgraph().get(vertex).size()));
		}
		type = t.getType();
		numEdges = t.getNumEdges();
		maxDegree = t.getMaxDegree();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 31); // two randomly chosen prime numbers
		// if deriving: appendSuper(super.hashCode()).
		hashCodeBuilder.append(numEdges).append(maxDegree).append(getType());
		int value =  hashCodeBuilder.toHashCode();
		return value;
	}

	public SubgraphType getType() {
		return this.type;
	}

	public boolean equals(FourNodeGraphPattern o) {
		System.out.println("equal method Called");
		Collections.sort(list);
		Collections.sort(o.list);
		Iterator<LabelDegree> iterator = o.list.iterator();
		for(LabelDegree item: list) {
			if(!iterator.hasNext()) {
				return false;
			} else {
				LabelDegree nextItem = iterator.next();
				if(!item.equals(nextItem)) {
					return false;
				}
			}
		}
		if(iterator.hasNext())
			return false;
		return true;
		

	}

	public String toString() {
		return list.toString() + " " + type;
	}

	public int compareTo(FourNodeGraphPattern o) {
		int edgeDiff = numEdges - o.numEdges;
		if (edgeDiff != 0) {
			return edgeDiff;
		} else {
			int maxDegreeDiff = maxDegree-o.maxDegree;
			if(maxDegreeDiff != 0 ) { 
				return maxDegreeDiff;
			} else {
				Collections.sort(list);
				Collections.sort(o.list);
				Iterator<LabelDegree> iterator = o.list.iterator();
				for(LabelDegree item: list) {
					if(!iterator.hasNext()) {
						return -1;
					} else {
						LabelDegree nextItem = iterator.next();
						if(!item.equals(nextItem)) {
							return item.label- nextItem.label;
						}
					}
				}
				return 0;
			}
		}
	}
}
