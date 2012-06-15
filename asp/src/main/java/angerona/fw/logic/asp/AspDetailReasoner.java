package angerona.fw.logic.asp;

import java.util.List;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
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
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		//return super.query(query);
		return new AngeronaDetailAnswer(bb, query, new Atom(new Predicate("FALSE")));
		//return new AngeronaAnswer(bb, query, AnswerValue.AV_REJECT);
	}

}