package com.github.angerona.fw.logic.asp;

import java.util.Collection;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;
import com.github.angerona.fw.serialize.GlobalConfiguration;

/**
 * ASP Revision using the preference handling concept
 * @author Tim Janus
 */
public abstract class AspRevision extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspRevision.class);
	
	/** wrapper for the used ASP solver */
	protected SolverWrapper wrapper;
	
	public AspRevision() throws InstantiationException {
		GlobalConfiguration config = Angerona.getInstance().getConfig();
		if(!config.getParameters().containsKey("asp-solver")) {
			throw new InstantiationException("Configuration 'asp-solver' not set in configuration.xml");
		}
		String solverStr = config.getParameters().get("asp-solver");
		if(solverStr != null)
			this.wrapper = SolverWrapper.valueOf(solverStr);
		else
			this.wrapper = SolverWrapper.DLV;
		
		if(this.wrapper.getError() != null) {
			throw this.wrapper.getError();
		}
	}
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	protected abstract MultipleBaseRevisionOperator<Rule> createRevisionImpl();
	
	@Override
	protected BaseBeliefbase processInternal(
			ChangeBeliefbaseParameter param) {
		LOG.info("Perform ASPRevison as change.");
		if(! (param.getSourceBeliefBase() instanceof AspBeliefbase))
			throw new RuntimeException("Error: Beliefbase must be of type asp");
		AspBeliefbase bb = (AspBeliefbase)param.getSourceBeliefBase();
		
		if(! (param.getNewKnowledge() instanceof AspBeliefbase))  
			throw new RuntimeException("Error: Beliefbase must be of type asp.");
		
		AspBeliefbase newK = (AspBeliefbase)param.getNewKnowledge();
		
		MultipleBaseRevisionOperator<Rule> impl = createRevisionImpl();
		Collection<Rule> reval = impl.revise(bb.getProgram(), newK.getProgram());
		if(reval != null) {
			Program p = new Program();
			p.addAll(reval);
			bb.setProgram(p);
		} else {
			LOG.warn("Cannot revise:\n{}\nwith:\n{}", bb.getProgram().toString(), 
					newK.getProgram().toString());
		}
		return bb;
	}

}
