package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Simply adds the new rule to the belief base, can make it inconsistent and
 * so on.
 * @author Tim Janus
 */
public class AspExpansion extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspExpansion.class);
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		LOG.info("Perform ASPExpansion as change.");
		AspBeliefbase abb = (AspBeliefbase)param.getBeliefBase();
		Program p = abb.getProgram();
		AspBeliefbase newK = (AspBeliefbase)param.getNewKnowledge();
		p.add(newK.getProgram());
			
		return param.getBeliefBase();
	}

}
