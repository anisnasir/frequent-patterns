package structTest;

import static org.junit.Assert.*;

import org.junit.Test;

import input.StreamEdge;

public class StreamEdgeTest {

	@Test
	public void test() {
		StreamEdge a = new StreamEdge("a",1, "b", 2,"B");
		StreamEdge b = new StreamEdge("a",1 , "b" , 2,"B");
		assertEquals(true, (a.compareTo(b) == 0) );
	}

}
