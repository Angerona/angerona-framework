package angerona.fw.logic.base;

import angerona.fw.operators.Operator;

/**
 * Base class for all revision operations on belief bases.
 * An revision extends the knowledge of a belief base and
 * guaranteed that it is consistent.
 * @author Tim Janus
 */
public abstract class BaseRevision extends Operator<BeliefUpdateParameter, BaseBeliefbase> {
	
	/**
	 * @return the class definition of the belief base this revision operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
