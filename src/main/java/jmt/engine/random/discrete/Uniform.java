package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.random.AbstractDistribution;
import jmt.engine.random.Parameter;

// extend the `AbstractDistribution` to get the random engine.
// implement the `DiscreteDistribution` to define the behavior of this distribution.
public class Uniform extends AbstractDistribution implements DiscreteDistribution {

	// Constructor with NO parameter, because the XML initialized the distribution parameter through Parameter.
	public Uniform(){
	}

	@Override
	public int nextRand(Parameter p) throws IncorrectDistributionParameterException{
		if(p instanceof UniformPar && p.check()){
			UniformPar up = (UniformPar) p;
			double min = (double) up.getMin();
			double max = (double) up.getMax();
			return (int) Math.ceil((min - 1) + ((1 + max - min) * engine.raw()));
		} else {
			throw new IncorrectDistributionParameterException(
					"Error: the *max* parameter must be greater than the *min* one\n"+
					"Error: the Parameter must be the `UniformPar`");
		}
	}

	public boolean nextBoolean() {
		return engine.raw() > 0.5;
	}

	@Override
	public double pmf(int x, Parameter p) throws IncorrectDistributionParameterException{
		if(p instanceof UniformPar && p.check()){
			UniformPar up = (UniformPar) p;
			double min = (double) up.getMin();
			double max = (double) up.getMax();
			if (x <= min || x >= max) {
				return 0.0;  //if x is out of bound return 0
			}
			return 1.0 / (max - min + 1);
		} else {
			throw new IncorrectDistributionParameterException(
					"Error: the *max* parameter must be greater than the *min* one\n"+
					"Error: the Parameter must be the `UniformPar`");
		}
	}

	@Override
	public double cdf(int x, Parameter p) throws IncorrectDistributionParameterException{
		if(p instanceof UniformPar && p.check()){
			UniformPar up = (UniformPar) p;
			double min = (double) up.getMin();
			double max = (double) up.getMax();
			if (x <= min) {
				return 0.0;  //if x is lower than the min bound return 0
			} else if(x >= max){
				return 1.0;  //if x is greater than the max bound return 1
			}
			return (x - min + 1) / (max - min + 1);
		} else {
			throw new IncorrectDistributionParameterException(
					"Error: the *max* parameter must be greater than the *min* one\n"+
					"Error: the Parameter must be the `UniformPar`");
		}
	}

	@Override
	public double theorMean(Parameter p) throws IncorrectDistributionParameterException{
		if(p instanceof UniformPar && p.check()){
			UniformPar up = (UniformPar) p;
			double min = (double) up.getMin();
			double max = (double) up.getMax();
			return (max + min) / 2.0;
		} else {
			throw new IncorrectDistributionParameterException(
					"Error: the *max* parameter must be greater than the *min* one\n"+
					"Error: the Parameter must be the `UniformPar`");
		}
	}

	@Override
	public double theorVariance(Parameter p) throws IncorrectDistributionParameterException{
		if(p instanceof UniformPar && p.check()){
			UniformPar up = (UniformPar) p;
			double min = (double) up.getMin();
			double max = (double) up.getMax();
			return (Math.pow((max - min + 1), 2.0) - 1) / 12.0;
		} else {
			throw new IncorrectDistributionParameterException(
					"Error: the *max* parameter must be greater than the *min* one\n"+
					"Error: the Parameter must be the `UniformPar`");
		}
	}
}
