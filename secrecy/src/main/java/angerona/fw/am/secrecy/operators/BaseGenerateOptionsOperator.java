package angerona.fw.am.secrecy.operators;

import angerona.fw.Agent;
import angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import angerona.fw.operators.Operator;
import angerona.fw.util.Pair;


/**
 * Base class for option generation operators.
 * It creates a set of formulas which represent the new agent desires.
 * @author Tim Janus
 */
public abstract class BaseGenerateOptionsOperator extends 
	Operator<Agent, GenerateOptionsParameter, Integer> {

	public static final String OPERATION_TYPE = "GenerateOptions";
	
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, 
				BaseGenerateOptionsOperator.class);
	}
	
	
	@Override
	protected GenerateOptionsParameter getEmptyParameter() {
		return new GenerateOptionsParameter();
	}


	@Override
	protected Integer defaultReturnValue() {
		return 0;
	}
}
