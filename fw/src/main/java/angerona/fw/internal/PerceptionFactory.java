package angerona.fw.internal;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.Perception;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;
import angerona.fw.serialize.perception.PerceptionDO;

public abstract class PerceptionFactory {
	public abstract Perception generateFromDataObject(PerceptionDO dataObject, Context context);
	
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
