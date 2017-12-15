package utility;

import java.util.Random;

final public class Z  {
	private static Random generator = new Random();
	private final int n;
	private double w;
	public Z(int n) {
		this.n = n;
		this.w = Math.exp(-Math.log(generator.nextDouble()) / n);
	}

	public int apply(int t) {
		int S;
		if (t <= (22.0 * n))
		{
			/* Process records using Algorithm X until t is large enough */
			double		V,
			quot;

			V = generator.nextDouble();	/* Generate V */
			S = 0;
			t += 1;
			/* Note: "num" in Vitter's code is always equal to t - n */
			quot = (t - (double) n) / t;
			/* Find min S satisfying (4.1) */
			while (quot > V)
			{
				S += 1;
				t += 1;
				quot *= (t - (double) n) / t;
			}
			return S;
		}
		else
		{
			double term = t - this.n + 1;
			double u;
			double x;
			int gamma;
			while (true) {
				//generate u and x
				u = generator.nextDouble();
				x = t * (this.w - 1.0);
				gamma = (int) x;
				//test if u <= h(gamma)/cg(x)
				double lhs = Math.exp(Math.log(((u * Math.pow(((t + 1) / term), 2)) * (term + gamma)) / (t + x)) / this.n);
				double rhs = (((t + x) / (term + gamma)) * term) / t;
				if (lhs < rhs) {
					this.w = rhs / lhs;
					break;
				}
				//test if u <= f(gamma)/cg(x)
				double y = (((u * (t + 1)) / term) * (t + gamma + 1)) / (t + x);
				double denom;
				double number_lim;
				if (this.n < gamma) {
					denom = t;
					number_lim = term + gamma;
				} else {
					denom = t - this.n + gamma;
					number_lim = t + 1;
				}

				for (long number = t + gamma; number >= number_lim; number--) {
					y = (y * number) / denom;
					denom = denom - 1;
				}
				this.w = Math.exp(-Math.log(generator.nextDouble()) / this.n);
				if (Math.exp(Math.log(y) / this.n) <= (t + x) / t) {
					break;
				}
			}
			return gamma;
		}
		

	}
}
