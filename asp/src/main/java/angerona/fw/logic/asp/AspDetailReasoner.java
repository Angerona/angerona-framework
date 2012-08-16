package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AngeronaDetailAnswer;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
* This reasoning operator determines what answers satisfy a query of the detail query or open query types. 
* @author Daniel Dilger
*/

public class AspDetailReasoner extends AspReasoner {
	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}
	
	public Answer query(FolFormula query) {
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		return new AngeronaDetailAnswer(bb, query, findAnswer(query));
	}
	
	/**Chooses one instance of the answers given by openQueryAnswer.
	 * Actually, having a separate function for this doesn't make much sense. It would never be used.
	 * If it ever was used, there would be the danger of always repeating oneself in cases where the question is asked again.
	 * True implementation of the "Detail Query Answer" speech act would just take the open query answers and split them up
	 * @param 
	 * @return null (unless a need for the method is found, then FolFormula answer)
	 */
	protected FolFormula detailQueryAnswer(FolFormula query)
	{
		return null;
	}
	
	/** Method for 
	 * @param FolFormula query
	 * @return Set<AngeronaDetailAnswer> answers
	 */
	protected Set<FolFormula> openQueryAnswers(FolFormula query)
	{

		Set <FolFormula> knowledge = super.infer(); 
		Set<FolFormula> answers = new HashSet<FolFormula>();
		
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
	}
	
	/**
	 * Deprecated
	 * Special case where d = 0. That is, a credulous operator
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
	}
	/**
	 * Return the answers for an open query in the form of AngeronaDetailAnswer (speech act) objects
	 * @param FolFormula query
	 * @return Set<AngeronaDetailAnswer>
	 */
	@Override
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
	/**
	 * Deprecated. Use openAnswerQuery instead (either would be called from queryallAnswers)
	 * @param query
	 * @return Set<FolFormula>
	 */
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
	}
}