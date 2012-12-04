package angerona.fw.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.TranslatorParameter;

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
 *    // TODO: Remove the duplicate methods
 * 
 * @author Tim Janus
 */
public abstract class BaseTranslator extends Operator<TranslatorParameter, BaseBeliefbase>{
	
	protected BaseBeliefbase processInternal(TranslatorParameter preprocessedParameters) {
		if(preprocessedParameters.getPerception() != null) {
			return translatePerceptionInt(preprocessedParameters.getPerception());
		} else if(preprocessedParameters.getInformation() != null) {
			return translateFOLInt(preprocessedParameters.getInformation());
		}
		return null;
	}
	
	@Override
	protected TranslatorParameter getEmptyParameter() {
		return new TranslatorParameter();
	}

	@Override
	protected BaseBeliefbase defaultReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Sub classes implement this method to translate a perception into the
	 * belief base type represented by the translator.
	 * @param p
	 * @return	A BaseBeliefbase instance containing the knowledge encapsulated in p.
	 */
	protected abstract BaseBeliefbase translatePerceptionInt(Perception p);
	
	/**
	 * Translates the given perception in a beliefbase only containing the knowledge 
	 * encoded in the perception.
	 * Work is done in the translatePerceptionInt method. This method ensures that
	 * the operator callstack is updated.
	 * @param p		Reference to the perception.
	 * @return		A beliefbase containing the information encoded in the perception.
	 */
	public BaseBeliefbase translatePerception(Perception p) {
		getOwner().pushOperator(this);
		BaseBeliefbase reval = translatePerceptionInt(p);
		getOwner().popOperator();
		return reval;
	}
	
	/**
	 * Helper method: Extends the interface to easily handle one FOL-
	 * Formula instead a set of formulas.
	 * @param formula		Reference to the formula
	 * @return	A belief base containing the knowledge encoded in the FOL-Formula
	 */
	protected BaseBeliefbase translateFOLInt(FolFormula formula) {
		Set<FolFormula> set = new HashSet<>();
		set.add(formula);
		return translateFOLInt(set);
	}
	
	/**
	 * Sub classes implement this method to translate a set of formulas into the
	 * belief base type represented by the translator.
	 * @param formulas
	 * @return A belief base containing the knowledge encoded in the set of FOL-Formulas
	 */
	protected abstract BaseBeliefbase translateFOLInt(Set<FolFormula> formulas);
	
	/**
	 * Translates the given set of FOL-Formulas in a beliefbase only containing the knowledge
	 * encoded in the set of formulas.
	 * @param formulas	Reference to the set of formulas
	 * @return		A beliefbase containing the information encoded in the set of formulas.
	 */
	public BaseBeliefbase translateFOL(Set<FolFormula> formulas) {
		getOwner().pushOperator(this);
		BaseBeliefbase reval = translateFOLInt(formulas);
		getOwner().popOperator();
		return reval;
	}
}
