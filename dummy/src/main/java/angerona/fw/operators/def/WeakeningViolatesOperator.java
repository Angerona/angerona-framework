package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.operators.parameter.ViolatesParameter;
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

	/**
	 * Assumes information given is always a fact
	 */
	private Rule convertToRule(FolFormula f) {
		String ruleString = f.toString();
		ruleString += ".";
		/* Quick fix to bridge representations of ! and - */
		if (ruleString.startsWith("!")) {
			ruleString = "-" + ruleString.substring(1);
		}
		Rule rule = new Rule(ruleString);
		return rule;
	}

	/**
	 * 
	 */
	private List<Pair<Secret, Double>> representTotalExposure(
			ConfidentialKnowledge conf) {
		List<Pair<Secret, Double>> reval = new LinkedList<>();
		for (Secret secret : conf.getTargets()) {
			reval.add(new Pair<>(secret, INFINITY));
		}
		return reval;
	}

	/**
	 * 
	 */
	private double calculateSecrecyStrength(FolFormula secretInfo,
			List<AnswerSet> ansSets) {
		double numAnsSets = ansSets.size();
		double setsWithSecret = 0.0;
		for (AnswerSet as : ansSets) {
			Program p = as.toProgram();
			Rule secretRule = convertToRule(secretInfo);
			if (p.hasRule(secretRule)) {
				setsWithSecret += 1;
			}
		}
		double quotient = setsWithSecret / numAnsSets;
		double strength = 1.0 - quotient;
		return strength;
	}

	@Override
	protected ViolatesResult onPerception(Perception percept, ViolatesParameter param) {
		Logger LOG = LoggerFactory.getLogger(WeakeningViolatesOperator.class);
		List<Pair<Secret, Double>> secretList = new LinkedList<>();

		/*
		 * Check if any confidential knowledge present. If none then no secrecy
		 * weakening possible
		 */
		ConfidentialKnowledge conf = param.getAgent().getComponent(
				ConfidentialKnowledge.class);
		if (conf == null)
			return new ViolatesResult();

		/*
		 * Remaining operations depend on whether the action in question is an answer
		 */
		if (param.getAtom() instanceof Answer) {

			Answer a = (Answer) param.getAtom();

			/*
			 * Consider self-repeating answers (and only answers) as bad as
			 * revealing all secrets
			 */
			List<Action> actionsHistory = param.getAgent().getActionHistory();
			for (Action act : actionsHistory) {
				if (a.equals(act)) {
					report(param.getAgent().getName()
							+ "' <b> self-repeats </b> with: '"
							+ param.getAtom() + "'");
					secretList = representTotalExposure(conf);
					return new ViolatesResult(secretList);
				}
			}

			Map<String, BaseBeliefbase> views = param.getBeliefs()
					.getViewKnowledge();
			if (views.containsKey(a.getReceiverId())
					&& a.getAnswer().getAnswerValue() == AnswerValue.AV_COMPLEX) {

				// TODO: Merge violates operators
				AspBeliefbase view = (AspBeliefbase) views.get(a.getReceiverId()).clone();
				Program prog = view.getProgram();

				Set<FolFormula> answers = a.getAnswer().getAnswers();
				if (answers.size() > 1) {
					LOG.warn("More than one answer but '" + this.getClass().getSimpleName()
							+ "' only works with one (first).");
				} else if (answers.size() == 0) {
					LOG.warn("No answers given. Might be an error... violates operator doing nothing!");
					return new ViolatesResult();
				}
				FolFormula answer = answers.iterator().next();
				LOG.info("Make Revision for QueryAnswer: '{}'", answer);

				Rule rule = convertToRule(answer);
				prog.add(rule);

				/*
				 * Check for contradictions. If one is found consider all
				 * secrets totally revealed
				 */
				List<AnswerSet> newAnsSets = null;

				AspReasoner ar = (AspReasoner) view.getReasoningOperator();
				ar.infer(view);
				view.infere();
				newAnsSets = ar.processAnswerSets();
				prog = view.getProgram();

				if (newAnsSets == null) {
					String actString = param.getAtom().toString();
					report(param.getAgent().getName() + "' <b> creates contradiction </b> by: '"
							+ actString.substring(0, actString.length() - 1) + "'", view);
					secretList = representTotalExposure(conf);
					return new ViolatesResult(secretList);
				}

				/* Now the secrecy strengths get added */
				for (Secret secret : conf.getTargets()) {
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

						// TODO: Find default policy like a default parameter
						// value if not set yet.
						String d = secret.getReasonerParameters().get("d");
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
							report(param.getAgent().getName()
									+ "' <b> weakens secret: </b> '"
									+ secretInfo.toString()
									+ "' by: '"
									+ degreeOfWeakening
									+ "' with: '"
									+ actString.substring(0,
											actString.length() - 1) + "'", view);
						} else {
							report(param.getAgent().getName()
									+ "' <b> weakens no secrets </b> ' with: '"
									+ actString.substring(0,
											actString.length() - 1) + "'", view);
						}
					} else {
						String actString = param.getAtom().toString();
						report(param.getAgent().getName()
								+ "' <b> weakens no secrets: </b> ' with: '"
								+ actString.substring(0, actString.length() - 1)
								+ "'", view);
					}
				}
			}
		}

		return new ViolatesResult(secretList);
	}
}
