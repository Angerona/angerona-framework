package angerona.fw.mary;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AngeronaDetailAnswer;
import angerona.fw.logic.AnswerValue;

public class LyingOperator {
	/**
	 * Returns the AnswerValue version of a lie, given the AngeronaAnswer containing the truth. Simply negates it. 
	 * @param truth
	 * @return
	 */
	protected AnswerValue lie(AngeronaAnswer truth)
	{
		if(truth.getAnswerExtended() == AnswerValue.AV_TRUE)
			return AnswerValue.AV_FALSE;
		else if(truth.getAnswerExtended() == AnswerValue.AV_FALSE)
			return AnswerValue.AV_TRUE;
		return AnswerValue.AV_UNKNOWN;
	}
	/**
	 * Returns the FolFormula version of a lie, wrapped in its own AngeronaDetailAnswer. Not sure yet if it should just return the FolFormula.
	 * Simply negates it for now. Later there should be an option for lying by choosing an alternative possibility. 
	 * Lying operates on parsing the string version of the FolFormula, which is admittedly crude. 
	 * @param truth
	 * @return
	 */
	protected AngeronaDetailAnswer lie(AngeronaDetailAnswer truth)
	{
		FolFormula trueAnswer = truth.getAnswerExtended();
		String trueAnswerString = trueAnswer.toString();
		FolFormula lie = null;
		if(trueAnswerString.startsWith("!"))
		{
			lie =  (FolFormula) new Atom(new Predicate(trueAnswerString.substring(1))); //Test this parsing-based lying mechanism
		}
		else
		{
			lie = new Negation(trueAnswer);
		}
		return new AngeronaDetailAnswer(truth.getKnowledgeBase(), truth.getQuery(), lie);
	}
}
