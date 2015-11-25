package com.github.kreaturesfw.core.logic;

import java.util.Set;

import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.Perception;
import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.comm.Inform;
import com.github.kreaturesfw.core.comm.Justification;
import com.github.kreaturesfw.core.comm.Justify;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.comm.SpeechAct;
import com.github.kreaturesfw.core.operators.Operator;
import com.github.kreaturesfw.core.operators.parameter.TranslatorParameter;
import com.github.kreaturesfw.core.util.Pair;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;
import net.sf.tweety.lp.nlp.syntax.NLPRule;

/**
 * Base class for all translate operations for different belief base type
 * implementations. It supports the translation of {@link Perception}, of first order logic
 * and of nested logic programs, whereby the perceptions and first order constructs are
 * translated into nested logic programs internally.
 * 
 * It supports two important methods which has to be implemented by subclasses:
 * 1. translatePerceptionImpl() to transform a perception into a belief base which
 *    can be used for revision operations with the agents knowledge
 * 2. translateNLPImpl() to transform a set of nested logic program rules into a belief base
 *    which represents the knowledge encoded in the given NLP.
 * 
 * @author Tim Janus
 */
public abstract class BaseTranslator 
	extends Operator<BaseBeliefbase, TranslatorParameter, BaseBeliefbase>{
	
	public static final String OPERATION_TYPE = "Translator";
	
	@Override
	protected BaseBeliefbase processImpl(TranslatorParameter preprocessedParameters) {
		if(preprocessedParameters.getPerception() != null) {
			return translatePerceptionImpl(preprocessedParameters.getBeliefBase(), 
					preprocessedParameters.getPerception());
		} else if(preprocessedParameters.getInformation() != null) {
			return translateNLPImpl(preprocessedParameters.getBeliefBase(), 
					preprocessedParameters.getInformation());
		}
		return null;
	}
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		Pair<String, Class<?>> reval = new Pair<>();
		reval.first = OPERATION_TYPE;
		reval.second = BaseTranslator.class;
		return reval;
	}
	
	/**
	 * Translates the given perception in a belief base only containing the knowledge 
	 * encoded in the Perception. This method ensures that the operator callstack is updated
	 * but the real work is done in the translatePerceptionImpl() method. 
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param p			The Perception which shall be translated into a BaseBeliefbase.
	 * @return			A BaseBeliefbase containing the information encoded in the Perception.
	 */
	public BaseBeliefbase translatePerception(BaseBeliefbase caller, Perception p) {
		caller.getStack().pushOperator(this);
		BaseBeliefbase reval = translatePerceptionImpl(caller, p);
		caller.getStack().popOperator();
		return reval;
	}
	
	/**
	 * Sub classes implement this method to translate a Perception into a sub class of
	 * BaseBeliefbase.
	 * 
	 * Angerona's default package uses {@link SpeechAct} as communication actions. There are
	 * requesting speech-acts like {@link Query} or {@link Justify} and informative speech-acts
	 * like {@link Inform}, {@link Justification} and {@link Answer}. They have in common that
	 * they store a set of information as first order formulas. But {@link Answer} is special
	 * because it also stores more semantically information in form of a {@link AnswerValue}.
	 * Thats why it is highly recommended to investigate the {@link SpeechAct} interface and at
	 * least the {@link Answer} and the {@link Query} speech-act before implementing this
	 * method on your own.
	 * 
	 * 
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param p			The Perception which shall be translated into a BaseBeliefbase.
	 * @return			A BaseBeliefbase instance containing the knowledge encapsulated in p.
	 */
	protected abstract BaseBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception p);
	
	/**
	 * Translates the given FOL formula in a BaseBeliefbase that only contains the knowledge
	 * encoded in the formula. 
	 * @param caller		The BaseBeliefbase which acts as caller.
	 * @param formula		A FOL formula representing the knowledge
	 * @return				A belief base containing the knowledge encoded in the formula.
	 */
	public BaseBeliefbase translateFOL(BaseBeliefbase caller, FolFormula formula) {
		NLPProgram program = new NLPProgram();
		program.add(new NLPRule(formula));
		return translateNLP(caller, program);
	}
	
	/**
	 * Translates the given set of FOL formula in a BaseBeliefbase that only contains the knowledge
	 * encoded in the set.
	 * @param caller		The BaseBeliefbase which acts as caller.
	 * @param formulas		A set of FOL formulas representing the knowledge
	 * @return				A belief base containing the knowledge encoded in the set.
	 */
	public BaseBeliefbase translateFOL(BaseBeliefbase caller, Set<FolFormula> formulas) {
		NLPProgram program = new NLPProgram();
		for(FolFormula f : formulas) {
			program.add(new NLPRule(f));
		}
		return translateNLP(caller, program);
	}
	
	/**
	 * Translates the given NLP-rule in a BaseBeliefbase that only contains the knowledge
	 * encoded in the rule.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param rule		A NLP rule containing the information which shall be translated
	 * @return	A belief base containing the knowledge encoded in the rule.
	 */
	public BaseBeliefbase translateNLP(BaseBeliefbase caller, NLPRule rule) {
		NLPProgram program = new NLPProgram();
		program.add(rule);
		return translateNLP(caller, program);
	}
	
	/**
	 * Translates the given NLP-program in a BaseBeliefbase that only contains the knowledge
	 * encoded in the Program.
	 * @param 	caller		The BaseBeliefbase which acts as caller.
	 * @param 	formulas	Reference to the set of formulas
	 * @return	A BaseBeliefbase containing the information encoded in the NLP-program.
	 */
	public BaseBeliefbase translateNLP(BaseBeliefbase caller, NLPProgram program) {
		caller.getStack().pushOperator(this);
		BaseBeliefbase reval = translateNLPImpl(caller, program);
		caller.getStack().popOperator();
		return reval;
	}
	
	/**
	 * Sub classes have to implement this method to translate a NLP program into the
	 * belief base type represented by the translator.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param program	A NLP-program containing the information which shall be translated.
	 * @return A BaseBeliefbase containing the knowledge encoded in the set of FolFormula.
	 */
	protected abstract BaseBeliefbase translateNLPImpl(BaseBeliefbase caller, NLPProgram program);
	
	
	@Override
	protected TranslatorParameter getEmptyParameter() {
		return new TranslatorParameter();
	}
}
