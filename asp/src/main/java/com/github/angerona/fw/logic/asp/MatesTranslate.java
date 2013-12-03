package com.github.angerona.fw.logic.asp;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.translators.aspfol.AspFolTranslator;
import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.syntax.DLPNeg;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.asp.component.AspMetaKnowledge;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.comm.SpeechAct.SpeechActType;

/**
 * This translate operation implements an extended translate functionality as described by Krümpelmann
 * in mates13. The translation operator also encodes meta-information about the speech-act. 
 * 
 * @author Tim Janus
 */
public class MatesTranslate extends AspTranslator {
	@Override
	protected AspBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		// the meta inferences of Krümpelmann only works on speech-acts ...
		if(! (p instanceof SpeechAct))
			return super.translatePerceptionInt(caller, p);
		
		SpeechAct speechAct = (SpeechAct)p;
		AspBeliefbase reval = new AspBeliefbase();
		
		AspMetaKnowledge metaKnowledge = caller.getAgent().getComponent(AspMetaKnowledge.class);
		AspFolTranslator translator = new AspFolTranslator();
		for(FolFormula info : speechAct.getContent()) {
			DLPLiteral knowledge = (DLPLiteral)translator.toASP(info);
			
			String type = "t_"+speechAct.getClass().getSimpleName();
			String sender = speechAct.getSenderId();
			
			Constant senderConstant = new Constant("a_"+sender);
			
			Constant constantSymbolForL = null;
			if(speechAct.getType() == SpeechActType.SAT_REQUESTING) {
				constantSymbolForL = metaKnowledge.matesVar(knowledge.getAtom());
			} else {
				if(knowledge instanceof DLPAtom) {
					constantSymbolForL = metaKnowledge.matesPosConst(knowledge.getAtom());
				} else if(knowledge instanceof DLPNeg) {
					constantSymbolForL = metaKnowledge.matesNegConst(knowledge.getAtom());
				} else {
					throw new IllegalStateException();
				}
				
				boolean isSender = caller.getAgent().getName().equals(speechAct.getSenderId());
				boolean isView = caller.getAgent().getBeliefs().getWorldKnowledge().getGUID() != caller.getGUID();
				if(!isSender || (isSender && isView)) {
					reval = super.translatePerceptionInt(caller, speechAct);
				}
			}
			
			// todo: get cur step (but in a way that simulating and real performs work)
			NumberTerm curStep = new NumberTerm(metaKnowledge.getTick());
						
			DLPAtom atom = new DLPAtom("mi_sact", new Constant(type), senderConstant, constantSymbolForL, curStep);
			reval.getProgram().addFact(atom);
		}
		
		return reval;
	}
}
