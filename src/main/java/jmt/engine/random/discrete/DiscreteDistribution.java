package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.random.Parameter;

public interface DiscreteDistribution {

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
