package com.github.kreaturesfw.asp.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.lp.asp.syntax.Aggregate;
import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPElement;
import net.sf.tweety.lp.asp.syntax.DLPNeg;
import net.sf.tweety.lp.asp.syntax.Rule;
import net.sf.tweety.lp.asp.syntax.SymbolicSet;

import com.github.kreaturesfw.asp.component.AspMetaKnowledge;
import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.comm.SpeechAct;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.UpdateBeliefsOperator;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;

/**
 * This class implements the AUX program described by Krümpelmann in MATES13 by successive adding the rules for 
 * atoms to the different belief bases, therefore it uses a map that maps belief bases to their set of atoms that 
 * are handled by the AUX sub-program.
 * 
 * This operator handles the 'real' information of the speech-acts in the same way as {@link UpdateBeliefsOperator}.
 * 
 * @author Tim Janus
 */
public class MatesUpdateBeliefs extends UpdateBeliefsOperator {

	
	@Override
	protected Beliefs processImpl(EvaluateParameter param) {		
		// variables stores output for the report system:
		String outputWorld = "Only add the meta-information to the world knowledge";
		Map<String, String> outputView = new HashMap<>();
		for(String agentName : param.getAgent().getBeliefs().getViewKnowledge().keySet()) {
			outputView.put(agentName, "Only add the meta-information to the view on '" + agentName +"'");
		}

		// store old beliefs:
		Beliefs working = param.getBeliefs();
		Beliefs oldBeliefs = working.clone();
		
		// update the beliefs with the real-information of the speech-act
		if(param.getAtom() instanceof SpeechAct) {
			SpeechAct sa = (SpeechAct)param.getAtom();
			
			BaseBeliefbase world = working.getWorldKnowledge();
			world.addKnowledge(sa);
			outputWorld = "Add the information of '"+sa.toString()+"' to the world knowledge";
			
			boolean isReceiver = sa.getReceiverId().equals(param.getAgent().getName());
			String otherAgent = isReceiver ? sa.getSenderId() : sa.getReceiverId();
			BaseBeliefbase view = working.getViewKnowledge().get(otherAgent);
			view.addKnowledge(sa);
			outputView.put(otherAgent, "Add the information of '"+sa.toString()+"' to the view on '"+otherAgent+"'");
		}
		
		
		
		// Retrieve the meta knowledge of the agent
		AspMetaKnowledge metaKnowledge = param.getAgent().getComponent(AspMetaKnowledge.class);
		if(metaKnowledge != null) {
			// synchronize the AUX program using the world belief base of the agent
			if(working.getWorldKnowledge() instanceof AspBeliefbase) {
				synchonizeAux((AspBeliefbase) working.getWorldKnowledge(), metaKnowledge);
			}
			
			// synchronize the AUX program using the views of the agent
			for(BaseBeliefbase view : working.getViewKnowledge().values()) {
				if(view instanceof AspBeliefbase) {
					synchonizeAux((AspBeliefbase)view, metaKnowledge);
				}
			}
		}
		
		// after updating the meta knowledge generate the report outputs:
		param.report(outputWorld, working.getWorldKnowledge());
		for(Entry<String, String> entry : outputView.entrySet()) {
			BaseBeliefbase view = working.getViewKnowledge().get(entry.getKey());
			param.report(entry.getValue(), view);
		}
		
		if(working.getCopyDepth() == 0) {
			Perception p = (param.getAtom() instanceof Perception ? (Perception)param.getAtom() : null);
			param.getAgent().onUpdateBeliefs(p, oldBeliefs);
		}
		
		return working;
	}
	
	private void synchonizeAux(AspBeliefbase beliefBase, AspMetaKnowledge metaKnowledge) {
		// generate time facts / rules:
		Rule atRule = new Rule();
		atRule.setConclusion(new DLPAtom("mi_at", new Variable("T")));
		
		Set<Variable> vars = new HashSet<>();
		vars.add(new Variable("S"));
		Set<DLPElement> literals = new HashSet<>();
		literals.add(new DLPAtom("mi_time", new Variable("S")));
		SymbolicSet ss = new SymbolicSet(vars, literals);
		atRule.addPremise(new Aggregate("#max", ss, "=", new Variable("T")));
		atRule.addPremise(new DLPAtom("mi_time", new Variable("T")));
		
		beliefBase.getProgram().add(atRule);
		beliefBase.getProgram().addFact(new DLPAtom("mi_time", new NumberTerm(metaKnowledge.getTick())));
		
		// find every atom in belief base:
		Set<DLPAtom> atoms = new HashSet<>();
		for(Rule r : beliefBase.getProgram()) {
			atoms.addAll(r.getAtoms());
		}
		
		
		// for every atom
		for(DLPAtom atom : atoms) {
			if(atom.getName().startsWith("mi_")) {
				continue;
			}
			
			Constant posSymbol = metaKnowledge.matesPosConst(atom);
			Constant negSymbol = metaKnowledge.matesNegConst(atom);
			
			// generate the holds rules if the atom is either negative or positive
			
			Rule toAdd = new Rule();
			toAdd.setConclusion(atom);
			toAdd.addPremise(new DLPAtom("mi_holds", posSymbol));
			beliefBase.getProgram().add(toAdd);
			
			toAdd = new Rule();
			toAdd.setConclusion(new DLPNeg(atom));
			toAdd.addPremise(new DLPAtom("mi_holds", negSymbol));
			beliefBase.getProgram().add(toAdd);
			
			
			// generate the related facts:
			beliefBase.getProgram().addFact(new DLPAtom("mi_related", negSymbol,
					posSymbol));
			
			Rule relatedsync = new Rule();
			relatedsync.addPremise(new DLPAtom("mi_related", new Variable("X"), new Variable("Y")));
			relatedsync.setConclusion(new DLPAtom("mi_related", new Variable("Y"), new Variable("X")));
			beliefBase.getProgram().add(relatedsync);
			
		}
		
	}
}
