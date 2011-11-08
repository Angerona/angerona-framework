package angerona.fw.operators;

import java.util.List;

import angerona.fw.Intention;
import angerona.fw.operators.parameter.GenerateOptionsParameter;

/**
 * Base class for option generation operators.
 * @author Tim Janus
 */
public abstract class BaseGenerateOptionsOperator extends 
	Operator<GenerateOptionsParameter, List<Intention>> {
}
