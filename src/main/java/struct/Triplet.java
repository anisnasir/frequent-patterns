package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import input.StreamEdge;
import topkgraphpattern.Subgraph;

public class Triplet implements Comparable<Triplet>, Subgraph {
	public int numEdges;
	public LabeledNode nodeA;
	public LabeledNode nodeB;
	public LabeledNode nodeC;
	public int getNumEdges() {
		return numEdges;
	}

	public void setNumEdges(int numEdges) {
		this.numEdges = numEdges;
	}

	public LabeledNode getNodeA() {
		return nodeA;
	}

	public void setNodeA(LabeledNode nodeA) {
		this.nodeA = nodeA;
	}

	public LabeledNode getNodeB() {
		return nodeB;
	}

	public void setNodeB(LabeledNode nodeB) {
		this.nodeB = nodeB;
	}

	public LabeledNode getNodeC() {
		return nodeC;
	}

	public void setNodeC(LabeledNode nodeC) {
		this.nodeC = nodeC;
	}

	public StreamEdge getEdgeA() {
		return edgeA;
	}

	public void setEdgeA(StreamEdge edgeA) {
		this.edgeA = edgeA;
	}

	public StreamEdge getEdgeB() {
		return edgeB;
	}

	public void setEdgeB(StreamEdge edgeB) {
		this.edgeB = edgeB;
	}

	public StreamEdge getEdgeC() {
		return edgeC;
	}

	public void setEdgeC(StreamEdge edgeC) {
		this.edgeC = edgeC;
	}

	public StreamEdge edgeA, edgeB, edgeC;

	public Triplet() {
		numEdges = 0;
		nodeA = nodeB = nodeC = null;
		edgeA = edgeB = edgeC = null;
	}

	public boolean isTriangle() {
		return (this.numEdges == 3);
	}

	public Triplet(LabeledNode a, LabeledNode b, LabeledNode c, StreamEdge edgeA, StreamEdge edgeB) {
		numEdges = 2;
		LabeledNode arr[] = new LabeledNode[3];
		arr[0] = a;
		arr[1] = b;
		arr[2] = c;
		Arrays.sort(arr);

		this.nodeA = arr[0];
		this.nodeB = arr[1];
		this.nodeC = arr[2];

		if (edgeA.compareTo(edgeB) < 0) {
			this.edgeA = edgeA;
			this.edgeB = edgeB;
		} else {
			this.edgeA = edgeB;
			this.edgeB = edgeA;
		}

	}

	public Triplet(LabeledNode a, LabeledNode b, LabeledNode c, StreamEdge edgeA, StreamEdge edgeB, StreamEdge edgeC) {
		numEdges = 3;
		LabeledNode arr[] = new LabeledNode[3];
		arr[0] = a;
		arr[1] = b;
		arr[2] = c;
		Arrays.sort(arr);

		this.nodeA = arr[0];
		this.nodeB = arr[1];
		this.nodeC = arr[2];

		StreamEdge[] edgeArray = new StreamEdge[3];
		edgeArray[0] = edgeA;
		edgeArray[1] = edgeB;
		edgeArray[2] = edgeC;

		Arrays.sort(edgeArray);
		this.edgeA = edgeArray[0];
		this.edgeB = edgeArray[1];
		this.edgeC = edgeArray[2];

	}

	public int compareTo(Triplet o) {
		if (this.numEdges < o.numEdges) {
			return -1;
		} else if (this.numEdges == o.numEdges) {
			if (this.nodeA.compareTo(o.nodeA) < 0) {
				return -1;
			} else if (this.nodeA.compareTo(o.nodeA) == 0) {
				if (this.nodeB.compareTo(o.nodeB) < 0) {
					return -1;
				} else if (this.nodeB.compareTo(o.nodeB) == 0) {
					if (this.nodeC.compareTo(o.nodeC) < 0)
						return -1;
					else if (this.nodeC.compareTo(o.nodeC) == 0) {
						if (this.edgeA.compareTo(o.edgeA) < 0) {
							return -1;
						} else if (this.edgeA.compareTo(o.edgeA) == 0) {
							if (this.edgeB.compareTo(o.edgeB) < 0) {
								return -1;
							} else if (this.edgeB.compareTo(o.edgeB) == 0) {
								if (numEdges == 2)
									return 0;
								else {
									return this.edgeC.compareTo(o.edgeC);
								}
							} else
								return 1;
						} else
							return 1;
					} else
						return 1;
				} else
					return 1;
			} else
				return 1;
		} else
			return 1;
	}

	@Override
	public int hashCode() {
		// System.out.println("hashcode method");
		if (this.numEdges == 2) {
			int hashCode = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
			// if deriving: appendSuper(super.hashCode()).
					append(this.nodeA).append(this.nodeB).append(this.nodeC).append(this.edgeA).append(this.edgeB)
					.append(this.numEdges).toHashCode();
			return hashCode;
		} else {
			int hashCode = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
			// if deriving: appendSuper(super.hashCode()).
					append(this.nodeA).append(this.nodeB).append(this.nodeC).
					// append(this.edgeA).
					// append(this.edgeB).
					// append(this.edgeC).
					append(this.numEdges).toHashCode();
			return hashCode;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		// System.out.println("equal method ");
		Triplet t = (Triplet) o;

		return (this.compareTo(t) == 0);

	}

	@Override
	public String toString() {
		return "Triplet [numEdges=" + numEdges + ", nodeA=" + nodeA + ", nodeB=" + nodeB + ", nodeC=" + nodeC
				+ ", edgeA=" + edgeA + ", edgeB=" + edgeB + ", edgeC=" + edgeC + "]";
	}

	@Override
	public List<LabeledNode> getAllVertices() {
		return Arrays.asList(nodeA, nodeB, nodeC);
	}
	
	public Set<StreamEdge> getAllEdges() {
		if(this.numEdges == 0) {
			return new HashSet<StreamEdge>();
		} else if (this.numEdges == 2) { 
			Set<StreamEdge> list = new HashSet<StreamEdge>();
			list.add(edgeA);
			list.add(edgeB);
			return list;
		} else {
			Set<StreamEdge> list = new HashSet<StreamEdge>();
			list.add(edgeA);
			list.add(edgeB);
			list.add(edgeC);
			return list;
		}
	}
	
	public List<LabeledNode> getAllNodes() { 
		List<LabeledNode> list = new ArrayList<LabeledNode>();
		list.add(nodeA);
		list.add(nodeB);
		list.add(nodeC);
		return list;
	}
	
	public boolean contains(LabeledNode a) {
		return (a.equals(nodeA) || a.equals(nodeB) || a.equals(nodeC));
	}
}
