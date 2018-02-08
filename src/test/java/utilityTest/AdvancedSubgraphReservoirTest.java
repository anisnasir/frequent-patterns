package utilityTest;

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
		StreamEdge edgeA = new StreamEdge("a",1,  "b", 2, "u");
		StreamEdge edgeB = new StreamEdge("a",1, "c", 3, "u");
		Triplet t = new Triplet(a,b,c,edgeA, edgeB); //b-a-c wedge
		StreamEdge edgeC = new StreamEdge("b",2, "c",3, "u");
		Triplet t1 = new Triplet(a,b,c,edgeA, edgeB,edgeC); // a-b-c triangle
		AdvancedSubgraphReservoir<Triplet> reservoir = new AdvancedSubgraphReservoir<Triplet>();
		reservoir.add(t1);
		reservoir.add(t);
		
		System.out.println(reservoir.getAllTriplets(a));
		System.out.println(reservoir.getAllTriplets(b));
		System.out.println(reservoir.getAllTriplets(c));
		
		reservoir.remove(t1);

		System.out.println(reservoir.getAllTriplets(a));
		System.out.println(reservoir.getAllTriplets(b));
		System.out.println(reservoir.getAllTriplets(c));
		
		reservoir.remove(t);

		System.out.println(reservoir.getAllTriplets(a));
		System.out.println(reservoir.getAllTriplets(b));
		System.out.println(reservoir.getAllTriplets(c));
		
		reservoir.remove(t);
	}
	

}
