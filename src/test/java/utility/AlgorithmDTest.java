package utility;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.AlgorithmD;

public class AlgorithmDTest {

	@Test
	public void algorithmATest() {
		AlgorithmD tester = new AlgorithmD();
		final int NUM_ITEMS = 100;
		for(int i =1;i<NUM_ITEMS;i++) {
			int c1 = i%NUM_ITEMS;
			int c2 = NUM_ITEMS;
			int skip = tester.vitter_a_skip(c1, c2);
			
			System.out.println(skip);
		}
	}
	@Test
	public void algorithmDTest() {
		AlgorithmD tester = new AlgorithmD();
		final int NUM_ITEMS = 100;
		for(int i =1;i<NUM_ITEMS;i++) {
			int c1 = i%NUM_ITEMS;
			int c2 = NUM_ITEMS;
			int skip = tester.vitter_d_skip(c1, c2);
			System.out.println(skip);
		}
	}

}
