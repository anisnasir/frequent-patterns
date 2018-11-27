package struct;

public class DFSEdge {
	String vertex1;
	int vertex1Label;
	String vertex2;
	int vertex2Label;

	public String getVertex1Id() {
		return vertex1;
	}

	@Override
	public String toString() {
		return "DFSEdge [vertex1Id=" + vertex1 + ", vertex1Label=" + vertex1Label + ", vertex2Id=" + vertex2
				+ ", vertex2Label=" + vertex2Label + "]";
	}

	public void setVertex1Id(String vertex1Id) {
		this.vertex1 = vertex1Id;
	}

	public int getVertex1Label() {
		return vertex1Label;
	}

	public void setVertex1Label(int vertex1Label) {
		this.vertex1Label = vertex1Label;
	}

	public String getVertex2Id() {
		return vertex2;
	}

	public void setVertex2Id(String vertex2Id) {
		this.vertex2 = vertex2Id;
	}

	public int getVertex2Label() {
		return vertex2Label;
	}

	public void setVertex2Label(int vertex2Label) {
		this.vertex2Label = vertex2Label;
	}

	public boolean equals(DFSEdge dfsEdge) {
		if (!this.vertex1.equals(dfsEdge.vertex1)) {
			return false;
		}
		if (!this.vertex2.equals(dfsEdge.vertex2)) {
			return false;
		}
		if (this.vertex1Label != dfsEdge.vertex1Label) {
			return false;
		}
		if (this.vertex2Label != dfsEdge.vertex2Label) {
			return false;
		}
		return true;
	}

	boolean isLessThan(DFSEdge edge) {
		if (vertex2.compareTo(vertex1) < 0 && edge.vertex2.compareTo(edge.vertex1) > 0) {
			return true;
		}
		if (vertex2.compareTo(vertex1) < 0 && edge.vertex2.compareTo(edge.vertex1) < 0
				&& vertex2.compareTo(edge.vertex2) < 0) {
			return true;
		}
		if (vertex2.compareTo(vertex1) < 0 && edge.vertex2.compareTo(edge.vertex1) < 0 && vertex2.equals(edge.vertex2)) {
			return true;
		}
		if (vertex2.compareTo(vertex1) > 0 && edge.vertex2.compareTo(edge.vertex1) > 0
				&& edge.vertex1.compareTo(vertex1) < 0) {
			return true;
		}
		if (vertex2.compareTo(vertex1) > 0 && edge.vertex2.compareTo(edge.vertex1) > 0 && edge.vertex1.equals(vertex1)
				&& vertex1Label < edge.vertex1Label) {
			return true;
		}
		if (this.vertex2.compareTo(vertex1) > 0 && edge.vertex2.compareTo(edge.vertex1) > 0
				&& edge.vertex1.equals(vertex1) && vertex1Label == edge.vertex1Label
				&& vertex2Label < edge.vertex2Label) {
			return true;
		}
		if (vertex2.compareTo(vertex1) > 0 && edge.vertex2.compareTo(edge.vertex1) > 0 && edge.vertex1.equals(vertex1)
				&& vertex1Label == edge.vertex1Label 
				&& vertex2Label < edge.vertex2Label) {
			return true;
		}
		return false;
	}

	public DFSEdge(String vertex1, int vertex1Label, String vertex2, int vertex2Label) {
		this.vertex1 = vertex1;
		this.vertex1Label = vertex1Label;
		this.vertex2 = vertex2;
		this.vertex2Label = vertex2Label;
	}
}
