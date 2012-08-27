package angerona.fw.knowhow;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.comm.SpeechAct;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ViolatesResult;
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
	
	/** reference to the knowhow-strategy which was used at last */
	private KnowhowStrategy lastUsedStrategy;
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter param) {
		report("Using Knowhow for Subgoal Generation.");
		
		boolean gen  = false;
		for (Desire des : getOwner().getDesires().getDesires()) {
			// scenario specific tests:
			boolean revReq = des.getAtom().getPredicate().getName().equals("attend_scm");
			if(revReq) {
				Subgoal sg = runKnowhow("attend_scm(v_self)", param, des);
				if(sg != null) {
					gen = true;
					param.getActualPlan().addPlan(sg);
				}
			}	

			gen = gen || revisionRequest(des, param, param.getActualPlan().getAgent());
			gen = gen || answerQuery(des, param, param.getActualPlan().getAgent());
		}
		
		lastUsedStrategy = null;
		return gen;
	}
	
	/** Adapt the default behavior for strike-committee meeting. */
	@Override
	protected Boolean revisionRequest(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		if(! (des.getPerception() instanceof RevisionRequest))
			return false;
		
		// test if the revision request is excused, if yes then run the knowhow not_sure
		RevisionRequest rr = (RevisionRequest) des.getPerception();
		if(rr.getSentences().size() == 1) {
			FolFormula ff = rr.getSentences().iterator().next();
			if(	ff instanceof Atom && 
				((Atom)ff).getPredicate().getName().equalsIgnoreCase("excused")) {
				Subgoal sg = runKnowhow("not_sure(attend_scm)", pp, des);
				pp.getActualPlan().addPlan(sg);
				return true;
			}
		}
		
		return false;
	}

	/** adapt the default behavior for strike-committee meeting. */
	@Override 
	protected Boolean answerQuery(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		if(!(des.getPerception() instanceof Query))
			return false;
		
		// run the answer_query knowhow-statement till no more answers are found or an answer
		// was found which does not violates secrecy.
		Subgoal sg = runKnowhow("answer_query", pp, des);
		while(sg != null) {
			ViolatesResult res = ag.performThought(ag.getBeliefs(), 
					sg.peekStack(0));
			if(res.isAlright()) {
				pp.getActualPlan().addPlan(sg);
				return true;
			}
			
			sg = iterateKnowhow(pp, des, ag);
		}
		
		
		
		return false;
	}
	
	/**
	 * Helper method: Starts the knowhow processing:
	 * @param intention		start intention used in the intention-tree
	 * @param param			the subgoal-generation parameter data-structure
	 * @param des			The associated desire.
	 * @return				A subgoal containing one atomic-action.
	 */
	private Subgoal runKnowhow(String intention, SubgoalGenerationParameter param, Desire des) {
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
		lastUsedStrategy = new KnowhowStrategy(solverpath);
		lastUsedStrategy.init(kb, intention, actions, worldKB);
		
		return iterateKnowhow(param, des, ag);
	}

	/**
	 * Tries to re-iterate over the knowhow to find more actions
	 * @param param	The subgoal-genaration data-structure
	 * @param des	The associated desire
	 * @param ag	reference to the agent
	 * @return
	 */
	private Subgoal iterateKnowhow(SubgoalGenerationParameter param, Desire des, Agent ag) {
		// iterate knowhow algorithm until a action is found.
		int reval = 0;
		boolean calcKH = true;
		while(calcKH) {
			try {
				reval = lastUsedStrategy.performStep();
			} catch (SolverException e) {
				LOG.error("Knowhow-Base cannot use Solver: '{}'", e.getMessage());
				e.printStackTrace();
				return null;
			}
			
			if(reval != 0) {
				calcKH = false;
			}
		}
		
		// if no action was found return false
		if(reval == -1 || lastUsedStrategy.getActions().size() == 0) {
			report("Knowhow was not able to generate atomic action for: '"+lastUsedStrategy.getInitialIntention()+"'");
			return null;
		}
		
		// otherwise update Angerona plan component
		return createAtomicAction(param, des, ag);
	}

	/**
	 * This method creates a Subgoal containing an atomic action by mapping the action found by the knowhow
	 * into the Angerona System.
	 * @param param		Subgoal-Generation parameter data-structure
	 * @param des		The associated desire.
	 * @param ag		Reference to the agent
	 * @return			A subgoal containing an atomic action (Skill)
	 */
	private Subgoal createAtomicAction(SubgoalGenerationParameter param, Desire des,
			Agent ag) {
		Subgoal reval = null;
		// iterate over all actions found by the Knowhow (at the moment this is only one)
		for(Pair<String, HashMap<Integer, String>> action : lastUsedStrategy.getActions()) {
			report("Knowhow generates Action: " + action.toString());
			
			// test if the skill exists
			String skillName = action.first.substring(2);
			Skill skill = ag.getSkill(skillName);
			if(skill == null) {
				LOG.warn("Knowhow found Skill '{}' but the Agent '{}' does not support the Skill.", skillName, ag.getName());
				continue;
			}
			
			// create the action using the SkillParameter map and the name of the skill
			SpeechAct act = null;
			if(skillName.equals("RevisionRequest")) {
				act = createRevisionRequest(action.second);
			} else if(skillName.equals("Query")) {
				act = createQuery(action.second);
			} else if(skillName.equals("QueryAnswer")) {
				act = createQueryAnswer(action.second, (Query)des.getPerception());
			} else {
				LOG.error("The parameter mapping for Skill '{}' is not implemented yet.", skillName);
				continue;
			}
			
			// create the Subgoal which will be returned and report this step to Angerona.
			reval = new Subgoal(ag, des);
			reval.newStack(ag.getSkill(skillName), act);
			report("Knowhow finds new atomic action '"+skillName+"'");
		}
		return reval;
	}
	
	/**
	 * Helper method: Creates an instance of the RevisionRequest class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type RevisionReqeust which represents the Angerona version of the
	 * 						action found by the knowhow.
	 */
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
	
	/**
	 * Helper method: Creates an instance of the Query class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type Query which represents the Angerona version of the
	 * 						action found by the knowhow.
	 */
	protected Query createQuery(Map<Integer, String> paramMap) {
		String var = getVarWithPrefix(0, paramMap);
		Agent self = processVariable(var);
		
		var = getVarWithPrefix(1, paramMap);
		Agent receiver = processVariable(var);
		
		var = getVarWithPrefix(2, paramMap);
		FolFormula atom = processVariable(var);
		
		return new Query(self.getName(), receiver.getName(), atom);
	}
	
	/**
	 * Helper method: Creates an instance of the QueryAnswer class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type QueryAnswer which represents the Angerona version of the
	 * 						action found by the knowhow.
	 */
	protected Answer createQueryAnswer(Map<Integer, String> paramMap, Query reason) {
		String var = getVarWithPrefix(0, paramMap);
		boolean honest = var.equals("r_honest");
		AngeronaAnswer aa = getOwner().reason(reason.getQuestion());
		if(!honest) {
			// TODO: Move into lying operator... 
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				// TODO: make complex lie
			} else {
				AnswerValue av = aa.getAnswerValue();
				
				if(av == AnswerValue.AV_TRUE) {
					av = AnswerValue.AV_FALSE;
				} else if(av == AnswerValue.AV_FALSE) {
					av = AnswerValue.AV_TRUE;
				}
				
				aa = new AngeronaAnswer(aa.getKnowledgeBase(), aa.getQueryFOL(), av);
			}
		}
		
		report((honest ? "Honest answer " : "Lie ") + "for: '"+reason.getQuestion().toString()+"' = " + aa.getAnswerValue().toString());

		return new Answer(getOwner().getName(), reason.getSenderId(), reason.getQuestion(), aa);
	}
	
	/**
	 * Helper method: checks if the given index is in the parameter map, if this is not the case some error output
	 * is produced.
	 * @param index		index of the parameter
	 * @param paramMap	the parameter-map 
	 * @return			A String representing the value of the parameter.
	 */
	protected String getVarWithPrefix(int index, Map<Integer, String> paramMap) {
		String var = paramMap.get(index);
		if(var == null) {
			LOG.error("The mapping of the variables is wrong. We assume index 0-x are used to represent the x variables of the used Skill.");
		}
		return var;
	}
	
	/**
	 * Helper method: checks for prefixes and creates the correct objects from the given parameter/variable.
	 * @param variableWithPrefix	String containing the value
	 * @return	An object of generic Type. Might be an agent or an FOL-Atom or a string.
	 */
	protected <T> T processVariable(String variableWithPrefix) {
		if(variableWithPrefix.startsWith("v_")) {
			return this.getVariable(variableWithPrefix.substring(2));
		} else if(variableWithPrefix.startsWith("a_")) {
			return (T) this.getAgent(variableWithPrefix.substring(2));
		} else {
			Atom temp =  createAtom(variableWithPrefix);
			List<Term> terms = new LinkedList<>();
			for(Term t : temp.getArguments()) {
				if(t.getName().charAt(1) == '_') {
					Agent newName = processVariable(t.getName());
					terms.add(new Constant(newName.getName()));
				} else {
					terms.add(t);
				}
			}
			return (T) new Atom(temp.getPredicate(), terms);
		}
	}
	
	/**
	 * Helper method: Creates a FOL-Atom using the given string
	 * @param formula	String which will be parsed to a FolFormula (Atom).
	 * @return			The FOL-Atom of the given String
	 */
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
	
	/**
	 * Gets a variable of the given name... actually there is only self
	 * but a TODO is to use the Context system to give the knowhow more
	 * access to the Angerona internal systems.
	 * @param name		The name of the variable
	 * @return			An Object representing the variable.
	 */
	private <T> T getVariable(String name) {
		if(name.equalsIgnoreCase("self")) {
			return (T) Agent.class.cast(getOwner());
		} else {
			LOG.error("Cannot convert variable with name '{}'.", name);
			return null;
		}
	}
	
	/**
	 * Helper method: Searchs for the agent with the given name.
	 * @param name		The agent's name
	 * @return			Reference to the Agent with the given name or null if
	 * 					no such Agent exists.
	 */
	private Agent getAgent(String name) {
		Agent reval = this.getOwner().getEnvironment().getAgentByName(name);
		if(reval == null) {
			LOG.warn("Knowhow tries to find Agent with name '{}' but its not part of the Enviornment", name);
		}
		return reval;
	}
}