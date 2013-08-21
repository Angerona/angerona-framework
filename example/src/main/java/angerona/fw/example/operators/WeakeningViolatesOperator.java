package angerona.fw.example.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.translate.aspfol.AspFolTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.ActionHistory;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.am.secrecy.components.SecrecyKnowledge;
import angerona.fw.comm.Answer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.operators.OperatorCallWrapper;
import angerona.fw.operators.parameter.EvaluateParameter;
import angerona.fw.util.Pair;

/**
 * Extension of my DetailSimpleViolatesOperator which enables one to weaken
 * secrecy. The operator determines which secrets would be affected by an
 * action. It returns a list of pairs containing the secret and the degree by
 * which it would be weakened.
 * 
 * @author Daniel Dilger, Tim Janus
 * 
 */
public class WeakeningViolatesOperator extends ViolatesOperator {
	static final double INFINITY = 1000.0;

	private static final String EXPANSION = "angerona.fw.logic.asp.AspExpansion";

	/**
	 * 
	 */
	private List<Pair<Secret, Double>> representTotalExposure(
			SecrecyKnowledge conf) {
		List<Pair<Secret, Double>> reval = new LinkedList<>();
		for (Secret secret : conf.getSecrets()) {
			reval.add(new Pair<>(secret, INFINITY));
		}
		return reval;
	}

	private double calculateSecrecyStrength(FolFormula secretInfo,
			List<AnswerSet> ansSets) {
		double numAnsSets = ansSets.size();
		double setsWithSecret = 0.0;
		for (AnswerSet as : ansSets) {
			//Program p = as.toProgram();
			Program p = new Program();
			for(DLPLiteral lit : as) {
				p.addFact(lit);
			}
			//TODO: FIX
			AspFolTranslator translator = new AspFolTranslator();
			Rule secretRule = new Rule((DLPLiteral)translator.toASP(secretInfo));
			if (p.contains(secretRule)) {
				setsWithSecret += 1;
			}
		}
		double quotient = setsWithSecret / numAnsSets;
		double strength = 1.0 - quotient;
		return strength;
	}

	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		Logger LOG = LoggerFactory.getLogger(WeakeningViolatesOperator.class);
		List<Pair<Secret, Double>> secretList = new LinkedList<>();
		ViolatesResult reval = new ViolatesResult();

		// Check if any confidential knowledge present. If none then no secrecy
		//weakening possible
		SecrecyKnowledge conf = param.getAgent().getComponent(
				SecrecyKnowledge.class);
		if (conf == null)
			return reval;

		// Remaining operations depend on whether the action in question is an answer
		if (! (param.getAtom() instanceof Answer)) {
			return super.onPerception(percept, param);
		}
		Answer a = (Answer) param.getAtom();

		Beliefs newBeliefs = param.getBeliefs().clone();
		reval.setBeliefs(newBeliefs);
		// Consider self-repeating answers (and only answers) as bad as
		// revealing all secrets	
		ActionHistory history = param.getBeliefs().getComponent(ActionHistory.class);
		if(history != null) {
			if(history.didAction(param.getAgent().getName(), a)) {
				param.report(param.getAgent().getName()
						+ "' <b> self-repeats </b> with: '"
						+ param.getAtom() + "'");
				secretList = representTotalExposure(conf);
				reval.setSecretPairs(secretList);
				return reval;
			}
		}

		
		Map<String, BaseBeliefbase> views = newBeliefs.getViewKnowledge();
		if (views.containsKey(a.getReceiverId())
				&& a.getAnswer().getAnswerValue() == AnswerValue.AV_COMPLEX) {
			BaseBeliefbase view = views.get(a.getReceiverId());
			
			Set<FolFormula> answers = a.getAnswer().getAnswers();
			if (answers.size() == 0) {
				LOG.warn("No answers given. Might be an error... violates operator doing nothing!");
				return reval;
			}
			
			LOG.info("Make Revision for QueryAnswer: '{}'", answers);
			OperatorCallWrapper bcb = view.getOperators().getOperationSetByType(
					BaseChangeBeliefs.OPERATION_TYPE).getOperator(EXPANSION);
			if(bcb == null) {
				bcb = view.getOperators().getPreferedByType(BaseChangeBeliefs.OPERATION_TYPE);
				LOG.warn("The Weakening Operator wants to use '{}' " +
						"for revision. But it was not found and '{}' " +
						"will be used as default alternative.", EXPANSION, 
						bcb.getClass().getName());
			}
				
			view.addKnowledge(answers, null, bcb);

			// Check for contradictions. If one is found consider all
			// secrets totally revealed
			List<AnswerSet> newAnsSets = null;				
			AspReasoner ar = (AspReasoner) view.getReasoningOperator().getImplementation();
			newAnsSets = ar.processAnswerSets((AspBeliefbase)view);

			if (newAnsSets == null) {
				String actString = param.getAtom().toString();
				param.report(param.getAgent().getName() + "' <b> creates contradiction </b> by: '"
						+ actString.substring(0, actString.length() - 1) + "'", view);
				secretList = representTotalExposure(conf);
				reval.setSecretPairs(secretList);
				return reval;
			}

			// Now the secrecy strengths get added
			for (Secret secret : conf.getSecrets()) {
				FolFormula secretInfo = secret.getInformation();

				boolean secretContained = false;
				for (AnswerSet ans : newAnsSets) {
					if (ans.toString().contains(secretInfo.toString())) {
						int index = ans.toString().indexOf(
								secretInfo.toString());
						if (index == 0 || ans.toString().charAt(index - 1) != '-') {
							secretContained = true;
						}
					}
				}
				
				if (secretContained) {
					Pair<Secret, Double> sPair = new Pair<>();
					sPair.first = secret;
					double newStrength = calculateSecrecyStrength(secretInfo, newAnsSets);

					/** @todo Find default policy like a default parameter value if not set yet. */
					String d = secret.getReasonerSettings().get("d");
					double curStrength = 0;
					if (d == null) {
						curStrength = 1;
					} else {
						curStrength = Double.parseDouble(d);
					}
					
					double degreeOfWeakening = curStrength - newStrength;
					sPair.second = degreeOfWeakening;

					secretList.add(sPair);
					String actString = param.getAtom().toString();
					if (degreeOfWeakening > 0) {
						param.report(param.getAgent().getName()
								+ "' <b> weakens secret: </b> '"
								+ secretInfo.toString()
								+ "' by: '"
								+ degreeOfWeakening
								+ "' with: '"
								+ actString.substring(0,
										actString.length() - 1) + "'", view);
					} else {
						param.report(param.getAgent().getName()
								+ "' <b> weakens no secrets </b> ' with: '"
								+ actString.substring(0,
										actString.length() - 1) + "'", view);
					}
				} else {
					String actString = param.getAtom().toString();
					param.report(param.getAgent().getName()
							+ "' <b> weakens no secrets: </b> ' with: '"
							+ actString.substring(0, actString.length() - 1)
							+ "'", view);
				}
			}
		}

		reval.setSecretPairs(secretList);
		return reval;
	}
}
