package angerona.fw.knowhow;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.comm.SpeechAct;
import angerona.fw.operators.def.SubgoalGenerationOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.util.Pair;

/**
 * Subgoal Generation using Knowhow as basic.
 * @author Tim Janus
 */
public class KnowhowSubgoal extends SubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowSubgoal.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter param) {
		report("Using Knowhow for Subgoal Generation.");
		
		boolean gen  = false;
		for (Desire des : getOwner().getDesires().getDesires()) {
			// scenario specific tests:
			boolean revReq = des.getAtom().getPredicate().getName().equals("attend_scm");
			if(revReq) {
				Subgoal sg = new Subgoal(param.getActualPlan().getAgent(), des);
				gen = gen || runKnowhow("attend_scm(v_self)", param, sg);
			}	

			gen = gen || revisionRequest(des, param, param.getActualPlan().getAgent());
		}
		
		
		return gen;
	}
	
	// Adapt the default behavior for strike-committee meeting.
	@Override
	protected Boolean revisionRequest(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		if(! (des.getPerception() instanceof RevisionRequest))
			return false;
		
		RevisionRequest rr = (RevisionRequest) des.getPerception();
		if(rr.getSentences().size() == 1) {
			FolFormula ff = rr.getSentences().iterator().next();
			if(	ff instanceof Atom && 
				((Atom)ff).getPredicate().getName().equalsIgnoreCase("excused")) {
				Subgoal sg = new Subgoal(ag, des);
				return runKnowhow("not_sure(attend_scm)", pp, sg);
			}
		}
		
		return false;
	}

	private boolean runKnowhow(String intention, SubgoalGenerationParameter param, Subgoal sg) {
		
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
		KnowhowStrategy ks = new KnowhowStrategy(solverpath);
		Set<Pair<String, Integer>> realActions = new HashSet<>();
		for(String action : actions) {
			realActions.add(new Pair<>("s_"+action, 0));
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
		for(Pair<String, HashMap<Integer, String>> action : ks.getActions()) {
			report("Knowhow generates Action: " + action.toString());
			
			String skillName = action.first.substring(2);
			Skill skill = ag.getSkill(skillName);
			if(skill == null) {
				LOG.warn("Knowhow found Skill '{}' but the Agent '{}' does not support the Skill.", skillName, ag.getName());
				continue;
			}
			
			SpeechAct act = null;
			if(skillName.equals("RevisionRequest")) {
				act = createRevisionRequest(action.second);
			} else if(skillName.equals("Query")) {
				act = createQuery(action.second);
			} else {
				LOG.error("The parameter mapping for Skill '{}' is not implemented yet.", skillName);
				continue;
			}
			sg.newStack(ag.getSkill(skillName), act);
			
			gen = true;
			param.getActualPlan().addPlan(sg);
			report("Add the new atomic action '"+skillName+"' to the plan using knowhow.", ag.getPlanComponent());
		}
		return gen;
	}
	
	protected RevisionRequest createRevisionRequest(Map<Integer, String> paramMap) {
		if(paramMap.size() != 3) {
			LOG.error("Knowhow found Skill '{}' but there are '{}' parameters instead of 3", "RevisionRequest", paramMap.size());
			return null;
		}
		
		String var = getVarWithPrefix(0, paramMap);
		Agent self = processVariable(var);
		
		var = getVarWithPrefix(1, paramMap);
		Agent receiver = processVariable(var);
		
		var = getVarWithPrefix(2, paramMap);
		FolFormula atom = processVariable(var);
		
		return new RevisionRequest(self.getName(), receiver.getName(), atom);
	}
	
	protected Query createQuery(Map<Integer, String> paramMap) {
		String var = getVarWithPrefix(0, paramMap);
		Agent self = processVariable(var);
		
		var = getVarWithPrefix(1, paramMap);
		Agent receiver = processVariable(var);
		
		var = getVarWithPrefix(2, paramMap);
		FolFormula atom = processVariable(var);
		
		return new Query(self.getName(), receiver.getName(), atom);
	}
	
	protected String getVarWithPrefix(int index, Map<Integer, String> paramMap) {
		String var = paramMap.get(index);
		if(var == null) {
			LOG.error("The mapping of the variables is wrong. We assume index 0-x are used to represent the x variables of the used Skill.");
		}
		return var;
	}
	
	protected <T> T processVariable(String variableWithPrefix) {
		if(variableWithPrefix.startsWith("v_")) {
			return this.getVariable(variableWithPrefix.substring(2));
		} else if(variableWithPrefix.startsWith("a_")) {
			return (T) this.getAgent(variableWithPrefix.substring(2));
		} else {
			Atom temp =  createAtom(variableWithPrefix);
			List<Term> terms = new LinkedList<>();
			for(Term t : temp.getArguments()) {
				if(t.getName().startsWith("v_")) {
					Agent newName = processVariable(t.getName());
					terms.add(new Constant(newName.getName()));
				} else {
					terms.add(t);
				}
			}
			return (T) new Atom(temp.getPredicate(), terms);
		}
	}
	
	private Atom createAtom(String formula) {
		FolParserB parser = new FolParserB(new StringReader(formula));
		try {
			Atom reval = parser.atom(new FolSignature());
			return reval;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private <T> T getVariable(String name) {
		if(name.equalsIgnoreCase("self")) {
			return (T) Agent.class.cast(getOwner());
		} else {
			LOG.error("Cannot convert variable with name '{}'.", name);
			return null;
		}
	}
	
	private Agent getAgent(String name) {
		Agent reval = this.getOwner().getEnvironment().getAgentByName(name);
		if(reval == null) {
			LOG.warn("Knowhow tries to find Agent with name '{}' but its not part of the Enviornment", name);
		}
		return reval;
	}

}
