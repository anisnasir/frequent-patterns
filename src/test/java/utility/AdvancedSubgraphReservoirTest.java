package utility;

import org.junit.Test;

import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import struct.LabeledNode;
import struct.Triplet;

public class AdvancedSubgraphReservoirTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode(120,1);
		LabeledNode b = new LabeledNode(130,2);
		LabeledNode c = new LabeledNode(140,3);
		LabeledNode d = new LabeledNode(150,4);
		StreamEdge edgeA = new StreamEdge(120,1,  130, 2);
		StreamEdge edgeB = new StreamEdge(120,1, 140, 3);
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); //b-a-c wedge
		StreamEdge edgeC = new StreamEdge(130,2, 140,3);
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB,edgeC); // a-b-c triangle
		AdvancedSubgraphReservoir<Triplet> reservoir = new AdvancedSubgraphReservoir<Triplet>();
		reservoir.add(t1);
		reservoir.add(t);
		
		System.out.println(reservoir.getAllSubgraphs(a));
		System.out.println(reservoir.getAllSubgraphs(b));
		System.out.println(reservoir.getAllSubgraphs(c));
		
		reservoir.remove(t1);

		System.out.println(reservoir.getAllSubgraphs(a));
		System.out.println(reservoir.getAllSubgraphs(b));
		System.out.println(reservoir.getAllSubgraphs(c));
		
		reservoir.remove(t);

		System.out.println(reservoir.getAllSubgraphs(a));
		System.out.println(reservoir.getAllSubgraphs(b));
		System.out.println(reservoir.getAllSubgraphs(c));
		
		reservoir.remove(t);
	}
	

}
