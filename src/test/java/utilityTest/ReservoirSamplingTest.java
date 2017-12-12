package utilityTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import utility.ReservoirSampling;

public class ReservoirSamplingTest {

	@Test
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0 ;i <10;i++) {
			list.add(i);
		}
		ReservoirSampling reservoir = new ReservoirSampling();
		assertEquals(3, reservoir.selectKItems(list, 3).size());
		System.out.println(reservoir.selectKItems(list, 3));
	}

}
