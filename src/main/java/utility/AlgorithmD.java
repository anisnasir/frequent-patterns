package utility;

import java.util.Random;

public class AlgorithmD {
	Random rand;
	int vitter_d_alpha_inverse;
	public AlgorithmD() {
		rand = new Random();
		vitter_d_alpha_inverse = 13;
	}

	public int vitter_a_skip(int n, int N)
	{
		if(n<1) {
			return N;
		}else if(n==1) {
			return rand.nextInt(N ); 
		}else {
			int S;
			double top, Nreal, V,quot;
			Nreal = N;
			S = 0;
			top = Nreal - n;
			quot = top/(Nreal);
			V = Math.random();

			while (quot > V)
			{
				++S;
				quot *= (top) / (Nreal);
			}
			return S;
		}
	}

	public int vitter_d_skip(int n , int N) {
		int S;
		int top, t, limit;
		int qu1 = 1 + N - n;
		double X, y1, y2, bottom;

		double nreal = n ;
		double ninv = 1/nreal;
		double Nreal = N ;
		double Vprime = Math.exp(Math.log(Math.random())*ninv);
		double qu1real = 1-nreal+Nreal;
		double nmin1inv = 1/(nreal-1);

		if(n < 1) {
			return N;
		}else if ( (vitter_d_alpha_inverse *nreal) > Nreal)  {
			return vitter_a_skip(n, N);
		}
		else
		{
			while(true) {
				for(X = Nreal * (1 - Vprime), S = (int)(X);
						S >= qu1real;
						X = Nreal * (1 - Vprime), S = (int)(X)) {
					Vprime = Math.exp(Math.log(Math.random())*ninv);
				}

				double U = Math.random();
				double negSreal = -1*S;

				y1 = Math.exp(Math.log(U*Nreal/qu1real)*nmin1inv);
				Vprime = y1*(((-1*X)/Nreal)+1)*(qu1real/(negSreal+qu1real));

				if(Vprime <=1) {
					break;
				}

				y2 =1;
				top = (int)Nreal-1;


				if((nreal-1)>S) {
					bottom = Nreal-nreal;
					limit = (int) (Nreal - S);
				}else {
					bottom = Nreal - negSreal-1;
					limit = qu1;
				}

				for(t = (int)(Nreal-1); t>=limit;t--) {
					y2 = (y2*top)/bottom;
					top = top-1;
					bottom = bottom-1;
				}

				if((Nreal/(Nreal-X)) >= (y1*Math.exp(Math.log(y2)*nmin1inv))) {
					Vprime = Math.exp(Math.log(Math.random())*nmin1inv);
					break;
				}
				Vprime = Math.exp(Math.log(Math.random())*ninv);

			}
			return S;
		}
	}

}
