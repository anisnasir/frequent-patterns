package utility;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class UniformHasher {
	HashFunction MMhash;
	int prime = 17;
	public UniformHasher() { 
		MMhash = Hashing.murmur3_128(prime);
	}
	public double hash(String s) {
		int hash = Math.abs(MMhash.hashBytes(s.getBytes()).asInt());
		hash %=Integer.MAX_VALUE;
		double doubleHash = hash/(double)Integer.MAX_VALUE;
		return doubleHash;
	}
}
