package utility;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;
import reservoir.AdvancedSubgraphReservoir;
import reservoir.Reservoir;
import reservoir.SubgraphReservoir;
import struct.LabeledNode;
import struct.Triplet;

public class AdvancedSubgraphReservoirTest {

	@Test
	public void test() {
		LabeledNode a = new LabeledNode("a",1);
		LabeledNode b = new LabeledNode("b",2);
		LabeledNode c = new LabeledNode("c",3);
		LabeledNode d = new LabeledNode("d",4);
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2);
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3);
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); //b-a-c wedge
		StreamEdge edgeC = new StreamEdge("b",2, "c",3);
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
