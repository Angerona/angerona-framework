package com.github.angerona.fw.example.operators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.am.secrecy.Secret;
import com.github.angerona.fw.am.secrecy.components.SecrecyKnowledge;
import com.github.angerona.fw.am.secrecy.operators.BaseViolatesOperator;
import com.github.angerona.fw.am.secrecy.operators.ViolatesResult;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;
import com.github.angerona.fw.util.Pair;

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
	
	/** The ViolatesResult reference used internally if working on a plan in mental state */
	private ViolatesResult violates;
	
	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		LOG.info("Run Default-ViolatesOperator");
		
		// only apply violates if secrecy knowledge is saved in agent.
		SecrecyKnowledge conf = param.getAgent().getComponent(SecrecyKnowledge.class);
		if(conf == null)
			return new ViolatesResult();
		
		Agent ag = param.getAgent();
		Beliefs beliefs = param.getBeliefs();
		Action p = (Action) param.getAtom();
		
		// use cloned beliefs to generate new beliefs:
		//Beliefs newBeliefs = ag.updateBeliefs((Perception)param.getAtom(), beliefs.clone());
		Beliefs newBeliefs = ag.updateBeliefs(percept, beliefs.clone());
		
		
		Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
		BaseBeliefbase origView = beliefs.getViewKnowledge().get(p.getReceiverId());
		BaseBeliefbase view = newBeliefs.getViewKnowledge().get(p.getReceiverId()); 
		
		boolean alright = true;
		if(views.containsKey(p.getReceiverId())) {
			for(Pair<String, Map<String, String>> reasoningOperator : conf.getSecretsByReasoningOperator().keySet()) {
				
				// Infer only once with the ReasoningOperator defined by the pair.
				Set<FolFormula> origInfere = origView.infere(reasoningOperator.first, reasoningOperator.second);
				Set<FolFormula> cloneInfere = view.infere(reasoningOperator.first, reasoningOperator.second);
				for(Secret secret : conf.getSecretsByReasoningOperator().get(reasoningOperator)) {
					if(secret.getSubjectName().equals(p.getReceiverId())) {
						// Check for false positives first, output an warning, because secret weakening was not applied correctly then
						boolean inOrig = origInfere.contains(secret.getInformation());
						if(inOrig) {
							LOG.warn("The secret '{}' is already infered in the original view.", secret.toString());
							continue;
						}
						
						boolean inClone = cloneInfere.contains(secret.getInformation());
						if(	inClone )  {
							param.report("Secret: '" + secret + "' of '" + param.getAgent().getName() + 
									"' injured by: '" + param.getAtom() + "'");
							alright = false;
						}
					}
				}
			}
		}
		ViolatesResult reval = new ViolatesResult(alright);
		reval.setBeliefs(newBeliefs);
		
		if(alright)
			param.report("No violation applying the perception/action: '" + percept + "'");
		else
			param.report("Violation by appling the perception/action: '" + percept + "'");
		return reval;
	}
	
	@Override
	protected ViolatesResult onPlan(PlanElement pe, EvaluateParameter param) {
		if(pe.isAtomic()) {
			// clear ViolatesResult state:
			violates = new ViolatesResult();
			violates.setBeliefs(param.getBeliefs());
			
			// prepare and run the plan-element:
			pe.prepare(this, param.getBeliefs());
			pe.run();
			
			return violates;
		}
		throw new NotImplementedException("No complex plans supported by violate operator yet.");
	}
	
	@Override
	public void performAction(Action action, Agent agent, Beliefs beliefs) {
		if(agent == null) {
			return;
		}
		EvaluateParameter param = new EvaluateParameter(agent,  beliefs, action);
		violates = violates.combine(process(param));
	}

	@Override
	protected ViolatesResult onCheck(Agent agent, Beliefs beliefs) {
		// only apply violates if secrecy knowledge is saved in agent.
		SecrecyKnowledge conf = agent.getComponent(SecrecyKnowledge.class);
		if(conf == null)
			return new ViolatesResult();

		boolean alright = true;
		Map<String, Set<FolFormula>> viewInferences = new HashMap<>();
		for(Pair<String, Map<String, String>> reasoningOperator : conf.getSecretsByReasoningOperator().keySet()) {
			for (Secret s : conf.getSecretsByReasoningOperator().get(reasoningOperator)) {
				Set<FolFormula> infere = null;
				if(viewInferences.containsKey(s.getSubjectName())) {
					infere = viewInferences.get(s.getSubjectName());
				} else {
					infere = beliefs.getViewKnowledge().get(s.getSubjectName()).infere(reasoningOperator.first, 
							reasoningOperator.second);
					viewInferences.put(s.getSubjectName(), infere);
				}
				
				if(infere.contains(s.getInformation())) {
					alright = false;
				}
			}
		}
		
		return new ViolatesResult(alright);
	}
}
