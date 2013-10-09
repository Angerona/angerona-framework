package com.github.angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.SpeechAct;

/**
 * This translator implements a translate function as described by Krümpelmann
 * in mates13.
 * @author Tim Janus
 */
public class MatesTranslate extends AspTranslator {

	@Override
	protected AspBeliefbase translatePerceptionInt(BaseBeliefbase caller,
			Perception p) {
		// the meta inferences of Krümpelmann only work on speech-acts ...
		if(p instanceof SpeechAct) {
			
			String type = p.getClass().getSimpleName();
			
			// TODO: represent agents, etc. pp.
			
			
			AspBeliefbase reval = super.translatePerceptionInt(caller, p);
			Program prog = reval.getProgram();
			
			
			return reval;
		} else {
			// ... so that we use the default translate operator if the perception is
			// no speech act.
			return super.translatePerceptionInt(caller, p);
		}
	}
}
