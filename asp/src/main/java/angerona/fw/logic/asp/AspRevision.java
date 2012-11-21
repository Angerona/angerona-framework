package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.revision.PreferenceHandling;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Angerona;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;
import angerona.fw.serialize.GlobalConfiguration;

/**
 * ASP Revision using the preference handling concept
 * @author Tim Janus
 */
public class AspRevision extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspRevision.class);
	
	/** wrapper for the used ASP solver */
	private SolverWrapper wrapper;
	
	public AspRevision() {
		GlobalConfiguration config = Angerona.getInstance().getConfig();
		String solverStr = config.getParameters().get("asp-solver");
		if(solverStr != null)
			this.wrapper = SolverWrapper.valueOf(solverStr);
		else
			this.wrapper = SolverWrapper.DLV;
	}
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		LOG.info("Perform ASPRevison as change.");
		PreferenceHandling pf = new PreferenceHandling();
		if(! (param.getBeliefBase() instanceof AspBeliefbase))
			throw new RuntimeException("Error: Beliefbase must be of type asp");
		AspBeliefbase bb = (AspBeliefbase)param.getBeliefBase();
		
		if(! (param.getNewKnowledge() instanceof AspBeliefbase))  
			throw new RuntimeException("Error: Beliefbase must be of type asp.");
		
		AspBeliefbase newK = (AspBeliefbase)param.getNewKnowledge();
		
		try {			
			bb.setProgram(pf.revision(bb.getProgram(), newK.getProgram(), 
					wrapper.getSolver()));
		} catch (SolverException e) {
			e.printStackTrace();
		} 
		
		return bb;
	}

}
