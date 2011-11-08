package angerona.fw;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;

public abstract class PerceptionFactory {
	public List<Perception> generateFromParentElement(Element elParent, Context context) {
		List<Perception> reval = new LinkedList<Perception>();
		NodeList lst = elParent.getChildNodes();
		for(int i=0; i<lst.getLength(); ++i) {
			if(lst.item(i) instanceof Element) {
				reval.add(generateFromElement((Element)lst.item(i), context));
			}
		}
		return reval;
	}
	
	public abstract Perception generateFromElement(Element elPerception, Context context);
	
	public static String createString(String attributeValue, Context context) {
		if(attributeValue.startsWith("$"))
			return (String)context.get(attributeValue.substring(1));
		else
			return attributeValue;
	}
	
	public static FolFormula createFormula(String attributeValue, Context context) {
		if(attributeValue.startsWith("$"))
			return (FolFormula)context.get(attributeValue.substring(1));
		else // TODO: use more generic formula generation.
			return new Atom(new Predicate(attributeValue));
	}
	
	public static AnswerValue createAnswerValue(String attributeValue, Context context) {
		if(attributeValue.startsWith("$")) {
			Object obj = context.get(attributeValue.substring(1));
			if(obj instanceof AngeronaAnswer)
				return ((AngeronaAnswer)obj).getAnswerExtended();
			else 
				return (AnswerValue)obj;
		}
		else {
			return AngeronaAnswer.valueOf(attributeValue.substring(1));
		}
	}
}
