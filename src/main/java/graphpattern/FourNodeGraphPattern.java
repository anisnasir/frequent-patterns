package graphpattern;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.HashCodeBuilder;

import input.StreamEdge;
import struct.LabeledNode;
import struct.Quadriplet;
import topkgraphpattern.Pattern;
import topkgraphpattern.SubgraphType;

public class FourNodeGraphPattern implements Comparable<FourNodeGraphPattern>, Pattern {
	TreeSet<LabeledStreamEdge> labels;
	SubgraphType type;
	int numEdges;
	int maxDegree;

	public FourNodeGraphPattern(Quadriplet t) {
		labels = new TreeSet<LabeledStreamEdge>();
		Set<StreamEdge> labeledEdges = t.getAllEdges();
		for (StreamEdge labeledEdge : labeledEdges) {
			labels.add(new LabeledStreamEdge(labeledEdge.getSrcLabel(), labeledEdge.getDstLabel()));
		}
		type = t.getType();
		numEdges = t.getNumEdges();
		maxDegree = t.getMaxDegree();
	}

	@Override
	public int hashCode() {
		int hashCode = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		// if deriving: appendSuper(super.hashCode()).
				append(this.labels.toString()).append(numEdges).append(maxDegree).append(type).toHashCode();
		return hashCode;
	}

	public SubgraphType getType() {
		return this.type;
	}

	public int getNumEdges() {
		return this.numEdges;
	}

	public int getMaxDegree() {
		return this.maxDegree;
	}

	@Override
	public boolean equals(Object o) {
		FourNodeGraphPattern p = (FourNodeGraphPattern) o;
		if (numEdges != p.numEdges) {
			return false;
		} else if (!type.equals(p.getType())) {
			return false;
		} else if (this.maxDegree != p.getMaxDegree()) {
			return false;
		} else if (!this.labels.toString().equals(p.labels.toString())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		return "FourNodeGraphPattern [labels=" + labels + ", type=" + type + ", numEdges=" + numEdges + ", maxDegree="
				+ maxDegree + "]";
	}

	public int compareTo(FourNodeGraphPattern o) {
		FourNodeGraphPattern p = (FourNodeGraphPattern) o;
		if (numEdges != p.numEdges) {
			return numEdges-p.numEdges;
		} else if (!type.equals(p.getType())) {
			return maxDegree-p.getMaxDegree();
		} else if (!this.labels.equals(p.labels)) {
			return -1;
		} else {
			return 0;
		}
	}
}
