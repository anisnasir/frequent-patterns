package utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberTest {

	@Test
	public void test() {
		double N = 565612313;
		double M = 715579;
		double correctFactor = Math.max(1, ((double) N / M));
		int count = 100478;
		double value = count * correctFactor;
		int answer = (int)value;
		System.out.println(answer);
	}

}
