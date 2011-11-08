package angerona.fw;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.w3c.dom.Element;

import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AnswerValue;
import angerona.fw.nacts.NAAnswer;
import angerona.fw.nacts.NAQuery;
import angerona.fw.reflection.Context;

public class DefaultPerceptionFactory extends PerceptionFactory {

	@Override
	public Perception generateFromElement(Element elAP, Context context) {
		// HACK: The generation of the question (q) is highly plugin dependent...
		String elName = elAP.getNodeName();
		String s = elAP.getAttribute("sender");
		String r = elAP.getAttribute("receiver");
		
		s = PerceptionFactory.createString(s, context);
		r = PerceptionFactory.createString(r, context);
		
		if(elName.compareToIgnoreCase("Query") == 0) {
			FolFormula f = PerceptionFactory.createFormula(
					elAP.getAttribute("question"), context);
			return new NAQuery(s, r, f);
		}
		else if(elName.compareToIgnoreCase("Answer") == 0) {
			FolFormula f = PerceptionFactory.createFormula(
					elAP.getAttribute("question"), context);
			AnswerValue av = PerceptionFactory.createAnswerValue(
					elAP.getAttribute("answer"), context);
				
			return new NAAnswer(s, r, f, av);
		}
		
		throw new NotImplementedException("Factory doesn't support: " + elName + " as Parameter");
	}

}
