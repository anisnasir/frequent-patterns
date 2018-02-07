package utility;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import gnu.trove.set.hash.THashSet;

public class BottomKSketch<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	UniformHasher MMhash;
	int k;
	PriorityQueue<Double> list;
	PriorityQueue<Double> remaining;
	THashSet<Double> set ;
	THashSet<Double> remainingSet ;
	public BottomKSketch(int k) {
		this.k = k ;
		MMhash = new UniformHasher();
		list = new PriorityQueue<Double>(k, Collections.reverseOrder());
		remaining = new PriorityQueue<Double>();
		set = new THashSet<Double>();
		remainingSet = new THashSet<Double>();
	}

	public boolean offerAll(THashSet<T> set) {
		for(T a: set) {
			offer(a);
		}
		return true;
	}
	public boolean offer(T o) {
		double hash = MMhash.hash(o.toString());
		if(set.contains(hash)) {
			return true;
		}else if(list.size() < k) {
			list.add(hash);
			set.add(hash);
			return true;
		} else {
			double peekElement = list.peek();
			if(peekElement < hash) {
				if(!remainingSet.contains(hash)) {
					remainingSet.add(hash);
					remaining.offer(hash);
				}
				return true;
			}else {
				double item = list.poll();
				set.remove(item);

				remainingSet.add(item);
				remaining.add(item);

				list.add(hash);
				set.add(hash);

				return true;
			}
		}

	}
	public boolean offer(Double hash) {
		if(set.contains(hash)) {
			return true;
		}else if(list.size() < k) {
			list.add(hash);
			set.add(hash);
			return true;
		} else {
			double peekElement = list.peek();
			if(peekElement < hash) {
				if(!remainingSet.contains(hash)) {
					remainingSet.add(hash);
					remaining.offer(hash);
				}
				return true;
			}else {
				double item = list.poll();
				set.remove(item);
				
				remainingSet.add(item);
				remaining.add(item);
				
				list.add(hash);
				set.add(hash);
				return true;
			}
		}

	}

	public int cardinality() {
		return (int) (list.size()<k? list.size(): ((k-1)/list.peek()));
	}

	public boolean union(BottomKSketch<T> Y) {
		PriorityQueue<Double> otherList = Y.getSortedList();
		this.k = Math.min(this.k, Y.getKValue());

		while(list.size() > k) {
			double value = list.poll();
			set.remove(value);
		}
		for( Double value : otherList )  {
			offer(value);
		}


		return true;
	}

	public int getKValue() {
		return this.k;
	}
	public PriorityQueue<Double> getSortedList() {
		return this.list;
	}
	public THashSet<Double> getSet() {
		return this.set;

	}
	public int unionCardinality(BottomKSketch<T> Y) {
		BottomKSketch<T> X = new BottomKSketch<T>(this.k);
		X.union(this);
		X.union(Y);
		return X.cardinality();
	}
	public int unionImprovedCardinality(BottomKSketch<T> Y) {
		
		if(Y.list.size() < 1) {
			return this.list.size();
		}
		if(list.size() < 1) {
			return Y.list.size();
		}
		
		
		BottomKSketch<T> A = new BottomKSketch<T>(this.k);
		A.union(this);
		BottomKSketch<T> B = new BottomKSketch<T>(Y.k);
		B.union(Y);

		double peekValue = Math.min(A.list.peek(), B.list.peek());

		
		while(A.list.size() > 0) {
			if(A.list.peek() > peekValue) {
				double value = A.list.poll();
				A.set.remove(value);
			} else
				break;

		}
		
		while(B.list.size() > 0 ) {
			if(B.list.peek() > peekValue) {
				double value = B.list.poll();
				B.set.remove(value);
			}else 
				break;
		}
		THashSet<Double> filter = new THashSet<Double>();
		filter.addAll(A.set);
		filter.addAll(B.set);
		double cardinality = (filter.size()-1)/peekValue;
		return (int) cardinality;
	}

	public int intersectionCardinality(BottomKSketch<T> Y) {
		int results = this.cardinality() + Y.cardinality() - this.unionCardinality(Y);
		return results;
	}
	public int intersectionImprovedCardinality(BottomKSketch<T> Y) {
		if(Y == null) 
			return 0;
		else if (Y.list.size() ==0) {
			return 0;
		}
		SetFunctions<Double> helper = new SetFunctions<Double>();
		THashSet<Double> intersection = helper.intersectionSet(set, Y.getSet());

		double peekValue = Math.min(list.peek(), Y.list.peek());

		int delta = 0;
		for(double value: intersection) {
			if(value > peekValue)
				delta = 1;
		}
		int results = (int)((intersection.size()-delta)/peekValue);
		return results;
	}
	
	public void remove(T o) {
		double hash = MMhash.hash(o.toString());
		if(set.contains(hash)) {
			set.remove(hash);
			list.remove(hash);
			
			if(list.size() < k && remainingSet.size() > 0) {
				double value = remaining.poll();
				remainingSet.remove(value);
				
				list.offer(value);
				set.add(value);
			}
		}
	}



}