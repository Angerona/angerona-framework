package angerona.fw.logic.asp;

import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AngeronaDetailAnswer;
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
		return new AngeronaDetailAnswer(bb, query, findAnswer(query));
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
		
		Predicate qp = query.getPredicates().iterator().next();
		
		for (FolFormula f : knowledge)
		{
			// TODO: Their might be an answer to who(X) which has more than one formula: who(john), who(mary) ect.
			Predicate fp = f.getPredicates().iterator().next();
			if(!fp.getName().equals(qp.getName()))
				continue;
			
			answer = f;
			/*
			String fString = f.toString();
			String queryString = query.toString();
			if(fString.startsWith("!")) //Check if it's a negation
			{
				Atom a = null;
				if(f instanceof Negation)
					a = (Atom)((Negation)f).getFormula();
				else if(f instanceof Atom)
					a = (Atom)f;
				else
					throw new IllegalArgumentException(f.toString() + " is neither atom nor negation of an atom");
				Atom newAtom = new Atom(new Predicate(a.getPredicate().getName()), a.getArguments());
				answer = new Negation(newAtom);
			}
			else
			{
				if(fString.equals(queryString))
				{
					answer = f;
				}
			}
			*/
		}
		return answer;
		//return new Atom(new Predicate("TEST"));
	}
	/*
	public FolFormula skepticalDetailInference(List<AnswerSet> answerSets, FolFormula query)
	{
		
	}
	*/
}