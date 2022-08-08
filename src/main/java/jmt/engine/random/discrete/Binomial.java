package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.math.Arithmetic;
import jmt.engine.random.Parameter;

import java.util.ArrayList;

public class Binomial extends DiscreteDistribution {

	private int numberOfElements;
	private double probability;

	// Constructor with NO parameter, because the XML initialized the distribution parameter through `Parameter`.
	public Binomial(){ super(); }

	public Binomial(double probability, int numberOfElements) throws IncorrectDistributionParameterException {
		if ( numberOfElements<0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		if (probability<0 || probability>1) {
			throw new IncorrectDistributionParameterException("`probability` should be in range [0, 1]");
		}

		outdated();		// Set all cache flag false;
		this.probability = probability;
		this.numberOfElements = numberOfElements;
		this.cached = true;
	}

	public Binomial(Double probability, Integer numberOfElements) throws IncorrectDistributionParameterException {
		this(probability.doubleValue(), numberOfElements.intValue());
	}
	public Binomial(Integer numberOfElements, Double probability) throws IncorrectDistributionParameterException {
		this(probability.doubleValue(), numberOfElements.intValue());
	}

	public boolean updatePar(double probability, int numberOfElements)throws IncorrectDistributionParameterException{
		if ( numberOfElements<0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		if (probability<0 || probability>1) {
			throw new IncorrectDistributionParameterException("`probability` should be in range [0, 1]");
		}

		outdated();		// Set all cache flag false;
		this.probability = probability;
		this.numberOfElements = numberOfElements;
		this.cached = true;

		return true;
	}

	@Override
	public boolean updatePar(Parameter p) throws IncorrectDistributionParameterException{
		if (p instanceof BinomialPar && p.check()){
			return updatePar(((BinomialPar) p).getProbability(), ((BinomialPar) p).getNumberOfElements());
		}
		return false;
	}

	@Override
	public int getUpper() {
		if(cached){
			return numberOfElements;
		}
		return -1;
	}

	@Override
	public int getlower() {
		if(cached){
			return 0;
		}
		return -1;
	}

	/**
	 *	Cause we can calculate each CDF through one iteration, override it.
	 */
	@Override
	protected ArrayList<Double> createdCDFList(int lower, int upper) {
		if(cached){
			ArrayList<Double> lst = new ArrayList<Double>(upper-lower+1);
			double CDF = 0.0;
			for(int i=lower; i<=upper; i++){
				CDF += binomial_pmf(numberOfElements, i, probability);
				lst.add(i, CDF);
			}
			return lst;
		}
		return null;
	}

	@Override
	public int nextRand() {
		if(cached){
			if(!CDFListCalculated){
				this.CDFList = createdCDFList(0, numberOfElements);
				this.CDFListCalculated = true;
			}
			return binarySearch(0, numberOfElements, engine.nextDouble(), this.CDFList);
		}
		return -1;
	}

	@Override
	public double pmf(int x) {
		if(cached){
			if( x<0 || x>numberOfElements ){
				return 0.0;
			}
			return binomial_pmf(numberOfElements, x, probability);
		}
		return -1.0;
	}

	@Override
	public double cdf(int x) {
		if(cached){
			if (x < 0) {
				return 0.0;
			} else if (x >= numberOfElements) {
				return 1.0;
			}
			double result = 0.0;
			for(int i=0; i<=x; i++){
				result += binomial_pmf(numberOfElements, i, probability);
			}
			return result;
		}
		return -1.0;
	}

	@Override
	public double theorMean() {
		if(cached){
			return probability * numberOfElements;
		}
		return -1.0;
	}

	@Override
	public double theorVariance() {
		if(cached){
			return numberOfElements * probability * (1-probability);
		}
		return -1.0;
	}

	@Override
	public int nextRand(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof BinomialPar && p.check()) {
			BinomialPar up = (BinomialPar) p;
			int num = up.getNumberOfElements();
			double probability = up.getProbability();

			ArrayList<Double> lst = new ArrayList<Double>(num+1);
			double CDF = 0.0;
			for(int i=0; i<=num; i++){
				CDF += binomial_pmf(num, i, probability);
				lst.add(i, CDF);
			}
			return binarySearch(0, num, engine.nextDouble(), lst);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: the Parameter must be the `BinomialPar`\n" +
				"Error: numberOfElements must be a integer > 0\n" +
				"Error: Probability must be in range [0, 1]");
		}
	}

	@Override
	public double pmf(int x, Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof BinomialPar && p.check()) {
			BinomialPar up = (BinomialPar) p;
			int num = up.getNumberOfElements();
			double probability = up.getProbability();

			if( x<0 || x>num ){
				return 0.0;
			}
			return binomial_pmf(num, x, probability);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: the Parameter must be the `BinomialPar`\n" +
				"Error: numberOfElements must be a integer > 0\n" +
				"Error: Probability must be in range [0, 1]");
		}
	}

	@Override
	public double cdf(int x, Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof BinomialPar && p.check()) {
			BinomialPar up = (BinomialPar) p;
			int num = up.getNumberOfElements();
			double probability = up.getProbability();

			if (x < 0) {
				return 0.0;
			} else if (x >= num) {
				return 1.0;
			}
			double result = 0.0;
			for(int i=0; i<=x; i++){
				result += binomial_pmf(num, i, probability);
			}
			return result;
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: the Parameter must be the `BinomialPar`\n" +
				"Error: numberOfElements must be a integer > 0\n" +
				"Error: Probability must be in range [0, 1]");
		}
	}

	@Override
	public double theorMean(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof BinomialPar && p.check()) {
			BinomialPar up = (BinomialPar) p;
			return up.getProbability() * up.getNumberOfElements();
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: the Parameter must be the `BinomialPar`\n" +
				"Error: numberOfElements must be a integer > 0\n" +
				"Error: Probability must be in range [0, 1]");
		}
	}

	@Override
	public double theorVariance(Parameter p) throws IncorrectDistributionParameterException {
		if (p instanceof BinomialPar && p.check()) {
			BinomialPar up = (BinomialPar) p;
			double num = (double) up.getNumberOfElements();
			double probability = up.getProbability();
			return num * probability * (1-probability);
		} else {
			throw new IncorrectDistributionParameterException(
				"Error: the Parameter must be the `BinomialPar`\n" +
				"Error: numberOfElements must be a integer > 0\n" +
				"Error: Probability must be in range [0, 1]");
		}
	}

	private static double binomial_pmf(int n, int x, double p){
		return Arithmetic.binomial(n, x) * Math.pow(p, x) * Math.pow((1-p), (n-x));
	}
}
