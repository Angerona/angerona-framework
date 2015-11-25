package com.github.kreatures.core.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This class represents a discrete belief operator family, that means it is an preference
 * relation on reasoner, that uses a discrete ordering. A set of operators can be added to the 
 * family, but the order must be clear, therefore the predecessor must be given everytime a
 * operator is added to the family.
 * 
 * @author Tim Janus
 */
public class DiscreteBeliefOperatorFamily implements BeliefOperatorFamily {
	
	/** this list represents the discrete order of the belief operators */
	private List<OperatorCallWrapper> operators = new LinkedList<>();
	
	@Override
	public OperatorCallWrapper getPredecessor(OperatorCallWrapper current) {
		int index = operators.indexOf(current) - 1;
		return (index < 0 || index >= operators.size()) ? null : operators.get(index);
	}

	@Override
	public OperatorCallWrapper getSuccessor(OperatorCallWrapper current) {
		int index = operators.indexOf(current) + 1;
		return (index < 0 || index >= operators.size()) ? null : operators.get(index);

	}

	@Override
	public boolean addOperator(OperatorCallWrapper toAdd,
			OperatorCallWrapper predecessor) {
		int index;
		
		if(predecessor == null) 
			operators.add(toAdd);
		else if((index = operators.indexOf(predecessor)) != -1)
			operators.add(index, toAdd);
		else
			return false;
		return true;
	}

	@Override
	public double distance(OperatorCallWrapper from, OperatorCallWrapper to) {
		return Math.abs(operators.indexOf(from) - operators.indexOf(to));
	}

	@Override
	public OperatorCallWrapper getOperator(String operatorCls,
			Map<String, String> settings) {
		for(OperatorCallWrapper operator : operators) {
			if(	operator.getClass().getName().equals(operatorCls) &&
				operator.getSettings().equals(settings)	) {
				return operator;
			}
		}
		return null;
	}
}
