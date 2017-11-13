package utilityTest;

import static org.junit.Assert.*;

import org.junit.Test;

import utility.Z;

public class ZTest {

	@Test
	public void test() {
		Z temp = new Z(20);
		for(int i = 1;i< 100; i++) {
			System.out.println(temp.apply(i));
		}
	}

}
