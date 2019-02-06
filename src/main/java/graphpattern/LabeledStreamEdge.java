package graphpattern;

public class LabeledStreamEdge implements Comparable<LabeledStreamEdge>{
	int labelA;
	int labelB;
	
	LabeledStreamEdge(int labelA, int labelB) {
		if(labelA < labelB) {
			this.labelA = labelA;
			this.labelB = labelB;
		} else {
			this.labelB = labelA;
			this.labelA = labelB;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + labelA;
		result = prime * result + labelB;
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
		LabeledStreamEdge other = (LabeledStreamEdge) obj;
		if (labelA != other.labelA)
			return false;
		if (labelB != other.labelB)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LabeledStreamEdge [labelA=" + labelA + ", label2=" + labelB + "]";
	}

	@Override
	public int compareTo(LabeledStreamEdge o) {
		if(this.labelA < o.labelA) {
			return -1;
		} else if (this.labelA == o.labelA) { 
			if(this.labelB < o.labelB) {
				return -1;
			} else if (this.labelB == o.labelB) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}
}
