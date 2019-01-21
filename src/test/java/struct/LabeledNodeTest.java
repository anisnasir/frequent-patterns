package struct;
import static org.junit.Assert.*;

import org.junit.Test;

import struct.LabeledNode;

public class LabeledNodeTest {

	@Test
	public void equalsTest() {
		LabeledNode n1 = new LabeledNode("s1", 1);
		LabeledNode n2 = new LabeledNode("s2", 2);
		assertEquals(false, (n1.equals(n2)));
	}
	@Test
	public void hashcodeTest() {
		LabeledNode n1 = new LabeledNode("s1", 1);
		LabeledNode n2 = new LabeledNode("s2", 2);
		LabeledNode n3 = new LabeledNode("s1", 1);
		System.out.println(n2.compareTo(n1));
		assertEquals(false, (n1.hashCode() == n2.hashCode()));
		assertTrue(n1.equals(n3));
	}

}
