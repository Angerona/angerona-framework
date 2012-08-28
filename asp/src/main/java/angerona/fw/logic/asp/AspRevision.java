package angerona.fw.logic.asp;

import java.io.FileNotFoundException;

import net.sf.tweety.logicprogramming.asplibrary.revision.PreferenceHandling;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Aps Revision using 
 * @author Tim Janus
 */
public class AspRevision extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspRevision.class);
	
	private SolverWrapper wrapper = SolverWrapper.DLV_COMPLEX;
	
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
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		
		return bb;
	}

}
