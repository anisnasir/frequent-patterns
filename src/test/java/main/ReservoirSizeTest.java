package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReservoirSizeTest {

	@Test
	public void test() {
		double delta = 0.1;
		double epsilon = 0.01;
		double M = (4*(1+Math.log(1/delta)))/(epsilon*epsilon);
		Integer size = (int) Math.round(M);
		System.out.println(size);
	}

}
