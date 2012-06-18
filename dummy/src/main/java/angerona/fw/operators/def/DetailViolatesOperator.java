package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.operators.parameter.ViolatesParameter;

public class DetailViolatesOperator extends ViolatesOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViolatesOperator.class);
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Detail-ViolatesOperator");
		//JOptionPane.showMessageDialog(null, param.getAction());
		if(param.getAction() instanceof Answer) {
			// only apply violates if confidential knowledge is saved in agent.
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new Boolean(false);
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) {
				// First we check for already unrivaled secrets:
				BaseBeliefbase view = (BaseBeliefbase) views.get(a.getReceiverId()).clone(); 
				
				List<Secret> toRemove = new LinkedList<Secret>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						//LOG.info(id + " Found CF=" + ct + " and answer=" + aa);
						if(	view.infere().contains(secret.getInformation()))  {
							toRemove.add(secret);
							LOG.warn("Secret-Knowledge inconsistency found and removed by Violates-Operator.");
						}
					}
				}
				for(Secret remove : toRemove) {
					conf.removeConfidentialTarget(remove);
				}
				
				// Now we adapt the view and check again.
				/*
				if(a.getAnswer() == AnswerValue.AV_TRUE) {
					view.addNewKnowledge(a.getRegarding());
				} else if(a.getAnswer() == AnswerValue.AV_FALSE) {
					view.addNewKnowledge(new Negation(a.getRegarding()));
				}
		*/
				view.addNewKnowledge(((DetailQueryAnswer) a).getDetailAnswer());
				
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						//LOG.info(id + " Found CF=" + ct + " and answer=" + aa);
						if(	view.infere().contains(secret.getInformation()))  {
							report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
							//conf.removeConfidentialTarget(ct);
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
