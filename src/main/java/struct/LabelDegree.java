package struct;

public class LabelDegree implements Comparable<LabelDegree> {
	public int label;
	public int degree;

	public LabelDegree(int label, int degree) {
		this.label = label;
		this.degree = degree;
	}

	@Override
	public int compareTo(LabelDegree o) {
		if (degree == o.degree) {
			return label - o.label;
		} else {
			return degree - o.degree;
		}
	}

	@Override
	public String toString() {
		return "LD [label=" + label + ", degree=" + degree + "]";
	}

}
