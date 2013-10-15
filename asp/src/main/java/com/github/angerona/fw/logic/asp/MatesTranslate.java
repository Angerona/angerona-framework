package com.github.angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.SpeechAct;

/**
 * This translate operation implements an extended translate functionality as described by Krümpelmann
 * in mates13. The translation operator also encodes meta-information about the speech-act. 
 * 
 * @author Tim Janus
 */
public class MatesTranslate extends AspTranslator {
	@Override
	protected AspBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		AspBeliefbase reval = super.translatePerceptionInt(caller, p);
		
		// the meta inferences of Krümpelmann only works on speech-acts ...
		if(p instanceof SpeechAct) {
			SpeechAct speechAct = (SpeechAct)p;
			
			String type = speechAct.getClass().getSimpleName();
			String sender = speechAct.getSenderId();
			
			Constant senderConstant = new Constant("a_"+sender);
			
			// todo: add mapping for constant symbols
			Constant constantSymbolForL = new Constant("l");
			// todo: get cur step (but in a way that simulating and real performs work)
			NumberTerm curStep = new NumberTerm(0);
						
			DLPAtom atom = new DLPAtom(type, senderConstant, constantSymbolForL, curStep);
			reval.getProgram().addFact(atom);
		}
		
		return reval;
	}
}
