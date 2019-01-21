package utility;

import static org.junit.Assert.*;

import org.junit.Test;

import reservoir.Reservoir;
import reservoir.SubgraphReservoir;

public class SubgraphReservoirTest {

	@Test
	public void test() {
		int M = 100;
		SubgraphReservoir<Integer> reservoir = new SubgraphReservoir<Integer>();
		for(int i = 0 ;i< 100000;i++) {
			if(reservoir.size()< M) {
				reservoir.add(i);
			}else {
				double value = M/(double)i;
				if(Math.random() < value) {
					Integer temp = reservoir.getRandom();
					reservoir.remove(temp);
					reservoir.add(i);
				}
			}
		}
		System.out.println(reservoir.size());
	}

}
