package utilityTest;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.Z1;

public class ZTest {

	@Test
	public void test() {
		Z1 temp = new Z1(20);
		int i = 0;
		int count = 1;
		while(i<100) {
			int skip = temp.apply(i);
			i+=skip+1;
			System.out.println(skip + " "  +(count++) + " " + i);
		}
		
		int M = 20;
		double t = 0; 
		int c = 0;
		while(t < 100) {
			if(t<M) {
				c++;
			}else if(Math.random() < M/(t)){
				c++;
			}
			
			System.out.println(t+" " + c);
			t++;
		}
	}

}
