package utility;

import org.junit.Test;

public class ReservoirSizeTest {

	@Test
	public void test() {
		double epsilon = 0.01; 
		double delta = 0.15;
		int Tk  = 11244966;
		double epsilonk = (4 + epsilon) / (epsilon * epsilon);
		double Tkk = Math.log(Tk / delta);
		int size = (int) (Tkk * epsilonk);
		System.out.println(size);
	}

}
