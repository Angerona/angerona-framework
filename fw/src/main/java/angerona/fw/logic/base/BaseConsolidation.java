package angerona.fw.logic.base;

import angerona.fw.operators.Operator;

/**
 * Base class for all Consolidation operations performed on a
 * belief base.
 * A consolidation restores the consistency of the belief base.
 * @author Tim Janus
 */
public abstract class BaseConsolidation extends Operator<BaseBeliefbase, BaseBeliefbase> {
	
	/**
	 * @return the class definition of the belief base this consolidation operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
