package angerona.fw.knowhow;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Subgoal;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;

/**
 * Subgoal Generation using Knowhow as basic.
 * @author Tim Janus
 */
public class KnowhowSubgoal extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowSubgoal.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter param) {
		report("Using Knowhow for Subgoal Generation.");
		// TODO: Move path to config
		String solverpath = "D:/wichtig/Hiwi/workspace/a5/software/test/src/main/tools/win/solver/asp/dlv/dlv-complex.exe";
		Agent ag = param.getActualPlan().getAgent();
		
		// Gathering knowhow information
		Collection<String> worldKB = ag.getBeliefs().getWorldKnowledge().getAtoms();
		Collection<String> actions = ag.getSkills().keySet();
		KnowhowBase kb = (KnowhowBase)ag.getComponent(KnowhowBase.class);
		if(kb == null) {
			throw new RuntimeException("Agent '"+ag.getName()+"' has no KnowhowBase but tries to using '"+this.getClass().getSimpleName()+"'.");
		}
		
		// create and initialize the knowhow strategy
		String intention = "attend_scm";
		KnowhowStrategy ks = new KnowhowStrategy(solverpath);
		Set<String> realActions = new HashSet<>();
		for(String action : actions) {
			realActions.add(action.toLowerCase());
		}
		ks.init(kb, intention, realActions, worldKB);
		
		// iterate knowhow algorithm until a action is found.
		int reval = 0;
		boolean calcKH = true;
		while(calcKH) {
			try {
				reval = ks.performStep();
			} catch (SolverException e) {
				LOG.error("Knowhow-Base cannot use Solver: '{}'", e.getMessage());
				e.printStackTrace();
				return false;
			}
			
			if(reval != 0) {
				calcKH = false;
			}
		}
		
		// if no action was found return false
		if(reval == -1 || ks.getActions().size() == 0) {
			report("Knowhow was not able to generate atomic action for: '"+intention+"'");
			return false;
		}
		
		// otherwise update Angerona plan component
		boolean gen = false;
		for(String action : ks.getActions()) {
			report("Knowhow generates Action: " + action);
			Subgoal sg = new Subgoal(ag, null);
			for(String skill : ag.getSkills().keySet()) {
				if(skill.equalsIgnoreCase(action)) {
					List<Term> terms = new LinkedList<>();
					// TODO: Encode this somehow in the knowhow:
					terms.add(new Constant("employee"));
					sg.newStack(ag.getSkill(skill), new RevisionRequest(ag.getName(), "Boss", 
							new Atom(new Predicate("excused", 1), terms)));
					gen = true;
					break;
				}
			}
			param.getActualPlan().addPlan(sg);
		}
		
		return gen;
	}

}
