package input;

import java.io.Serializable;

public class StreamEdge implements Serializable, Comparable<StreamEdge> {
	private static final long serialVersionUID = -3733214465018614013L;
	private int src;
	private int srcLabel;
	private int dest;
	private int dstLabel;

	public StreamEdge(int src, int srcLabel, int dest, int dstLabel) {
		if (src < dest) {
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

	public int getSource() {
		return this.src;
	}

	public int getDestination() {
		return this.dest;
	}

	@Override
	public String toString() {
		return this.src + " " + this.srcLabel + " " + this.dest + " " + this.dstLabel;
	}

	@Override
	public int compareTo(StreamEdge o) {
		if (src < o.src) {
			return -1;
		} else if (src == o.src) {
			if (dest < o.dest) {
				return -1;
			} else if (dest == o.dest) {
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
		result = prime * result + dest;
		result = prime * result + dstLabel;
		result = prime * result + src;
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
		if (dest != other.dest)
			return false;
		if (dstLabel != other.dstLabel)
			return false;
		if (src != other.src)
			return false;
		if (srcLabel != other.srcLabel)
			return false;
		return true;
	}

}
