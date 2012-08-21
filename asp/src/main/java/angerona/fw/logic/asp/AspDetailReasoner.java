package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
* This reasoning operator determines what answers satisfy a query of the detail query or open query types. 
* @author Daniel Dilger, Tim Janus
*/

public class AspDetailReasoner extends AspReasoner {
	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}
	
	public Answer query(FolFormula query) {
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		return new AngeronaAnswer(bb, query, findAnswer(query));
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
	protected Set<FolFormula> findAnswer(FolFormula query)
	{
		Set <FolFormula> knowledge = super.infer();
		Set<FolFormula> answers = new HashSet<>();
		
		Predicate qp = query.getPredicates().iterator().next();
		
		for (FolFormula f : knowledge)
		{
			Predicate fp = f.getPredicates().iterator().next();
			if(!fp.getName().equals(qp.getName()))
			{
				continue;
			}
			
			answers.add(f);
		}
		return answers;
	}
	
}