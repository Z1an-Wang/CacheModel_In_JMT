package jmt.engine.random.discrete;

import jmt.common.exception.IncorrectDistributionParameterException;
import jmt.engine.random.AbstractParameter;

public class BinomialPar extends AbstractParameter {

	private int numberOfElements;
	private double probability;

	public BinomialPar(int numberOfElements, double probability) throws IncorrectDistributionParameterException {
		if ( numberOfElements<0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		if (probability<0 || probability>1) {
			throw new IncorrectDistributionParameterException("`probability` should be in range [0, 1]");
		}

		this.numberOfElements = numberOfElements;
		this.probability = probability;
	}

	public BinomialPar(Integer numberOfElements, Double probability) throws IncorrectDistributionParameterException {
		this(numberOfElements.intValue(), probability.doubleValue());
	}
	public BinomialPar(Double probability, Integer numberOfElements) throws IncorrectDistributionParameterException {
		this(numberOfElements.intValue(), probability.doubleValue());
	}

	@Override
	public boolean check(){
		return numberOfElements>=0 && probability>=0 && probability<=1;
	}

	public int getNumberOfElements(){
		return numberOfElements;
	}

	public double getProbability(){
		return probability;
	}

	public void setNumberOfElements(int numberOfElements ) throws IncorrectDistributionParameterException {
		if ( numberOfElements<0) {
			throw new IncorrectDistributionParameterException("`numberOfElements` should be positive.");
		}
		this.numberOfElements = numberOfElements;
	}

	public void setProbability(double probability ) throws IncorrectDistributionParameterException {
		if (probability<0 || probability>1) {
			throw new IncorrectDistributionParameterException("`probability` should be in range [0, 1]");
		}
		this.probability = probability;
	}
}
