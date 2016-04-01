package com.github.kreatures.core.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import com.github.kreatures.core.operators.BeliefOperatorFamilyFactory;
import com.github.kreatures.core.operators.ContinuousBeliefOperatorFamily;
import com.github.kreatures.core.operators.StepIteratorStrategy;

/**
 * This class is responsible to de/serialize the configuration for continuous
 * belief-operator-families. It contains a min-value, a max-value, the class
 * name of the used belief operator (reasoner), the parameter-name and the
 * iterator class name.
 * 
 * The min and max value are optional and there default values are zero and one.
 * The iterator class name is also optional and if it is not given then
 * {@link StepIteratorStrategy} is used.
 * 
 * To create a {@link ContinuousBeliefOperatorFamily} the factory 
 * {@link BeliefOperatorFamilyFactory} is used.
 * @author Tim Janus
 */
public class ContinuousBeliefOperatorFamilyConfig extends
		BeliefOperatorFamilyConfig {
	@Attribute(name="min-value", required=false)
	private double minValue = 0;
	
	@Attribute(name="max-value", required=false)
	private double maxValue = 1.0;
	
	@Element(name="operator", required=true)
	private String operatorCls;
	
	@Element(name="parameter", required=true)
	private String parameterName;
	
	@Element(name="iterator", required=false)
	private String iteratorCls = "kreatures.core.operators.StepIteratorStrategy";
	
	public double getMin() {
		return minValue;
	}
	
	public double getMax() {
		return maxValue;
	}
	
	public String getOperatorCls() {
		return operatorCls;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	
	public String getIteratorCls() {
		return iteratorCls;
	}
}
