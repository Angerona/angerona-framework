package com.github.kreatures.core.logic;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.operators.Operator;
import com.github.kreatures.core.operators.parameter.ChangeBeliefbaseParameter;
import com.github.kreatures.core.util.Pair;

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
public abstract class BaseChangeBeliefs 
	extends Operator<BaseBeliefbase, ChangeBeliefbaseParameter, BaseBeliefbase> {
	
	public static final String OPERATION_TYPE = "ChangeBeliefs";
	
	@Override
	protected abstract BaseBeliefbase processImpl(ChangeBeliefbaseParameter in);
	
	@Override
	protected ChangeBeliefbaseParameter getEmptyParameter() {
		return new ChangeBeliefbaseParameter();
	}

	@Override
	protected BaseBeliefbase defaultReturnValue() {
		return null;
	}
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		Pair<String, Class<?>> reval = new Pair<>();
		reval.first = OPERATION_TYPE;
		reval.second = BaseChangeBeliefs.class;
		return reval;
	}
	
	/**
	 * @return the class definition of the belief base this revision operation supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();

}
