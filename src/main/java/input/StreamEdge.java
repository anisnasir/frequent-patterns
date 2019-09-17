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
				}

			}
		}
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + dstLabel;
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		result = prime * result + srcLabel;
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
		StreamEdge other = (StreamEdge) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (dstLabel != other.dstLabel)
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		if (srcLabel != other.srcLabel)
			return false;
		return true;
	}

}
