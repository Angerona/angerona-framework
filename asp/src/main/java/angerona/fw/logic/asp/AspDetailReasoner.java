package angerona.fw.logic.asp;

import java.util.List;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.BaseBeliefbase;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AngeronaDetailAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;

public class AspDetailReasoner extends AspReasoner {
	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}
	
	public Answer query(FolFormula query) {
		/*
		List<AnswerSet> answerSets = processAnswerSets();
		AnswerValue av = AnswerValue.AV_UNKNOWN;
		
		if(semantic == InferenceSemantic.S_CREDULOUS) {
			throw new NotImplementedException();
		} else if(semantic == InferenceSemantic.S_SKEPTICAL) {
			av = skepticalInference(answerSets, query);
		}
		*/
		//return super.query(query);
		//return new AngeronaAnswer(bb, query, AnswerValue.AV_REJECT);
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		Predicate p = new Predicate(findAnswer(query).toString());
		return new AngeronaDetailAnswer(bb, query, new Atom(p));
	}
	/**
	 * The AspDetailReasoner parses the string form of the query and looks for a match in the string forms of the knowledge.
	 * Admittedly a crude way to go about it. 
	 * @param query
	 * @return
	 */
	protected FolFormula findAnswer(FolFormula query)
	{
		Set <FolFormula> knowledge = super.infer();
		FolFormula answer = null;
		if (!query.toString().contains("(")) //If the query has 0 arity (if it's a true/false question)
		{
			for (FolFormula f : knowledge)
			{
				String fString = f.toString();
				String queryString = query.toString();
				if(fString.startsWith("!")) //Check if it's a negation
				{
					System.out.println("ASDF negation");
					answer = (Negation) f;
				}
				else
				{
					if(fString.equals(queryString))
					{
						answer = f;
					}
					else
					{
						System.out.println("ASDF actual string:"+fString);
					}
				}
			}
		}
		return answer;
	}
	/*
	public FolFormula skepticalDetailInference(List<AnswerSet> answerSets, FolFormula query)
	{
		
	}
	*/
}