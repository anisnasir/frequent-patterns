package utilityTest;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.Z1;

public class ZTest {

	@Test
	public void test() {
		Z1 temp = new Z1(20);
		int i = 0;
		while(i<100) {
			int skip = temp.apply(i);
			i+=skip+1;
			System.out.println(skip);
		}
	}

}
