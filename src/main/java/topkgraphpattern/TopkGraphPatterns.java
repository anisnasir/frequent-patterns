package topkgraphpattern;

import java.util.HashMap;

import graphpattern.ThreeNodeGraphPattern;
import input.StreamEdge;

public interface TopkGraphPatterns {
	boolean addEdge(StreamEdge edge);
	boolean removeEdge(StreamEdge edge);
	HashMap<Pattern, Integer> getFrequentPatterns();
	int getNumberofSubgraphs();
}
