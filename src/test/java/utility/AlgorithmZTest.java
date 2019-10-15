package utility;

import org.junit.Test;

import utility.AlgorithmZ;

public class AlgorithmZTest {

	@Test
	public void test() {
		int NUM_ITEMS = 1000000;
		int M = 100;
		AlgorithmZ temp = new AlgorithmZ(M);
		int i = 0;
		int count = 1;
		int totalSkip = 0;
		long startTime = System.nanoTime();
		while (i < NUM_ITEMS) {
			int skip = temp.apply(i);
			i += skip + 1;
			count++;
			totalSkip+=skip;
			//System.out.println(skip + " " + (count) + " " + i);
		}

		System.out.println("execution time: " + ((System.nanoTime()-startTime)/1000));
		startTime = System.nanoTime();
		
		double t = 0;
		int c = 0;
		while (t < NUM_ITEMS) {
			if (t < M) {
				c++;
			} else if (Math.random() < M / (t)) {
				c++;
			}
			t++;
		}
		System.out.println("execution time: " + ((System.nanoTime()-startTime)/1000));
		System.out.println("reservoir " + c + " skip counter " + count);
		System.out.println("skipped " + totalSkip + " total skipped " + (NUM_ITEMS-c));
	}

}
