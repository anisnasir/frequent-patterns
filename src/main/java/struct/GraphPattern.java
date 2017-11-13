package struct;

import java.util.Arrays;

import org.apache.commons.lang.builder.HashCodeBuilder;

import input.StreamEdge;

public class GraphPattern implements Comparable<GraphPattern> {
	int label1;
	int label2;
	int label3;
	boolean isWedge;
	public GraphPattern(Triplet t) { 
		if(t.numEdges == 2) {
			isWedge = true;
			StreamEdge edgeA = t.edgeA;
			int sLabel = edgeA.getSrcLabel();
			int tLabel = edgeA.getDstLabel();

			StreamEdge edgeB = t.edgeB;
			int xLabel = edgeB.getSrcLabel();
			int yLabel = edgeB.getDstLabel();

			if(sLabel == xLabel) {
				label1 = sLabel;
				if(tLabel < yLabel) {
					label2 = tLabel;
					label3 = yLabel;
				}else {
					label2 = yLabel;
					label3 = tLabel;
				}
			}else if (sLabel == yLabel) {
				label1 = sLabel;
				if(tLabel < xLabel) {
					label2 = tLabel;
					label3 = xLabel;
				}else {
					label2 = xLabel;
					label3 = tLabel;
				}
			}else if (tLabel == xLabel) {
				label1 = tLabel;
				if(sLabel < yLabel) {
					label2 = sLabel;
					label3 = yLabel;
				}else {
					label2 = yLabel;
					label3 = sLabel;
				}
			}else if(tLabel == yLabel) {
				label1 = tLabel;
				if(sLabel < xLabel) {
					label2 = sLabel;
					label3 = xLabel;

				}else {
					label2 = xLabel;
					label3 = sLabel;
				}
			}


		}else {
			isWedge = false;
			int []arr = new int[3];

			arr[0] = t.a.getVertexLabel();
			arr[1] = t.b.getVertexLabel();
			arr[2] = t.c.getVertexLabel();

			Arrays.sort(arr);
			label1 = arr[0];
			label2 = arr[1];
			label3 = arr[2];
		}
	}

	@Override
	public int hashCode() { 
		int hashCode =  new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(this.label1).
				append(this.label2).
				append(this.label3).
				append(this.isWedge).
				toHashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		GraphPattern b = (GraphPattern)o;
		return (this.compareTo(b) == 0);

	}

	public String toString() {
		return this.label1 + " "  +  this.label2 + " " + this.label3;
	}

	public int compareTo(GraphPattern o) {
		if(this.isWedge!= o.isWedge) {
			if(this.isWedge)
				return -1;
			else return 1;
		}
		if(this.label1 < o.label1)  {
			return -1;
		}else if (this.label1 == o.label1) {
			if(this.label2 < o.label2) {
				return -1;
			}else if (this.label2 == o.label2) {
				return this.label3-o.label3;
			}else 
				return 1;
		}else 
			return 1;
	}
}
