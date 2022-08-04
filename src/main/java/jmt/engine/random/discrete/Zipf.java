package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.random.AbstractDistribution;
import jmt.engine.random.Parameter;

public class Zipf extends AbstractDistribution implements DiscreteDistribution {

	private double alpha;
	private int max;

	private boolean cached;
	private double Hmax;
	private boolean meanCalculated;
	private double mean;
	private boolean varCalculated;
	private double variance;


	// This constructor is used for method with outside `Parameter`
	// In XML the `distribution` and `distributionPar` initialize separately.
	public Zipf(){
		this.cached = false;
	}

	// This constructor is used for directly assigning the parameter
	// In XML, assigning the distribution parameter through <subParameter>.
	public Zipf(double alpha, int numberOfElements) throws IncorrectDistributionParameterException{
		if ( numberOfElements<=0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		if ( alpha<=0) {
			throw new IncorrectDistributionParameterException("`alpha` should be positive.");
		}
		this.alpha = alpha;
		this.max = numberOfElements;
		this.Hmax = Harmonic(max, alpha);
		this.cached = true;
		this.varCalculated = false;
		this.meanCalculated = false;
	}

	public Zipf(Double alpha, Integer numberOfElements) throws IncorrectDistributionParameterException{
		this(alpha.doubleValue(), numberOfElements.intValue());
	}

	public int nextRand(){
		if(cached){
			return getRandVar(max, alpha);
		}
		return -1;
	}

	public double pmf(int x){
		if(cached){
			if (x <= 0 || x > max) {
				return 0.0;
			}
			return 1/(Math.pow(x, alpha) * Harmonic(max, alpha));
		}
		return -1.0;
	}

	public double cdf(int x){
		if(cached) {
			if (x <= 0) {
				return 0.0;
			} else if (x >= max) {
				return 1.0;
			}
			return Harmonic(x, alpha) / Hmax;
		}
		return -1.0;
	}

	public double theorMean(){
		if(cached){
			if(!meanCalculated){
				this.mean = Harmonic(max, alpha-1) / Hmax;
			}
			return this.mean;
		}
		return -1.0;
	}

	public double theorVariance(){
		if(cached){
			if(!varCalculated) {
				double hs1 = Harmonic(max, alpha - 1);
				double hs2 = Harmonic(max, alpha - 2);
				this.variance = hs2 / Hmax - (hs1 * hs1) / (Hmax * Hmax);
			}
			return this.variance;
		}
		return -1.0;
	}

	@Override
	public int nextRand(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof ZipfPar && p.check()) {
			ZipfPar up = (ZipfPar) p;
			double alpha = up.getAlpha();
			int max = up.getMax();

			return getRandVar(max, alpha);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: Max must be a integer > 0\n" +
				"Error: Alpha must be (0, 100), to avoid the data oveflow\n" +
				"Error: the Parameter must be the `ZipfPar`");
		}
	}

	@Override
	public double pmf(int x, Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof ZipfPar && p.check()) {
			ZipfPar up = (ZipfPar) p;
			double alpha = up.getAlpha();
			int max = up.getMax();

			if (x <= 0 || x > max) {
				return 0.0;
			}
			return 1/(Math.pow(x, alpha) * Harmonic(max, alpha));
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: Max must be a integer > 0\n" +
				"Error: Alpha must be (0, 100), to avoid the data oveflow\n" +
				"Error: the Parameter must be the `ZipfPar`");
		}
	}

	@Override
	public double cdf(int x, Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof ZipfPar && p.check()) {
			ZipfPar up = (ZipfPar) p;
			double alpha = up.getAlpha();
			int max = up.getMax();

			if (x <= 0) {
				return 0.0;
			} else if (x >= max) {
				return 1.0;
			}
			return Harmonic(x, alpha) / Harmonic(max, alpha);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: Max must be a integer > 0\n" +
				"Error: Alpha must be (0, 100), to avoid the data oveflow\n" +
				"Error: the Parameter must be the `ZipfPar`");
		}
	}

	@Override
	public double theorMean(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof ZipfPar && p.check()) {
			ZipfPar up = (ZipfPar) p;
			double alpha = up.getAlpha();
			int max = up.getMax();

			return Harmonic(max, alpha-1) / Harmonic(max, alpha);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: Max must be a integer > 0\n" +
				"Error: Alpha must be (0, 100), to avoid the data oveflow\n" +
				"Error: the Parameter must be the `ZipfPar`");
		}
	}

	@Override
	public double theorVariance(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof ZipfPar && p.check()) {
			ZipfPar up = (ZipfPar) p;
			double alpha = up.getAlpha();
			int max = up.getMax();

			double hs1 = Harmonic(max, alpha-1);
			double hs2 = Harmonic(max, alpha-2);
			double hmax = Harmonic(max, alpha);
			return hs2/hmax - (hs1 * hs1)/(hmax * hmax);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: Max must be a integer > 0\n" +
				"Error: Alpha must be (0, 100), to avoid the data oveflow\n" +
				"Error: the Parameter must be the `ZipfPar`");
		}
	}

	public void updatePar(double alpha, int numberOfElements)throws IncorrectDistributionParameterException{
		if ( numberOfElements<=0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		if ( alpha<=0) {
			throw new IncorrectDistributionParameterException("`alpha` should be positive.");
		}
		this.alpha = alpha;
		this.max = numberOfElements;
		this.Hmax = Harmonic(max, alpha);
		this.cached = true;
		this.varCalculated = false;
		this.meanCalculated = false;
	}

	public void outdated(){
		this.cached = false;
		this.varCalculated = false;
		this.meanCalculated = false;
	}

	private static double Harmonic(int num, double alpha){
		double total = 0.0;
		for(int i=1; i<=num; i++){
			total += 1 / Math.pow(i, alpha);
		}
		return total;
	}

	private int getRandVar(int max, double alpha){
		double hIntegral_x = hIntegral(1.5, alpha) - 1.0;
		double hIntegral_max = hIntegral(max + 0.5, alpha);
		double s = 2.0 - hIntegralInverse(hIntegral(2.5, alpha) - h(2, alpha), alpha);

		while (true) {
			// u is uniformly distributed in (hIntegralX1, hIntegralNumberOfElements]
			double u = hIntegral_max
					+ engine.nextDouble() * (hIntegral_x - hIntegral_max);

			double x = hIntegralInverse(u, alpha);

			int k = (int) (x + 0.5);

			// Limit k to the range [1, numberOfElements]
			if (k < 1) {
				k = 1;
			} else if (k > max) {
				k = max;
			}

			if (k - x <= s || u >= hIntegral(k + 0.5, alpha) - h(k, alpha)) {
				return k;
			}
		}
	}

	private static double h(double x, double alpha) {
		return Math.exp(-alpha * Math.log(x));
	}

	private static double hIntegral(double x, double alpha) {
		double logX = Math.log(x);
		return func2((1.0 - alpha) * logX) * logX;
	}

	private static double hIntegralInverse(double x, double alpha) {
		double t = x * (1.0 - alpha);
		// t should in range [-1, +inf).
		if (t < -1.0) { t = -1; }
		return Math.exp(func1(t) * x);
	}

	// A Taylor series expansion is used, if x is close to 0.
	private static double func1(double x) {
		if (Math.abs(x) > 1e-8) {
			return Math.log1p(x) / x;
		} else {
			return 1.0 - x * ((1.0 / 2.0) - x * ((1.0 / 3.0) - x * (1.0 / 4.0)));
		}
	}

	// A Taylor series expansion is used, if x is close to 0.
	private static double func2(double x) {
		if (Math.abs(x) > 1e-8) {
			return Math.expm1(x) / x;
		} else {
			return 1.0 + x * (1.0 / 2.0) * (1.0 + x * (1.0 / 3.0) * (1.0 + x * (1.0 / 4.0)));
		}
	}
}

