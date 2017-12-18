package utility;

import java.util.Random;

final public class Z2 {
    private static Random generator = new Random();

    private final int n;

    private double w;

    public Z2(int n) {
        this.n = n;
        this.w = Math.exp(-Math.log(generator.nextDouble()) / n);
    }

    public long apply(long t) {
        double term = t - this.n + 1;
        double u;
        double x;
        long gamma;
        while (true) {
            //generate u and x
            u = generator.nextDouble();
            x = t * (this.w - 1.0);
            gamma = (long) x;
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
