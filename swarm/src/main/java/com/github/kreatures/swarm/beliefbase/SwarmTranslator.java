package com.github.kreatures.swarm.beliefbase;



import java.util.HashSet;
import java.util.Set;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.logic.asp.AspBeliefbase;
import com.github.kreatures.core.logic.asp.AspTranslator;

import com.github.kreatures.swarm.basic.SwarmPerception;

import net.sf.tweety.logics.fol.syntax.FolFormula;

public class SwarmTranslator extends AspTranslator{
	@Override
	protected AspBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception perception) {
		Set<FolFormula> formulas = new HashSet<FolFormula>();
		if (perception instanceof SwarmPerception) {
			//SwarmPerception swarmPerception = (SwarmPerception) perception;
		}
		// TODO Translator	
		return (AspBeliefbase) translateFOL(caller, formulas);
	}
}