package angerona.fw.logic.conditional;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Revision operator for conditional belief bases. Defined as
 * <k,R> ° {A} = <k, R + {A}>, if k(con(R) ^ A) != INFINITY
 *                = <k, R> else
 *                
 * @author Sebastian Homann, Pia Wierzoch
 */

public class ConditionalRevision extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

}
