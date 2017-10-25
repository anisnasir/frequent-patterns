package topkgraphpattern;

import gnu.trove.map.hash.THashMap;
import input.StreamEdge;

public class TriesteAlgorithm implements TopkGraphPatterns{
	EdgeReservoir reservoir;
	int k ;
	public TriesteAlgorithm(int size, int k) {
		System.out.println("constructor for Trieste called"); 
		reservoir = new EdgeReservoir(size);
		this.k = k;
	}
	public boolean addEdge(StreamEdge edge) {
		System.out.println("+" + edge);
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeEdge(StreamEdge edge) {
		// TODO Auto-generated method stub
		System.out.println("-" + edge);
		return false;
	}
	public THashMap<GraphPattern, Integer> getFrequentPatterns() {
		// TODO Auto-generated method stub
		return null;
	}

}
