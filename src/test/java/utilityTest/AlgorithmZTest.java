package utilityTest;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.AlgorithmZ;

public class AlgorithmZTest {

	@Test
	public void test() {
		int NUM_ITEMS = 10000;
		int M = 100;
		AlgorithmZ temp = new AlgorithmZ(M);
		int i = 0;
		int count = 1;
		while(i<NUM_ITEMS) {
			int skip = temp.apply(i);
			i+=skip+1;
			count++;
			//System.out.println(skip + " "  +(count++) + " " + i);
		}
		
		double t = 0; 
		int c = 0;
		while(t < NUM_ITEMS) {
			if(t<M) {
				c++;
			}else if(Math.random() < M/(t)){
				c++;
			}
			t++;
		}
		
		System.out.println("reservoir "+ c + " skip counter " + count);
	}

}
