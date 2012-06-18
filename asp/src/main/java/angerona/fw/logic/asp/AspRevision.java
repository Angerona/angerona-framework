package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.revision.PreferenceHandling;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

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
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		PreferenceHandling pf = new PreferenceHandling();
		if(! (param.getBeliefBase() instanceof AspBeliefbase))
			throw new RuntimeException("Error: Beliefbase must be of type asp");
		AspBeliefbase bb = (AspBeliefbase)param.getBeliefBase();
		
		Program newInfo = new Program();
		for(FolFormula ff : param.getNewKnowledge()) {
			Rule r = new Rule();
			
			if(ff instanceof net.sf.tweety.logics.firstorderlogic.syntax.Atom) {
				net.sf.tweety.logics.firstorderlogic.syntax.Atom a = 
						(net.sf.tweety.logics.firstorderlogic.syntax.Atom)ff;
				LOG.info("ASDF Predicate name:"+a.getPredicate().getName());
				r.addHead(new Atom(a.getPredicate().getName()));
			} else if(ff instanceof Negation) {
				Negation n = (Negation)ff;
				Atom a = new Atom(n.getAtoms().iterator().next().getPredicate().getName());
				r.addHead(new Neg(a));
			} else {
				continue;
			}
			newInfo.add(r);
		}
		
		try {

			// TODO: Find better solution replace duplo (REASONER)
			String postfix = "";
			postfix += System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? ".exe" : "";
			postfix += System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0 ? ".bin" : "";
			// no postfix for unix.
			
			bb.setProgram(pf.revision(bb.getProgram(), newInfo, 
					new DLVComplex("tools/solver/asp/dlv/dlv-complex"+postfix)));
		} catch (SolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bb;
	}

}
