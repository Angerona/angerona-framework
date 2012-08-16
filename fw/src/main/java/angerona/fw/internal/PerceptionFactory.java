package angerona.fw.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
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
		if(paramValue.startsWith("$")) {
			Object obj = context.get(paramValue.substring(1));
			if(obj instanceof FolFormula) {
				return (FolFormula)obj;
			} else if(obj instanceof String) {
				return new Atom(new Predicate(obj.toString()));
			} else {
				throw new ClassCastException("Cannot cast: " + obj.toString() + " to FolFormula");
			}
		}
		else {
			if(paramValue.startsWith("-")) {
				paramValue = paramValue.substring(1);
				return new Negation(new Atom(new Predicate(paramValue)));
			}
			return new Atom(new Predicate(paramValue));
		}
	}
	
	public static Set<FolFormula> createFormulaSet(String paramValue, Context context) {
		Set<FolFormula> reval = new HashSet<FolFormula>();
		
		if(paramValue.startsWith("$")) {
			Object obj = context.get(paramValue.substring(1));
			if(obj instanceof Collection<?>) {
				for(Object element : (Collection<?>)obj) {
					if(element instanceof FolFormula)
						reval.add((FolFormula)element);
				}
			}
		} else {
			String [] parts = paramValue.split(",");
			for(String part : parts) {
				reval.add(new Atom(new Predicate(part)));
			}
		}
		return reval;
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
				return ((AngeronaAnswer)obj).getAnswerValue();
			else 
				return (AnswerValue)obj;
		}
		else {
			return AngeronaAnswer.valueOf(paramValue.substring(1));
		}
	}
}
