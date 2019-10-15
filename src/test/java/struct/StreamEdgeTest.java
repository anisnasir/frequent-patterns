package struct;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;

public class StreamEdgeTest {

	@Test
	public void test() {
		StreamEdge a = new StreamEdge(120,1, 130, 2);
		StreamEdge b = new StreamEdge(120,1 , 130 , 2);
		assertEquals(true, (a.compareTo(b) == 0) );
	}

}
