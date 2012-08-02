package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.operators.parameter.ViolatesParameter;

public class DetailViolatesOperator extends ViolatesOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Detail-ViolatesOperator");
		if(param.getAction() instanceof Answer) {
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new Boolean(false);
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) {
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
				
				DetailQueryAnswer dqa = ((DetailQueryAnswer) a);
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", dqa.getDetailAnswer());
				view.addKnowledge(dqa.getDetailAnswer());
				
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
				
							return new Boolean(true);
						}
					}
				}
			}
		}
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new Boolean(false);
	}
}
