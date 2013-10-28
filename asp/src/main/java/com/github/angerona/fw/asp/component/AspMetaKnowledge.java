package com.github.angerona.fw.asp.component;

import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.translate.aspfol.AspFolTranslator;

import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.internal.IdGenerator;
import com.github.angerona.fw.listener.SimulationAdapter;
import com.github.angerona.fw.listener.SimulationListener;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.asp.AspBeliefbase;

/**
 * Represents additional information that are needed to provide the meta-knowledge
 * that is introduced by Krümpelmann in MATES13.
 * 
 * This container contains a map for atoms to their constant representation.
 * 
 * @author Tim Janus
 */
public class AspMetaKnowledge extends BaseAgentComponent {

	private static class ConstantTriple {
		Constant posConst;
		Constant negConst;
		Constant varConst;
	}
	
	private SimulationListener simListener = new SimulationAdapter() {
		@Override
		public void tickDone(AngeronaEnvironment simulationEnvironment) {
			tick += 1;
		}
	};
	
	private int tick = 0;
	
	private Map<DLPAtom, ConstantTriple> constantsMap = new HashMap<>();
	
	private IdGenerator idGen = new IdGenerator();
	
	public AspMetaKnowledge() {}
	
	public AspMetaKnowledge(AspMetaKnowledge other) {
		super(other);
	}
	
	@Override
	public void init(Map<String, String> parameters) {
		Angerona.getInstance().addSimulationListener(simListener);
	}
	
	public int getTick() {
		return tick;
	}
	
	public Constant matesInfer(DLPAtom atom, AspBeliefbase source) {
		ConstantTriple triple = getOrCreateTriple(atom);
		AspFolTranslator translator = new AspFolTranslator();
		AngeronaAnswer aa = source.reason(translator.toFOL(atom));
		if(aa.getAnswerValue() == AnswerValue.AV_TRUE) {
			return triple.posConst;
		} else if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
			return triple.negConst;
		} else {
			return triple.varConst;
		}
	}
	
	public Constant matesPosConst(DLPAtom atom) {
		return getOrCreateTriple(atom).posConst;
	}
	
	public Constant matesNegConst(DLPAtom atom) {
		return getOrCreateTriple(atom).negConst;
	}
	
	public Constant matesVar(DLPAtom atom) {
		return getOrCreateTriple(atom).varConst;
	}
	
	private ConstantTriple getOrCreateTriple(DLPAtom atom) {
		ConstantTriple reval = constantsMap.get(atom);
		if(reval == null) {
			reval = new ConstantTriple();
			long id = idGen.getNextId();
			reval.posConst = new Constant("pa_"+id);
			reval.negConst = new Constant("na_"+id);
			reval.varConst = new Constant("va_"+id);
			constantsMap.put(atom, reval);
		}
		return reval;
	}
	
	
	@Override
	public AspMetaKnowledge clone() {
		return new AspMetaKnowledge(this);
	}

}
