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
import angerona.fw.logic.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Aps Revision using 
 * @author Tim Janus
 */
public class AspRevision extends BaseChangeBeliefs {

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
			bb.setProgram(pf.revision(bb.getProgram(), newInfo, new DLVComplex("tools/solver/asp/dlv/dlv-complex")));
		} catch (SolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bb;
	}

}
