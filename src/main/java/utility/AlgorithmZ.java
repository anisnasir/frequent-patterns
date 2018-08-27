package utility;

import java.util.Random;

final public class AlgorithmZ {
	private static Random generator = new Random();
	private final int n;
	private double w;

	public AlgorithmZ(int n) {
		this.n = n;
		this.w = Math.exp(-Math.log(generator.nextDouble()) / n);
	}

	public int apply(int t) {
		int S = 0;
		if (t <= (22.0 * n)) {
			/* Process records using Algorithm X until t is large enough */
			double V = generator.nextDouble(); /* Generate V */
			S = 0;
			t += 1;
			/* Note: "num" in Vitter's code is always equal to t - n */
			double quot = (t - (double) n) / t;
			/* Find min S satisfying (4.1) */
			while (quot > V) {
				S += 1;
				t += 1;
				quot *= (t - (double) n) / (double) t;
			}
		} else {
			/* Now apply Algorithm Z */
			double W = this.w;
			double term = t - (double) n + 1;

			while (true) {
				double numer, numer_lim, denom;
				double U = generator.nextDouble();

				/* Generate U and X */
				double X = t * (W - 1.0);
				S = (int) X; /* S is tentatively set to floor(X) */
				/* Test if U <= h(S)/cg(X) in the manner of (6.3) */
				double tmp = (t + 1) / term;
				double lhs = Math.exp(Math.log(((U * tmp * tmp) * (term + S)) / (t + X)) / n);
				double rhs = (((t + X) / (term + S)) * term) / t;
				if (lhs <= rhs) {
					W = rhs / lhs;
					break;
				}
				/* Test if U <= f(S)/cg(X) */
				double y = (((U * (t + 1)) / term) * (t + S + 1)) / (t + X);
				if ((double) n < S) {
					denom = t;
					numer_lim = term + S;
				} else {
					denom = t - (double) n + S;
					numer_lim = t + 1;
				}
				for (numer = t + S; numer >= numer_lim; numer -= 1) {
					y *= numer / denom;
					denom -= 1;
				}
				W = Math.exp(-Math.log(generator.nextDouble()) / n); /* Generate W in advance */
				if (Math.exp(Math.log(y) / n) <= (t + X) / t)
					break;
			}
			this.w = W;
			// this.w = Math.exp(-Math.log(generator.nextDouble()) / n);;
		}
		return S;

	}
}
