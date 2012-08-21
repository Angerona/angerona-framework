package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.parameter.ViolatesParameter;
import angerona.fw.util.Pair;

/**
 * This class is capable of proofing if the applying of an answer
 * action violates confidentially.
 *
 * For every other action type the default violates operator returns
 * false.
 * @author Tim Janus
 */
public class ViolatesOperator extends BaseViolatesOperator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViolatesOperator.class);
	
	@Override
	protected ViolatesResult processInt(ViolatesParameter param) {
		LOG.info("Run Default-ViolatesOperator");
		
		Agent ag = param.getAgent();
		
		// only apply violates if confidential knowledge is saved in agent.
		ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
		if(conf == null)
			return new ViolatesResult();
		
		Beliefs beliefs = param.getBeliefs();
		if(param.getAction() instanceof Action) {
			Action p = (Action) param.getAction();
			
			// TODO: Clone internally???
			Beliefs newBeliefs = ag.updateBeliefs((Perception)param.getAction(), (Beliefs)beliefs.clone());
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			BaseBeliefbase origView = beliefs.getViewKnowledge().get(p.getReceiverId());
			BaseBeliefbase view = newBeliefs.getViewKnowledge().get(p.getReceiverId()); 
			
			List<Pair<Secret, Double>> pairs = new LinkedList<>();
			if(views.containsKey(p.getReceiverId())) {
				for(Pair<String, Map<String, String>> reasoningOperator : conf.getTargetsByReasoningOperator().keySet()) {
					
					// Infer only once with the ReasoningOperator defined by the pair.
					Set<FolFormula> origInfere = origView.infere(reasoningOperator.first, reasoningOperator.second);
					Set<FolFormula> cloneInfere = view.infere(reasoningOperator.first, reasoningOperator.second);
					for(Secret secret : conf.getTargetsByReasoningOperator().get(reasoningOperator)) {
						if(secret.getSubjectName().equals(p.getReceiverId())) {
							// Check for false positives first, output an warning, because secret weakening was not applied correctly then
							boolean inOrig = origInfere.contains(secret.getInformation());
							if(inOrig) {
								LOG.warn("The secret '{}' is already infered in the original view.", secret.toString());
								continue;
							}
							
							boolean inClone = cloneInfere.contains(secret.getInformation());
							if(	inClone )  {
								report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
								pairs.add(new Pair<>(secret, 1.0));
							}
						}
					}
				}
			}
			return new ViolatesResult(pairs);
		} else if(param.getAction() instanceof Skill) {
			Skill skill = (Skill)param.getAction();
			skill.setBeliefs(beliefs);
			skill.setRealRun(false);
			report("Performing mental-action applying: '"+skill.getName()+"'");
			skill.run();
			return skill.violates();
		} else if(param.getAction() instanceof Subgoal) {
			throw new NotImplementedException("The support for subgoals is not implemented by '" + this.getClass().getSimpleName() +  "'.");
		}
		
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new ViolatesResult();
	}
}
