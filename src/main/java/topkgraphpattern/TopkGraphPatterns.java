package topkgraphpattern;

import java.util.HashMap;

import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;

public interface TopkGraphPatterns {
	public boolean addEdge(StreamEdge edge);
	public boolean removeEdge(StreamEdge edge);
	public HashMap<Pattern, Long> getFrequentPatterns();
	public long getNumberofSubgraphs();
	public int getCurrentReservoirSize();
	public void correctEstimates();
}
