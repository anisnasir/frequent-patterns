package topkgraphpattern;

import gnu.trove.map.hash.THashMap;
import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;

public interface TopkGraphPatterns {
	boolean addEdge(StreamEdge edge);
	boolean removeEdge(StreamEdge edge);
	THashMap<Pattern, Integer> getFrequentPatterns();
	int getNumberofSubgraphs();
}
