package struct;

public class LabeledNode implements Comparable<LabeledNode> {
	int vertexId;
	int vertexLabel;

	public LabeledNode(int id, int label) {
		this.vertexId = id;
		this.vertexLabel = label;
	}

	@Override
	public int compareTo(LabeledNode o) {
		if (this.vertexId < o.vertexId)
			return -1;
		else if (this.vertexId == o.vertexId) {
			return (this.vertexLabel - o.vertexLabel);
		} else {
			return 1;
		}
	}

	public int getVertexId() {
		return vertexId;
	}

	public void setVertexId(int vertexId) {
		this.vertexId = vertexId;
	}

	public int getVertexLabel() {
		return vertexLabel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + vertexId;
		result = prime * result + vertexLabel;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LabeledNode other = (LabeledNode) obj;
		if (vertexId != other.vertexId)
			return false;
		if (vertexLabel != other.vertexLabel)
			return false;
		return true;
	}

	public void setVertexLabel(int vertexLabel) {
		this.vertexLabel = vertexLabel;
	}

	@Override
	public String toString() {
		return this.vertexId + " " + this.vertexLabel;
	}
}
