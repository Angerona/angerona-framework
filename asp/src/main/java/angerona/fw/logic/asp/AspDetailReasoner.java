package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.BaseBeliefbase;
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
			{
				continue;
			}
			
			answer = f;
		}
		return answer;
		//return new Atom(new Predicate("TEST"));
	}
	public Set<AngeronaDetailAnswer> queryForAllAnswers(FolFormula query)
	{
		Set<FolFormula> allAnswers = findAllAnswers(query);
		Set<AngeronaDetailAnswer> detailAnswers = new HashSet<AngeronaDetailAnswer>();
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		for (FolFormula answer : allAnswers)
		{
			detailAnswers.add(new AngeronaDetailAnswer(bb, query, answer));
		}
		return detailAnswers;
	}
	protected Set<FolFormula> findAllAnswers(FolFormula query)
	{
		Set <FolFormula> knowledge = super.infer();
		Set<FolFormula> answers = new HashSet<FolFormula>(); //Is it really appropriate to use this?
		
		Predicate qp = query.getPredicates().iterator().next();
		
		for (FolFormula f : knowledge)
		{
			// TODO: Their might be an answer to who(X) which has more than one formula: who(john), who(mary) ect.
			Predicate fp = f.getPredicates().iterator().next();
			if(!fp.getName().equals(qp.getName()))
			{
				continue;
			}
			else
			{
				answers.add(f);
			}
		}
		return answers;
		//return new Atom(new Predicate("TEST"));
	}
}