package angerona.fw.logic.base;

import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.BeliefbaseParameter;

/**
 * Base class for all Consolidation operations performed on a
 * belief base.
 * A consolidation restores the consistency of the belief base.
 * @author Tim Janus
 */
public abstract class BaseConsolidation extends Operator<BeliefbaseParameter, BaseBeliefbase> {
	
	/**
	 * @return the class definition of the belief base this consolidation operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
