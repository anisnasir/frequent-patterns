package struct;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class LabeledNeighbor implements Comparable<LabeledNeighbor> {
	LabeledNode dst;
	String edgeLabel;
	public LabeledNode getDst() {
		return dst;
	}

	public void setDst(LabeledNode dst) {
		this.dst = dst;
	}

	public String getEdgeLabel() {
		return edgeLabel;
	}

	public void setEdgeLabel(String edgeLabel) {
		this.edgeLabel = edgeLabel;
	}
	
	public LabeledNeighbor(LabeledNode dst, String label) {
		this.dst = dst;
		this.edgeLabel = label;
	}
	
	public String toString() {
		return this.dst.toString()+" "+ this.edgeLabel;
	}
	
	@Override
	public int hashCode() {
		int hashCode =  new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(this.dst.vertexId).
				append(this.dst.getVertexLabel()).
				toHashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if( o == null)
			throw new NullPointerException();
		
		LabeledNeighbor n = (LabeledNeighbor)o;
		return (this.compareTo(n) == 0);
	}

	public int compareTo(LabeledNeighbor o) {
		String neighborNode = dst.getVertexId();
		Integer neighborLabel = dst.getVertexLabel();
		//String neighborEdgeLabel = edgeLabel;
		
		String oNeighborNode = o.dst.getVertexId();
		Integer oNeighborLabel = o.dst.getVertexLabel();
		//String oNeighborEdgeLabel = o.edgeLabel;
		
		if(neighborNode.compareTo(oNeighborNode) < 0)
			return -1;
		else if (neighborNode.compareTo(oNeighborNode) == 0) {
			return (neighborLabel-oNeighborLabel);
		}else 
			return 1;
	}
}
