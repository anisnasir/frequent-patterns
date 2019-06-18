package utility;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import incremental.IncrementalExhaustiveCountingFourNode;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
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
		
		StreamEdge edge = new StreamEdge("a", 1, "d", 4);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("[b 2]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(1, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(1, dstOneHopNeighbor.size());
		assertEquals("[c 3]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void test1() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("c", 3, "d", 4);
		StreamEdge d = new StreamEdge("a", 1, "d", 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		
		StreamEdge edge = new StreamEdge("d", 4, "e", 5);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("[c 3, a 1]", srcOneHopNeighbor.toString());
		HashSet<LabeledNode> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		assertEquals(1, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("[]", dstOneHopNeighbor.toString());
		HashSet<LabeledNode> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		
		int newConnectedSubgraphCount = gen.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, new HashSet<LabeledNode>(), new HashSet<LabeledNode>());
		assertEquals(3, newConnectedSubgraphCount);
		List<Quadriplet> newConnectedSubgraphs = gen.getNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		assertEquals(3, newConnectedSubgraphs.size());
		
	}
	
	@Test
	public void test2() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("c", 3, "d", 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("[]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(1, dstOneHopNeighbor.size());
		assertEquals("[c 3]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void test3() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();

		StreamEdge a = new StreamEdge("a", 1, "b", 2);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		
		StreamEdge edge = new StreamEdge("c", 3, "d", 4);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("[b 2]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(1, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("[]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void test4() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "d", 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("[]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void test5() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("a", 1, "d", 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("[]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void test6() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge("c", 3, "d", 4);
		StreamEdge b = new StreamEdge("a", 1, "c", 3);
		StreamEdge c = new StreamEdge("a", 1, "d", 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("[]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void test7() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge("c", 3, "d", 4);
		StreamEdge b = new StreamEdge("b", 2, "c", 3);
		StreamEdge c = new StreamEdge("b", 2, "d", 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("[]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void test8() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge("a", 1, "c", 3);
		StreamEdge b = new StreamEdge("a", 1, "d", 4);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		StreamEdge d = new StreamEdge("b", 2, "d", 4);
		StreamEdge e = new StreamEdge("c", 3, "d", 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		utility.handleEdgeAddition(e, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(2, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(2, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void test9() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge("a", 1, "d", 4);
		StreamEdge c = new StreamEdge("b", 2, "c", 3);
		StreamEdge d = new StreamEdge("b", 2, "d", 4);
		StreamEdge e = new StreamEdge("c", 3, "d", 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		utility.handleEdgeAddition(e, nodeMap);
		
		StreamEdge edge = new StreamEdge("a", 1, "b", 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		HashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("[d 4]", srcOneHopNeighbor.toString());
		HashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(2, srcTwoHopNeighbors.size());
		HashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("[c 3, d 4]", dstOneHopNeighbor.toString());
		HashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}

}
