package utility;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import gnu.trove.set.hash.THashSet;
import input.StreamEdge;
import struct.LabeledNode;
import struct.NodeMap;
import struct.Quadriplet;
import struct.Triplet;
import utility.EdgeHandler;
import utility.QuadripletGenerator;

public class QuadripletGeneratorTest {
	
	@Test
	public void starAndTwoLines() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		StreamEdge d = new StreamEdge(120, 1, 150, 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		
		StreamEdge edge = new StreamEdge(150, 4, 160, 5);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("{140 3, 120 1}", srcOneHopNeighbor.toString());
		THashSet<LabeledNode> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src, srcOneHopNeighbor);
		assertEquals(1, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("{}", dstOneHopNeighbor.toString());
		THashSet<LabeledNode> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst, dstOneHopNeighbor);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		
		int newConnectedSubgraphCount = gen.getNewConnectedSubgraphCount(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, new THashSet<LabeledNode>(), new THashSet<LabeledNode>());
		assertEquals(3, newConnectedSubgraphCount);
		List<Quadriplet> newConnectedSubgraphs = gen.getNewConnectedSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		assertEquals(3, newConnectedSubgraphs.size());
		
	}
	
	@Test
	public void line() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("{}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(1, dstOneHopNeighbor.size());
		assertEquals("{140 3}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void line2() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();

		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		
		StreamEdge edge = new StreamEdge(140, 3, 150, 4);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("{130 2}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(1, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("{}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void OppositeStar() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 150, 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("{}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void star() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(120, 1, 150, 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("{}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void oppositeTailedTriangle() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(140, 3, 150, 4);
		StreamEdge b = new StreamEdge(120, 1, 140, 3);
		StreamEdge c = new StreamEdge(120, 1, 150, 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("{}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void tailedTriangle() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(140, 3, 150, 4);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		StreamEdge c = new StreamEdge(130, 2, 150, 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(0, srcOneHopNeighbor.size());
		assertEquals("{}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void clique() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(120, 1, 140, 3);
		StreamEdge b = new StreamEdge(120, 1, 150, 4);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		StreamEdge d = new StreamEdge(130, 2, 150, 4);
		StreamEdge e = new StreamEdge(140, 3, 150, 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		utility.handleEdgeAddition(e, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(2, srcOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(2, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(2, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	
	}
	
	@Test
	public void quasiClique() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge b = new StreamEdge(120, 1, 150, 4);
		StreamEdge c = new StreamEdge(130, 2, 140, 3);
		StreamEdge d = new StreamEdge(130, 2, 150, 4);
		StreamEdge e = new StreamEdge(140, 3, 150, 4);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		utility.handleEdgeAddition(d, nodeMap);
		utility.handleEdgeAddition(e, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("{150 4}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(2, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(2, dstOneHopNeighbor.size());
		assertEquals("{140 3, 150 4}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void cycle() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(120, 1, 140, 3);
		StreamEdge b = new StreamEdge(130, 2, 150, 4);
		StreamEdge c = new StreamEdge(140, 3, 150, 4);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		utility.handleEdgeAddition(c, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 130, 2);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("{140 3}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(1, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(1, dstOneHopNeighbor.size());
		assertEquals("{150 4}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(1, subgraphs.size());
	}
	
	@Test
	public void failedTestWithTriangle() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		StreamEdge b = new StreamEdge(130, 2, 140, 3);
		utility.handleEdgeAddition(a, nodeMap);
		utility.handleEdgeAddition(b, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 140, 3);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("{130 2}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(1, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(1, dstOneHopNeighbor.size());
		assertEquals("{130 2}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(1, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(0, subgraphs.size());
	}
	
	@Test
	public void failedTestWithWedge() {
		QuadripletGenerator gen = new QuadripletGenerator();
		NodeMap nodeMap = new NodeMap();
		EdgeHandler utility = new EdgeHandler();
		
		StreamEdge a = new StreamEdge(120, 1, 130, 2);
		utility.handleEdgeAddition(a, nodeMap);
		
		StreamEdge edge = new StreamEdge(120, 1, 140, 3);
		
		LabeledNode src = new LabeledNode(edge.getSource(), edge.getSrcLabel());
		LabeledNode dst = new LabeledNode(edge.getDestination(), edge.getDstLabel());
		THashSet<LabeledNode> srcOneHopNeighbor = nodeMap.getNeighbors(src);
		assertEquals(1, srcOneHopNeighbor.size());
		assertEquals("{130 2}", srcOneHopNeighbor.toString());
		THashSet<Triplet> srcTwoHopNeighbors = nodeMap.getTwoHopNeighbors(src);
		assertEquals(0, srcTwoHopNeighbors.size());
		THashSet<LabeledNode> dstOneHopNeighbor = nodeMap.getNeighbors(dst);
		assertEquals(0, dstOneHopNeighbor.size());
		assertEquals("{}", dstOneHopNeighbor.toString());
		THashSet<Triplet> dstTwoHopNeighbors = nodeMap.getTwoHopNeighbors(dst);
		assertEquals(0, dstTwoHopNeighbors.size());
		
		Set<Quadriplet> subgraphs = gen.getAllSubgraphs(nodeMap, edge, src, dst, srcOneHopNeighbor, dstOneHopNeighbor, srcTwoHopNeighbors, dstTwoHopNeighbors);
		//sSystem.out.println(subgraphs);
		assertEquals(0, subgraphs.size());
	}

}
