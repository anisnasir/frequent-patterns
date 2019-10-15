package struct;

public class DFSEdge {
	int vertex1;
	int vertex1Label;
	int vertex2;
	int vertex2Label;

	public int getVertex1Id() {
		return vertex1;
	}

	@Override
	public String toString() {
		return "DFSEdge [vertex1Id=" + vertex1 + ", vertex1Label=" + vertex1Label + ", vertex2Id=" + vertex2
				+ ", vertex2Label=" + vertex2Label + "]";
	}

	public void setVertex1Id(int vertex1Id) {
		this.vertex1 = vertex1Id;
	}

	public int getVertex1Label() {
		return vertex1Label;
	}

	public void setVertex1Label(int vertex1Label) {
		this.vertex1Label = vertex1Label;
	}

	public int getVertex2Id() {
		return vertex2;
	}

	public void setVertex2Id(int vertex2Id) {
		this.vertex2 = vertex2Id;
	}

	public int getVertex2Label() {
		return vertex2Label;
	}

	public void setVertex2Label(int vertex2Label) {
		this.vertex2Label = vertex2Label;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + vertex1;
		result = prime * result + vertex1Label;
		result = prime * result + vertex2;
		result = prime * result + vertex2Label;
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
		DFSEdge other = (DFSEdge) obj;
		if (vertex1 != other.vertex1)
			return false;
		if (vertex1Label != other.vertex1Label)
			return false;
		if (vertex2 != other.vertex2)
			return false;
		if (vertex2Label != other.vertex2Label)
			return false;
		return true;
	}

	boolean isLessThan(DFSEdge edge) {
		if (vertex2 < vertex1 && edge.vertex2 > edge.vertex1) {
			return true;
		}
		if (vertex2 < vertex1 && edge.vertex2 < edge.vertex1
				&& vertex2 < edge.vertex2) {
			return true;
		}
		if (vertex2 < vertex1 && edge.vertex2 < edge.vertex1 && vertex2 == edge.vertex2) {
			return true;
		}
		if (vertex2 > vertex1 && edge.vertex2 > edge.vertex1
				&& edge.vertex1 < vertex1) {
			return true;
		}
		if (vertex2 > vertex1 && edge.vertex2 > edge.vertex1 && edge.vertex1 == vertex1
				&& vertex1Label < edge.vertex1Label) {
			return true;
		}
		if (this.vertex2 > vertex1 && edge.vertex2 > edge.vertex1
				&& edge.vertex1 == vertex1 && vertex1Label == edge.vertex1Label
				&& vertex2Label < edge.vertex2Label) {
			return true;
		}
		if (vertex2 > vertex1 && edge.vertex2 > edge.vertex1 && edge.vertex1 == vertex1
				&& vertex1Label == edge.vertex1Label 
				&& vertex2Label < edge.vertex2Label) {
			return true;
		}
		return false;
	}

	public DFSEdge(int vertex1, int vertex1Label, int vertex2, int vertex2Label) {
		this.vertex1 = vertex1;
		this.vertex1Label = vertex1Label;
		this.vertex2 = vertex2;
		this.vertex2Label = vertex2Label;
	}
}
