package com.github.angerona.fw.logic.asp;

import java.util.HashSet;
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

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.asp.component.AspMetaKnowledge;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.UpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

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
	protected Beliefs processInternal(EvaluateParameter param) {		
		// update the beliefs with the real-information of the speech-act
		// Beliefs reval = super.processInternal(param); // this operator does not update all belief bases...
		Beliefs working = param.getBeliefs();
		if(param.getAtom() instanceof SpeechAct) {
			SpeechAct sa = (SpeechAct)param.getAtom();
			
			BaseBeliefbase world = working.getWorldKnowledge();
			world.addKnowledge(sa);
			param.report("Add the information of '"+sa.toString()+"' to the world knowledge", world);
			
			boolean isReceiver = sa.getReceiverId().equals(param.getAgent().getName());
			String otherAgent = isReceiver ? sa.getSenderId() : sa.getReceiverId();
			BaseBeliefbase view = working.getViewKnowledge().get(otherAgent);
			view.addKnowledge(sa);
			param.report("Add the information of '"+sa.toString()+"' to the view on '"+otherAgent+"'", view);
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
