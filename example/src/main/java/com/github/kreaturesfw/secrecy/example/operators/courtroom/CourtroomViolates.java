package com.github.kreaturesfw.secrecy.example.operators.courtroom;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.translators.aspfol.AspFolTranslator;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;
import net.sf.tweety.lp.asp.util.AnswerSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.asp.logic.AspBeliefbase;
import com.github.kreaturesfw.asp.logic.AspReasoner;
import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.legacy.Perception;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.core.util.Pair;
import com.github.kreaturesfw.secrecy.Secret;
import com.github.kreaturesfw.secrecy.components.SecrecyKnowledge;
import com.github.kreaturesfw.secrecy.example.operators.ViolatesOperator;
import com.github.kreaturesfw.secrecy.operators.ViolatesResult;

/**
 * Extension of my DetailSimpleViolatesOperator which enables one to weaken
 * secrecy. The operator determines which secrets would be affected by an
 * action. It returns a list of pairs containing the secret and the degree by
 * which it would be weakened.
 * 
 * @author Daniel Dilger, Tim Janus
 * 
 */
public class CourtroomViolates extends ViolatesOperator {
	static final double INFINITY = 1000.0;

	private static final String EXPANSION = "angerona.fw.logic.asp.AspExpansion";


	private double calculateSecrecyStrength(FolFormula secretInfo,
			List<AnswerSet> ansSets) {
		double numAnsSets = ansSets.size();
		double setsWithSecret = 0.0;
		for (AnswerSet as : ansSets) {
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
		return quotient;
	}

	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		Logger LOG = LoggerFactory.getLogger(CourtroomViolates.class);
		
		// Check if any confidential knowledge present. If none then no secrecy
		//weakening possible
		SecrecyKnowledge conf = param.getAgent().getComponent(
				SecrecyKnowledge.class);
		if (conf == null)
			return new ViolatesResult();

		// Remaining operations depend on whether the action in question is an answer
		if (! (param.getAtom() instanceof Answer)) {
			return super.onPerception(percept, param);
		}
		
		Answer a = (Answer) param.getAtom();
		Beliefs newBeliefs = param.getBeliefs().clone();
		Map<String, BaseBeliefbase> views = newBeliefs.getViewKnowledge();
		
		if (views.containsKey(a.getReceiverId())
				&& a.getAnswer().getAnswerValue() == AnswerValue.AV_COMPLEX) {
			BaseBeliefbase view = views.get(a.getReceiverId());
			
			Set<FolFormula> answers = a.getAnswer().getAnswers();
			if (answers.size() == 0) {
				LOG.warn("No answers given. Might be an error... violates operator doing nothing!");
				return new ViolatesResult();
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
				return new ViolatesResult(false);
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
					
					double degreeOfWeakening = newStrength - curStrength;
					sPair.second = degreeOfWeakening;

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
								+ "' <b> not weakens secret: </b>" +
								" '" + secretInfo.toString() + "' with: '"
								+ actString.substring(0,
										actString.length() - 1) + "'", view);
					}
				} else {
					String actString = param.getAtom().toString();
					param.report(param.getAgent().getName()
							+ "' <b> not weakens secrets </b>" +
							" '" + secretInfo.toString() + "' with: '"
							+ actString.substring(0, actString.length() - 1)
							+ "'", view);
				}
			}
		}

		
		ViolatesResult reval = super.onPerception(percept, param);
		return reval;
	}
}
