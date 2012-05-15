package angerona.fw.operators;

import java.util.Set;

import angerona.fw.Desire;
import angerona.fw.operators.parameter.GenerateOptionsParameter;

/**
 * Base class for option generation operators.
 * It creates a set of formulas which represent the new agent desires.
 * @author Tim Janus
 */
public abstract class BaseGenerateOptionsOperator extends 
	Operator<GenerateOptionsParameter, Set<Desire>> {
}
