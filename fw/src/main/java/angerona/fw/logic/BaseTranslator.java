package angerona.fw.logic;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.operators.Operator;

/**
 * Base class for all translate operations for different beliefbase type
 * implementations.
 * @author Tim Janus
 *
 * @param <TBB>	The type of the Beliefbase this tranlator translate to
 */
public abstract class BaseTranslator<TBB extends BaseBeliefbase> extends Operator<Perception, TBB>{
	/**
	 * Translates the given perception in a beliefbase only containing the knowledge 
	 * encoded in the perception.
	 * @param p		Reference to the perception.
	 * @return		A beliefbase of the type TBB containing the information encoded 
	 * 				in the perception.
	 */
	public abstract TBB translatePerception(Perception p);
	
	/**
	 * Translates the given set of FOL-Formulas in a beliefbase only containing the knowledge
	 * encoded in the set of formulas.
	 * @param formulas	Reference to the set of formulas
	 * @return		A beliefbase of the type TBB containing the information encoded
	 * 				in the set of formulas.
	 */
	public abstract TBB translateFOL(Set<FolFormula> formulas);
	
	@Override
	protected TBB processInt(Perception param) {
		return translatePerception(param);
	}
}
