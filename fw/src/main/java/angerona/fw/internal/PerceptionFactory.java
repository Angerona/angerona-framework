package angerona.fw.internal;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.Perception;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;
import angerona.fw.serialize.perception.PerceptionDO;

/**
 * Base class for perception-factories.
 * A basic implementation is implemented in {@link DefaultPerceptionFactory}
 * @author Tim Janus
 */
public abstract class PerceptionFactory {
	public abstract Perception generateFromDataObject(PerceptionDO dataObject, Context context);
	
	/**
	 * creates a string using the given paramValue. 
	 * If it starts with a prefix '$' paramValue identifies a variable.
	 * @param paramValue
	 * @param context
	 * @return	if paramValue identifies no variable paramValue is returned
	 * 			otherwise the value of the variable identified by paramValue 
	 * 			is returned.
	 */
	public static String createString(String paramValue, Context context) {
		if(paramValue.startsWith("$"))
			return (String)context.get(paramValue.substring(1));
		else
			return paramValue;
	}
	
	/**
	 * Creates a fol-formula (atom) using the given paramValue.
	 * If it starts with a prefix '$' paramValue identifies a variable.
	 * @param paramValue
	 * @param context
	 * @return	if paramValue identifies no variable the string 
	 * 			in paramValue is cast to a tweety atom otherwise 
	 * 			the value of the identified variable is returned
	 * 			as tweety atom.
	 */
	public static FolFormula createFormula(String paramValue, Context context) {
		if(paramValue.startsWith("$"))
			return (FolFormula)context.get(paramValue.substring(1));
		else // TODO: use more generic formula generation.
			return new Atom(new Predicate(paramValue));
	}
	
	/**
	 * create a angerona-answer using the given paramValue.
	 * If it starts with a prefix '$' paramValue identifies a variable.
	 * @param paramValue
	 * @param context
	 * @return	if paramValue identifies no variable the string in
	 * 			paramValue is interpret as AngeronaAnswer otherwise
	 * 			the value of the identified variables is returned as
	 * 			AngeronaAnswer.
	 */
	public static AnswerValue createAnswerValue(String paramValue, Context context) {
		if(paramValue.startsWith("$")) {
			Object obj = context.get(paramValue.substring(1));
			if(obj instanceof AngeronaAnswer)
				return ((AngeronaAnswer)obj).getAnswerExtended();
			else 
				return (AnswerValue)obj;
		}
		else {
			return AngeronaAnswer.valueOf(paramValue.substring(1));
		}
	}
}
