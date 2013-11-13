package com.github.angerona.fw.logic;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;
import net.sf.tweety.lp.nlp.syntax.NLPRule;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.operators.parameter.TranslatorParameter;
import com.github.angerona.fw.util.Pair;

/**
 * Base class for all translate operations for different belief base type
 * implementations.
 * 
 * It supports two important methods which has to be implemented by subclasses:
 * 1. translatePerception to transform a perception into a belief base which
 *    can be used for revision operations with the agents knowledge
 * 2. translateFOL to transform a set of FOL formulas into a belief base which
 *    which represents the knowledge encoded in FOL.
 * 
 * @author Tim Janus
 */
public abstract class BaseTranslator 
	extends Operator<BaseBeliefbase, TranslatorParameter, BaseBeliefbase>{
	
	public static final String OPERATION_TYPE = "Translator";
	
	@Override
	protected BaseBeliefbase processInternal(TranslatorParameter preprocessedParameters) {
		if(preprocessedParameters.getPerception() != null) {
			return translatePerceptionInt(preprocessedParameters.getBeliefBase(), 
					preprocessedParameters.getPerception());
		} else if(preprocessedParameters.getInformation() != null) {
			return translateNLPInt(preprocessedParameters.getBeliefBase(), 
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
	 * but the real work is done in the translatePerceptionInt method. 
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param p			The Perception which shall be translated into a BaseBeliefbase.
	 * @return			A BaseBeliefbase containing the information encoded in the Perception.
	 */
	public BaseBeliefbase translatePerception(BaseBeliefbase caller, Perception p) {
		caller.getStack().pushOperator(this);
		BaseBeliefbase reval = translatePerceptionInt(caller, p);
		caller.getStack().popOperator();
		return reval;
	}
	
	/**
	 * Sub classes implement this method to translate a Perception into a sub class of
	 * BaseBeliefbase.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param p			The Perception which shall be translated into a BaseBeliefbase.
	 * @return			A BaseBeliefbase instance containing the knowledge encapsulated in p.
	 */
	protected abstract BaseBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p);
	
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
		BaseBeliefbase reval = translateNLPInt(caller, program);
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
	protected abstract BaseBeliefbase translateNLPInt(BaseBeliefbase caller, NLPProgram program);
	
	
	@Override
	protected TranslatorParameter getEmptyParameter() {
		return new TranslatorParameter();
	}

	@Override
	protected BaseBeliefbase defaultReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
