package struct;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class LabeledNode implements Comparable<LabeledNode>{
	String vertexId;
	int vertexLabel;
	public LabeledNode(String id, int label) {
		this.vertexId = id;
		this.vertexLabel = label;
	}
	public int compareTo(LabeledNode o) {
		if(this.vertexId.compareTo(o.vertexId) < 0)
			return -1;
		else if(this.vertexId.compareTo(o.vertexId) == 0) {
			return (this.vertexLabel-o.vertexLabel);
		}else {
			return 1;
		}
	}
	
	public String getVertexId() {
		return vertexId;
	}
	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}
	public int getVertexLabel() {
		return vertexLabel;
	}
	public void setVertexLabel(int vertexLabel) {
		this.vertexLabel = vertexLabel;
	}
	@Override
	public int hashCode() {
		int hashCode =  new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(this.vertexId).
				append(this.vertexLabel).
				toHashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		LabeledNode n = (LabeledNode)o;
		return (this.compareTo(n) == 0);
	}
	
	public String toString() {
		return this.vertexId+" " +this.vertexLabel;
	}
}
