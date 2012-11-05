package angerona.fw.mary;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
/**
* This file does not correspond to any theoretical operator.
* The class exists to specify the means by which a lie is generated
* @author Daniel Dilger, Tim Janus
*/
public class LyingOperator {
	/**
	 * Returns the AnswerValue version of a lie, given the AngeronaAnswer containing the truth. Simply negates it. 
	 * @param truth
	 * @return	the opposite answer given as parameter (lie)
	 */
	protected AnswerValue lie(AngeronaAnswer truth)
	{
		if(truth.getAnswerValue() == AnswerValue.AV_TRUE)
			return AnswerValue.AV_FALSE;
		else if(truth.getAnswerValue() == AnswerValue.AV_FALSE)
			return AnswerValue.AV_TRUE;
		return AnswerValue.AV_UNKNOWN;
	}
	
	/**
	 * Returns the FolFormula version of a lie, wrapped in its own AngeronaDetailAnswer. Not sure yet if it should just return the FolFormula.
	 * Simply negates it for now. Later there should be an option for lying by choosing an alternative possibility. 
	 * Lying operates on parsing the string version of the FolFormula, which is admittedly crude. 
	 * @param truth
	 * @return	The given formula is negated to create a lie from a true expression.
	 */
	protected FolFormula lie(FolFormula truth)
	{
		FolFormula lie = null;
		
		if(truth instanceof Negation) {
			Negation neg = (Negation)truth;
			lie =  neg.getFormula();

		} else {
			lie = new Negation(truth);
	
		}
		return lie;
	}
}
