package utilityTest;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.Z1;

public class ZTest {

	@Test
	public void test() {
		int NUM_ITEMS = 10000;
		int M = 100;
		Z1 temp = new Z1(M);
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
