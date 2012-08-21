package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.parameter.ViolatesParameter;
import angerona.fw.util.Pair;

/**
* This version of the violates operator allows for answers of the detail answer speech act type. 
* @author Daniel Dilger, Tim Janus
*/

public class DetailViolatesOperator extends ViolatesOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
	
	@Override
	protected ViolatesResult processInt(ViolatesParameter param) {
		LOG.info("Run Detail-ViolatesOperator");
		if(param.getAction() instanceof Answer) {
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new ViolatesResult();
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId()) && a.getAnswer().getAnswerValue() == AnswerValue.AV_COMPLEX) {
				BaseBeliefbase view = (BaseBeliefbase) views.get(a.getReceiverId()).clone(); 
				
				List<Secret> toRemove = new LinkedList<Secret>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							toRemove.add(secret);
							LOG.warn("Secret-Knowledge inconsistency found and removed by Violates-Operator.");
						}
					}
				}
				for(Secret remove : toRemove) {
					conf.removeConfidentialTarget(remove);
				}
				
				Set<FolFormula> answers = a.getAnswer().getAnswers();
				if(answers.size() > 1) {
					LOG.warn("More than one answer but '" + this.getClass().getSimpleName() + "' only works with one (first).");
				} else if(answers.size() == 0) {
					LOG.warn("No answers given. Might be an error... violates operator doing nothing!");
					return new ViolatesResult();
				}
				
				FolFormula answer = answers.iterator().next();
				// TODO: Move this into the default Violates Operator and let it support open and closed queries / answers.
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", answer);
				view.addKnowledge(answer);
				
				List<Pair<Secret, Double>> pairs = new LinkedList<>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
							pairs.add(new Pair<>(secret, 1.0));
						}
					}
				}
				return new ViolatesResult(pairs);
			}
		}
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new ViolatesResult();
	}
}
