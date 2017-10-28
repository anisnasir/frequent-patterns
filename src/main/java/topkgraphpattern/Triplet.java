package topkgraphpattern;


import java.util.Arrays;

import org.apache.commons.lang.builder.HashCodeBuilder;

import input.StreamEdge;
import struct.LabeledNode;

public class Triplet implements Comparable<Triplet>{
	int numEdges;
	LabeledNode a, b, c;
	StreamEdge edgeA, edgeB, edgeC;

	public Triplet() {
		numEdges = 0;
		a = b = c = null;
		edgeA = edgeB = edgeC = null;
	}

	public Triplet(LabeledNode a, LabeledNode b, LabeledNode c, StreamEdge edgeA, StreamEdge edgeB) {
		numEdges = 2;
		LabeledNode arr[] = new LabeledNode[3];
		arr[0] = a;
		arr[1] = b;
		arr[2] = c;
		Arrays.sort(arr);
		
		this.a = arr[0];
		this.b = arr[1];
		this.c = arr[2];
		
		if(edgeA.compareTo(edgeB) < 0) {
			this.edgeA = edgeA;
			this.edgeB = edgeB;
		}else {
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
		
		this.a = arr[0];
		this.b = arr[1];
		this.c = arr[2];
		
		StreamEdge [] edgeArray = new StreamEdge[3];
		edgeArray[0] = edgeA;
		edgeArray[1] = edgeB;
		edgeArray[2] = edgeC;
		
		Arrays.sort(edgeArray);
		this.edgeA = edgeArray[0];
		this.edgeB = edgeArray[1];
		this.edgeC = edgeArray[2];

	}

	public int compareTo(Triplet o) {
		if(this.numEdges < o.numEdges) {
			return -1;
		}else if(this.numEdges == o.numEdges) {
			if(this.a.compareTo(o.a) < 0) {
				return -1;
			}else if (this.a.compareTo(o.a) == 0) {
				if(this.b.compareTo(o.b) < 0) {
					return -1;
				}else if(this.b.compareTo(o.b) ==0) {
					if(this.c.compareTo(o.c) < 0)
						return -1;
					else if(this.c.compareTo(o.c) == 0) {
						if(this.edgeA.compareTo(o.edgeA) < 0) {
							return -1;
						}else if(this.edgeA.compareTo(o.edgeA) == 0) {
							if(this.edgeB.compareTo(o.edgeB) < 0) {
								return -1;
							}else if(this.edgeB.compareTo(o.edgeB) == 0) {
								if(numEdges == 2) 
									return 0;
								else {
									return this.edgeC.compareTo(o.edgeC);
								}
							}else
								return 1;
						}else 
							return 1;
					}else 
						return 1;
				}else 
					return 1;
			}else 
				return 1;
		}else 
			return 1;
	}

	@Override
	public int hashCode() {
		//System.out.println("hashcode method");
		if(this.numEdges == 2) {
			int hashCode =  new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
					// if deriving: appendSuper(super.hashCode()).
					append(this.a).
					append(this.b).
					append(this.c).
					append(this.edgeA).
					append(this.edgeB).
					append(this.numEdges).
					toHashCode();
			return hashCode;
		}else {
			int hashCode =  new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
					// if deriving: appendSuper(super.hashCode()).
					append(this.a).
					append(this.b).
					append(this.c).
					//append(this.edgeA).
					//append(this.edgeB).
					//append(this.edgeC).
					append(this.numEdges).
					toHashCode();
			return hashCode;
		}

	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		
		//System.out.println("equal method ");
		Triplet t = (Triplet)o;
		
		return (this.compareTo(t) == 0);

	}

	public String toString() {
		if(numEdges == 2) {
			return a+":" + b + ":" + c + ":" + edgeA +":"+ edgeB;
		}else 
			return a+":" + b + ":" + c + ":" + edgeA +":"+ edgeB + ":" + edgeC;
	}
}
