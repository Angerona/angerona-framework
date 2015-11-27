package com.github.kreaturesfw.asp.component;

import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.translators.aspfol.AspFolTranslator;
import net.sf.tweety.lp.asp.syntax.DLPAtom;

import com.github.kreaturesfw.asp.logic.AspBeliefbase;
import com.github.kreaturesfw.core.KReatures;
import com.github.kreaturesfw.core.KReaturesEnvironment;
import com.github.kreaturesfw.core.basic.BaseAgentComponent;
import com.github.kreaturesfw.core.internal.IdGenerator;
import com.github.kreaturesfw.core.listener.SimulationAdapter;
import com.github.kreaturesfw.core.listener.SimulationListener;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;

/**
 * Represents additional information that are needed to provide the meta-knowledge
 * that is introduced by Krümpelmann in MATES13.
 * 
 * This container contains a map for atoms to their constant representation.
 * 
 * @author Tim Janus
 */
public class AspMetaKnowledge extends BaseAgentComponent {

	public static class ConstantTriple {
		public Constant posConst;
		public Constant negConst;
		public Constant varConst;
	}
	
	/** 
	 * A handler of the simulation listener that is responsible to increment
	 * the tick attribute of the {@link AspMetaKnowledge} when a tick is
	 * done.
	 */
	private SimulationListener simListener = new SimulationAdapter() {
		@Override
		public void tickDone(KReaturesEnvironment simulationEnvironment) {
			tick += 1;
		}
	};
	
	/** the current simulation tick */
	private int tick = 0;
	
	/** 
	 * A map that maps an atom to it's three constants for positive, 
	 * negative and variable representation 
	 */
	private Map<DLPAtom, ConstantTriple> constantsMap = new HashMap<>();
	
	/** the id generator that is used for the constants' names */
	private IdGenerator idGen = new IdGenerator();
	
	/** Default Ctor */
	public AspMetaKnowledge() {}
	
	/** Copy Ctor */
	public AspMetaKnowledge(AspMetaKnowledge other) {
		super(other);
	}
	
	@Override
	public void init(Map<String, String> parameters) {
		KReatures.getInstance().addSimulationListener(simListener);
		super.init(parameters);
	}
	
	/** @return the current tick of the simulation */
	public int getTick() {
		return tick;
	}
	
	/**
	 * Uses the given belief base to infer the MATES constant for the 
	 * given atom. That means the positive constant is processed if the atom
	 * is part of the belief base, the negative constant is processed if
	 * the negated atom is part of the belief base. The variable constant is 
	 * processed if the belief base contains no knowledge about atom.
	 * 
	 * @param	atom	An atom that represents the knowledge that is 
	 * 					searched in the belief base.
	 * @param	source	A belief base that acts as source for the operation
	 * @return	A constant representing the version of the given atom in the given
	 * 			belief base.
	 */
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
	
	/**
	 * Gets the positive constant for the given atom. The constant triple
	 * is generated if it does not exist yet.
	 * 
	 * @param atom	The atom
	 * @return A constant that represents the positive version of the atom
	 */
	public Constant matesPosConst(DLPAtom atom) {
		return getOrCreateTriple(atom).posConst;
	}
	
	/**
	 * Gets the negative constant for the given atom. The constant triple
	 * is generated if it does not exist yet.
	 * 
	 * @param atom	The atom
	 * @return A constant that represents the negative version of the atom
	 */
	public Constant matesNegConst(DLPAtom atom) {
		return getOrCreateTriple(atom).negConst;
	}
	
	/**
	 * Gets the variable constant for the given atom. The constant triple
	 * is generated if it does not exist yet.
	 * 
	 * @param atom	The atom
	 * @return A constant that represents the variable version of the atom
	 */
	/*
	public Constant matesVar(DLPAtom atom) {
		return getOrCreateTriple(atom).varConst;
	}
	*/
	
	/**
	 * Gets or creates a triple that contains three constants for
	 * a given atom. This triple contains a constant for positive,
	 * one for negative and one for variable representation of the
	 * atom as described in MATES13.
	 * 
	 * @param atom	The atom
	 * @return		A triple of constants
	 */
	public ConstantTriple getOrCreateTriple(DLPAtom atom) {
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
