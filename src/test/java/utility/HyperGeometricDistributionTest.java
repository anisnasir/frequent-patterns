package utility;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;

public class HyperGeometricDistributionTest {

	@Test
	public void test() {
		int populationSize = 25;
		int numberOfSuccesses = 4;
		int sampleSize = 6;
		HypergeometricDistribution hyper = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
		System.out.println(hyper.cumulativeProbability(0,2));
		
		
		double sum = 0; 
		int numberOfSamples =4; 
		for(int i = 0 ; i< numberOfSamples; i++) {
			sum += ((choose(4,i)*choose(21, 6-i))/(double)choose(25,6));
			System.out.println(sum);
		}
		
		System.out.println(sum);
	}
	
	private long choose(long total, long choose){
	    if(total < choose)
	        return 0;
	    if(choose == 0 || choose == total)
	        return 1;
	    return choose(total-1,choose-1)+choose(total-1,choose);
	}


}
