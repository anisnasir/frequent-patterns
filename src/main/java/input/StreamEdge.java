package input;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class StreamEdge implements Serializable, Comparable<StreamEdge> {
	private static final long serialVersionUID = -3733214465018614013L;
	private String src;
	private int srcLabel;
	private String dest;
	private int dstLabel;

	public StreamEdge(String src, int srcLabel, String dest, int dstLabel) {
		if (src.compareTo(dest) < 0) {
			this.src = src;
			this.srcLabel = srcLabel;
			this.dest = dest;
			this.dstLabel = dstLabel;
		} else {
			this.src = dest;
			this.srcLabel = dstLabel;
			this.dest = src;
			this.dstLabel = srcLabel;
		}
	}

	public int getSrcLabel() {
		return srcLabel;
	}

	public void setSrcLabel(int srcLabel) {
		this.srcLabel = srcLabel;
	}

	public int getDstLabel() {
		return dstLabel;
	}

	public void setDstLabel(int dstLabel) {
		this.dstLabel = dstLabel;
	}

	public String getSource() {
		return this.src;
	}

	public String getDestination() {
		return this.dest;
	}

	public String toString() {
		return this.src + " " + this.srcLabel + " " + this.dest + " " + this.dstLabel;
	}

	public int compareTo(StreamEdge o) {
		if (src.compareTo(o.src) < 0) {
			return -1;
		} else if (src.compareTo(o.src) == 0) {
			if (dest.compareTo(o.dest) < 0) {
				return -1;
			} else if (dest.compareTo(o.dest) == 0) {
				if (srcLabel < o.srcLabel) {
					return -1;
				} else if (srcLabel == o.srcLabel) {
					if (dstLabel < o.dstLabel) {
						return -1;
					} else if(dstLabel == o.dstLabel) {
						return 0;
					}
					else
						return 1;
				} else {
					return 1;
				}

			} else
				return 1;
		} else
			return 1;
	}

	@Override
	public int hashCode() {
		int hashCode = new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
		// if deriving: appendSuper(super.hashCode()).
				append(this.src).append(this.srcLabel).append(this.dest).append(this.dstLabel)
				.toHashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		StreamEdge n = (StreamEdge) o;
		return (this.compareTo(n) == 0);
	}

}
