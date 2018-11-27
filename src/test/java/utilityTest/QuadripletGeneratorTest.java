package utilityTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import incremental.IncrementalExhaustiveCountingFourNode;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Path;
import struct.Quadriplet;
import utility.EdgeHandler;
import utility.QuadripletGenerator;

public class QuadripletGeneratorTest {

	@Test
	public void test() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("c", 3, "d", 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("d", 4, "a", 1);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		HashSet<Path> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		HashSet<Path> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		assertEquals(1, subgraphs.size());
	}

}
