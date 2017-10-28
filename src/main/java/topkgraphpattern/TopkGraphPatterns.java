package topkgraphpattern;

import gnu.trove.map.hash.THashMap;
import input.StreamEdge;

public interface TopkGraphPatterns {
	boolean addEdge(StreamEdge edge);
	boolean removeEdge(StreamEdge edge);
	THashMap<GraphPattern, Integer> getFrequentPatterns();
	int getNumberofSubgraphs();
}
