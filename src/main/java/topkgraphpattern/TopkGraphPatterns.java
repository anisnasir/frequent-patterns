package topkgraphpattern;

import gnu.trove.map.hash.THashMap;
import input.StreamEdge;

public interface TopkGraphPatterns {
	public boolean addEdge(StreamEdge edge);
	public boolean removeEdge(StreamEdge edge);
	public THashMap<Pattern, Long> getFrequentPatterns();
	public long getNumberofSubgraphs();
	public int getCurrentReservoirSize();
	public THashMap<Pattern, Long> correctEstimates();
}
