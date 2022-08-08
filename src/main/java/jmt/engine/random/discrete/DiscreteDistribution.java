package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.random.AbstractDistribution;
import jmt.engine.random.Parameter;

import java.util.ArrayList;

public abstract class DiscreteDistribution extends AbstractDistribution{

	/*
	 This Class extends from `AbstractDistribution`, also has a field - RandomEngine and its corresponding setter.
	 This function is invoked by `SimLoader`, if is is an instance of AbstractDistribution
	 protected RandomEngine engine;
	 public void setRandomEngine(RandomEngine engine) { this.engine = engine;}
	*/

	protected boolean cached;
	protected ArrayList<Double> CDFList;
	protected boolean CDFListCalculated;

	public DiscreteDistribution(){
		cached = false;
		CDFListCalculated = false;
		CDFList = null;
	}

	/**
	 * Set all cache flag false, call it when parameters changed.
	 */
	public void outdated(){
		this.cached = false;
		this.CDFListCalculated = false;
	}

	/**
	 *	Sub-class must implement this method to update parameter.
	 */
	public abstract boolean updatePar(Parameter p) throws IncorrectDistributionParameterException;

	public abstract int getUpper();
	public abstract int getlower();

	/**
	 * 	Inverse transform sampling
	 * 	 Take an uniform distribution -  u (0, 1) as the input probability.
	 * 	 Find the CDF section that hold the probability generated by uniform distribution -  u (0, 1).
	 * 	 the upper bound of target section is the random variable that follow the specific distribution.
	 *  <br><br>
	 *  In each implementation of discrete distribution, the cumulative probability is calculated through `cdf()`
	 *  this function will create the CDF List for each unit, because the cumulative probability is monotone increasing,
	 *  use binary search to find the target section.
	 */
	protected int inverseTransformSampling(final int lower, final int upper){
		if(cached){
			if(!CDFListCalculated){
				this.CDFList = createdCDFList(lower, upper);
				this.CDFListCalculated = true;
			}
			// should call `setRandomEngine()` first ,and engine.nextDouble() should not be null.
			return binarySearch(lower, upper, engine.nextDouble(), this.CDFList);
		}
		return -1;
	}

	/**
	 * Calculate the cumulative distribution value for each discrete random variable.
	 * It depends on the `cdf()` implementation from its subclass.
	 */
	protected ArrayList<Double> createdCDFList(int lower, int upper){
		if(cached){
			ArrayList<Double> lst = new ArrayList<Double>(upper-lower+1);
			for(int i=lower; i<=upper; i++){
				lst.add(i, (Double) this.cdf(i));
			}
			return lst;
		}
		return null;
	}

	protected ArrayList<Double> createdCDFList(int lower, int upper, Parameter p) throws IncorrectDistributionParameterException {
		ArrayList<Double> lst = new ArrayList<Double>(upper-lower+1);
		for(int i=lower; i<=upper; i++){
			lst.add(i, (Double) this.cdf(i, p));
		}
		return lst;
	}

	protected static int binarySearch(final int lower, final int upper, final double probability, final ArrayList<Double> CDF_List){
		if (lower+1==upper){
			return upper;
		} else {
			int mid = (int)(lower+upper)/2;
			if(probability> CDF_List.get(lower) && probability< CDF_List.get(mid)){
				return binarySearch(lower, mid, probability, CDF_List);
			} else {
				return binarySearch(mid, upper, probability, CDF_List);
			}
		}
	}


	/**
	 * 	 These functions are used to direct initialize the Discrete Distribution.
	 * 	 once invoke parameter constructor with the distribution parameter, set cached = true.
	 * 	 if not cache the parameter, only method with `Parameter` are available.
	 */

	public abstract int nextRand();
	public abstract double pmf(int x);
	public abstract double cdf(int x);
	public abstract double theorMean();
	public abstract double theorVariance();

	/**
	 * Generate random variables that follow an probability distribution
	 * @param p parameter of the distribution.
	 * @return int - random variables that follow an probability distribution
	 * @throws IncorrectDistributionParameterException
	 */
	public abstract int nextRand(Parameter p) throws IncorrectDistributionParameterException;

	/**
	 * obtain the probability distribution from the distribution
	 * @param x int - random variable indicating where to evaluate the pmf.
	 * @param p parameter of the distribution.
	 * @return double - the probability weight evaluated at x.
	 * @throws IncorrectDistributionParameterException
	 */
	public abstract double pmf(int x, Parameter p) throws IncorrectDistributionParameterException;

	/**
	 * obtain the cumulative distribution from the distribution
	 * @param x int - random variable indicating where to evaluate the cdf.
	 * @param p parameter of the distribution.
	 * @return double - the probability weight cumulated at x.
	 * @throws IncorrectDistributionParameterException
	 */
	public abstract double cdf(int x, Parameter p) throws IncorrectDistributionParameterException;;


	/**
	 * Calculate the Mean value from the parameter of the distribution
	 * @param p parameter of the distribution.
	 * @return double - theoretic mean value of the distribution.
	 * @throws IncorrectDistributionParameterException
	 */
	public abstract double theorMean(Parameter p) throws IncorrectDistributionParameterException;

	/**
	 * Calculate the Variance from the parameter of the distribution
	 * @param p parameter of the distribution.
	 * @return double - theoretic variance value of the distribution.
	 * @throws IncorrectDistributionParameterException
	 */
	public abstract double theorVariance(Parameter p) throws IncorrectDistributionParameterException;

}
