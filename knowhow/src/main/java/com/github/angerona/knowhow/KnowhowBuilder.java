package com.github.angerona.knowhow;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.fw.util.Pair;
import com.github.angerona.knowhow.parameter.SkillParameter;

/**
 * Helper class responsible for translating the know-how base into other data-structures like
 * extended logical programs. Only static methods are used.
 * 
 * @author Tim Janus
 */
public class KnowhowBuilder {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowBuilder.class);
	
	/**
	 * Creates an extended Logic program from the given KnowhowBase.
	 * @todo Implement variables for Knowhow-Statement targets
	 * @param kb	reference to the knowhow base.
	 * @return		extended logic program representing the given KnowhowBase.
	 */
	public static Pair<Program, LinkedList<SkillParameter>> 
		buildKnowhowbaseProgram(KnowhowBase kb) {
		Program p = new Program();
		Pair<Program, LinkedList<SkillParameter>> reval 
			= new Pair<>(p, new LinkedList<SkillParameter>());
		
		List<KnowhowStatement> reconsider = new LinkedList<>();
		for(KnowhowStatement ks : kb.getStatements()) {
			boolean closed = true;
			for(int i=0; i<ks.getTarget().getArity(); ++i) {
				if(ks.getTarget().getTerm(i) instanceof Variable)
				{
					closed = false;
					break;
				}
			}

			if(!closed) {
				reconsider.add(ks);
				throw new NotImplementedException("variables are not supported by Knowhow yet");
			} else {
				reval.second.addAll(createFactsForKnowstatement(p, ks));
			}
		}
		
		/*
		boolean changed = false;
		for(KnowhowStatement vks : reconsider) {
			for(KnowhowStatement cks : kb.getStatements()) {
				
			}
		}
		*/
		
		return reval;
	}

	/**
	 * Helper method: Adds all facts of the given Knowhow statement to the given
	 * program and also creates a list of SkillParameters which has the parameters
	 * for Skills used as subtargets.
	 * @param p		The ELP program
	 * @param ks	The knowhow statement.
	 * @return		A list of SkillParameters containing the parameters needed subtargets
	 * 				which are Skills.
	 */
	private static List<SkillParameter> createFactsForKnowstatement(Program p, KnowhowStatement ks) {
		List<SkillParameter> params = new LinkedList<>();
		// Knowhow Statement
		Rule r = new Rule();
		r.setConclusion(new DLPAtom("khstatement", new Constant(ks.name), 
				new Constant(ks.getTarget().toString())));
		p.add(r);
		
		// Subtargets
		int i = 1;
		for(DLPAtom a : ks.getSubTargets()) {
			r = new Rule();
			
			// if the subgoal is an skill then we have to find the parameter mappings
			// and encode them in the logic program:
			if(a.toString().startsWith("s_")) {
				// search parameters:
				int c = 0;
				for(Term<?> t : a.getArguments()) {
					SkillParameter sp = new SkillParameter(
							ks.getId(), 				// statement id
							i, 							// subgoal index
							a.getName().substring(2),	// skill name
							c++,						// param index
							t); 						// param value
					params.add(sp);
				}
				a = new DLPAtom(a.getName());
			}
			r.setConclusion(new DLPAtom("khsubgoal", new Constant(ks.name), new NumberTerm(i), 
					new Constant(a.toString())));
			p.add(r);
			i++;
		}
		
		// Conditions
		for(DLPAtom a : ks.getConditions()) {
			r = new Rule();
			r.setConclusion(new DLPAtom("khcondition", new Constant(ks.name), 
					new Constant(a.toString())));
			p.add(r);
		}
		
		return params;
	}
	
	public static Program buildHoldsProgram(Collection<String> literals) {
		Program p = new Program();
		for(String atom : literals) {
			if(atom.startsWith("NEG_")) {
				p.addFact(new DLPAtom("nholds", new Constant(atom.substring(4))));
			} else {
				p.addFact(new DLPAtom("holds", new Constant(atom)));
			}
			
		}
		return p;
	}
	
	/**
	 * Creates an extended logic program from the given collection of skills
	 * @param atomic_actions	all skills which are atomic actions of the agent
	 * @return			An extended logic program containing all is_atomic facts for the agent
	 */
	public static Program buildAtomicProgram(Collection<String> atomic_actions) {
		Program p = new Program();
		
		for(String action : atomic_actions) {
			p.addFact(new DLPAtom("is_atomic", new Constant("s_"+action)));
		}
		
		return p;
	}
}
