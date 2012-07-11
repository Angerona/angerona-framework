package angerona.fw.mary;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Query;
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
	//TODO: make the lying based off of answer sets in the agent's view of the attacking agent, rather than its own worldview
	//Another LyingOperator could use lying alternatives predefined in XML
	protected AngeronaDetailAnswer lie(AngeronaDetailAnswer truth, BaseBeliefbase bb)
	{
		FolFormula trueAnswer = truth.getAnswerExtended();
		FolFormula query = (FolFormula) truth.getQuery();
		FolFormula lie = null;
		
		// You had to use Strings because you got the classes from the asp library.
		// In this case you get them from the first-order-logic lib.
		if(trueAnswer instanceof Negation)
		{
			Negation neg = (Negation)trueAnswer;
			lie =  neg.getFormula();

		}
		else
		{
			lie = new Negation(trueAnswer);
	
		}
		return new AngeronaDetailAnswer(truth.getKnowledgeBase(), query, lie);
	}
}
