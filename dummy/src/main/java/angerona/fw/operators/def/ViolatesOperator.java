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
import angerona.fw.PlanElement;
import angerona.fw.am.secrecy.operators.BaseViolatesOperator;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.SecrecyKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.parameter.EvaluateParameter;
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
	
	/** The ViolatesResult reference used internally if working on a plan in mental state */
	private ViolatesResult violates;
	
	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		LOG.info("Run Default-ViolatesOperator");
		
		// only apply violates if confidential knowledge is saved in agent.
		SecrecyKnowledge conf = param.getAgent().getComponent(SecrecyKnowledge.class);
		if(conf == null)
			return new ViolatesResult();
		
		Agent ag = param.getAgent();
		Beliefs beliefs = param.getBeliefs();
		Action p = (Action) param.getAtom();
		
		// TODO: Clone internally???
		Beliefs newBeliefs = ag.updateBeliefs((Perception)param.getAtom(), (Beliefs)beliefs.clone());
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
							param.report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAtom() + "'");
							pairs.add(new Pair<>(secret, 1.0));
						}
					}
				}
			}
		}
		ViolatesResult reval = new ViolatesResult(pairs);
	
		if(reval.isAlright())
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
			
			// prepare and run the plan-element:
			pe.prepare(this, param.getBeliefs());
			pe.run();
			
			// save the violates result in the plan-element.
			pe.setViolatesResult(violates);
			return violates;
		}
		throw new NotImplementedException("No complex plans supported by violate operator yet.");
	}
	
	@Override
	public void performAction(Action action, Agent agent, Beliefs beliefs) {
		EvaluateParameter param = new EvaluateParameter(agent,  beliefs, action);
		violates = violates.combine(process(param));
	}
}
