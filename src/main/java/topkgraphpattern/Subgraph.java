package topkgraphpattern;

import java.util.List;
import java.util.Set;

import input.StreamEdge;
import struct.LabeledNode;

public interface Subgraph {
	public List<LabeledNode> getAllVertices();
	public Set<StreamEdge> getAllEdges();
}
