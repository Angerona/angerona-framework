package angerona.fw.logic;

import angerona.fw.BaseBeliefbase;
import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Base class for all change operations on belief bases.
 * An revision extends the knowledge of a belief base and
 * guaranteed that it is consistent. An expansion extends the
 * knowledge of the belief base but does not proofs if the result
 * is inconsistent. Both types of operations might be implemented
 * by subclasses.
 * 
 * A subclass has to implement the processInt method of the Operator base class
 * 
 * @see Operator
 * 
 * @author Tim Janus
 */
public abstract class BaseChangeBeliefs extends Operator<BeliefUpdateParameter, BaseBeliefbase> {
	
	/**
	 * @return the class definition of the belief base this revision operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
