package angerona.fw.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.operators.Operator;

/**
 * Base class for all translate operations for different beliefbase type
 * implementations.
 * @author Tim Janus
 */
public abstract class BaseTranslator extends Operator<Perception, BaseBeliefbase>{
	
	protected abstract BaseBeliefbase translatePerceptionInt(Perception p);
	
	/**
	 * Translates the given perception in a beliefbase only containing the knowledge 
	 * encoded in the perception.
	 * @param p		Reference to the perception.
	 * @return		A beliefbase containing the information encoded in the perception.
	 */
	public BaseBeliefbase translatePerception(Perception p) {
		getOwner().pushOperator(this);
		BaseBeliefbase reval = translatePerceptionInt(p);
		getOwner().popOperator();
		return reval;
	}
	
	protected BaseBeliefbase translateFOLInt(FolFormula formula) {
		Set<FolFormula> set = new HashSet<>();
		set.add(formula);
		return translateFOLInt(set);
	}
	
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
	
	@Override
	protected BaseBeliefbase processInt(Perception param) {
		return translatePerception(param);
	}
}
