package angerona.fw.logic.base;

import angerona.fw.operators.Operator;

/**
 * Base class for all expansion operations on belief bases.
 * An expansion extends the knowledge of the belief base but
 * does not checks consistency.
 * @author Tim Janus
 */
public abstract class BaseExpansion extends Operator<BeliefUpdateParameter, BaseBeliefbase> {

	/**
	 * @return the class definition of the belief base this expansion operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
