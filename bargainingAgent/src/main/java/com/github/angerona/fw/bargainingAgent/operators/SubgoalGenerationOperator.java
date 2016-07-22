package com.github.angerona.fw.bargainingAgent.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.Tautology;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.bargainingAgent.Goals;
import com.github.angerona.fw.bargainingAgent.HistoryComponent;
import com.github.angerona.fw.bargainingAgent.Options;
import com.github.angerona.fw.bargainingAgent.comm.Offer;
import com.github.angerona.fw.bargainingAgent.util.Tupel;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.logic.asp.AspBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalRevision;
import com.github.angerona.fw.operators.OperatorCallWrapper;

/**
 * Bargaining agents subgoal generation generates the offers for the bargaining.
 * @author Pia Wierzoch
 */
public class SubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	
	/**
	 * Processes current desires..
	 */
	@Override
	protected Boolean processImpl(PlanParameter pp) {
		LOG.info("Run Bargaining-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		Desires des = ag.getComponent(Desires.class);
		
		boolean reval = false;
		
		if(des != null) {
			Set<Desire> currentDesires;
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareCounterOfferProcessing);
			for(Desire d : currentDesires) {
				reval = reval || processCounterOffer(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareFirstOfferProcessing);
			for(Desire d : currentDesires) {
				reval = reval || processFirstOffer(d, pp, ag);
			}
		}
		return reval;
	}
	
	/**
	 * Generates the first offer.
	 * @param d
	 * @param pp
	 * @param ag
	 */
	private boolean processFirstOffer(Desire desire, PlanParameter pp, Agent ag) {
		pp.report("Generate first offer");

		BaseBeliefbase bbase = ag.getBeliefs().getWorldKnowledge();
		Options options = bbase.getAgent().getComponent(Options.class);
		Goals goals = bbase.getAgent().getComponent(Goals.class);
		String receicerID = null;
		
		options.initialize(goals.getBestGoal(), bbase);
		
		for(String name :ag.getEnvironment().getAgentNames()){
			if(!name.equals(ag.getName())){
				receicerID = name;
			}
		}

		
		// send counter offer
		Offer counterOffer = new Offer(receicerID, ag.getName(), options.getNextOptionsWithoutRemoving());
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(counterOffer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ Offer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
		return true;
	}

	/**
	 * Process an incoming offer from an agent and generates an appropriate answer.
	 * @param desire
	 * @param pp
	 * @param ag
	 */
	public boolean processCounterOffer(Desire desire, PlanParameter pp, Agent ag) {
		pp.report("Generate new counter offer");

		BaseBeliefbase bbase = ag.getBeliefs().getWorldKnowledge();
		Options options = bbase.getAgent().getComponent(Options.class);
		Goals goals = bbase.getAgent().getComponent(Goals.class);
		HistoryComponent history = bbase.getAgent().getComponent(HistoryComponent.class);
		
		//get last self send offer
		Offer lastOffer = history.getHistory().get(history.getHistory().size()-2).getFirst();
		//Offer from Opponent
		Offer offer = (Offer) desire.getPerception();
		
		HashSet<FolFormula> demandsFromOpponent = offer.getDemandsAndPromises().getFirst();
		HashSet<FolFormula> prommisesFromOpponent = offer.getDemandsAndPromises().getLast();
		
		HashSet<FolFormula> lastDemands = lastOffer.getDemandsAndPromises().getFirst();
		HashSet<FolFormula> lastPromises = lastOffer.getDemandsAndPromises().getLast();
		//compute CounterOffer
		if(!(lastPromises.containsAll(demandsFromOpponent) &&
			 demandsFromOpponent.containsAll(lastPromises))){
			FolFormula bGoal = betterAchivableGoal(offer, bbase, goals, goals.getBestGoalPrio());
			if(bGoal != null){
				if(bbase instanceof AspBeliefbase){
					//TODO ASP

				}else if(bbase instanceof ConditionalBeliefbase){
					ConditionalBeliefbase base = (ConditionalBeliefbase) bbase;	
					List<HashSet<FolFormula>> supportSet = new LinkedList<HashSet<FolFormula>>();
					RuleBasedCReasoner reasoner = new RuleBasedCReasoner(base.getConditionalBeliefs());
					FOLPropTranslator translator = new FOLPropTranslator();
					
					OperatorCallWrapper changeOp = base.getChangeOperator();
					ConditionalRevision revisionOp = null;
					if(changeOp.getImplementation() instanceof ConditionalRevision) {
						revisionOp = (ConditionalRevision) changeOp.getImplementation();
					}
					
					reasoner.prepare();
					reasoner.process();
					RankingFunction rank = reasoner.getSemantic();
					rank.forceStrictness(base.getPropositions());
					
					ArrayList<Proposition> propositions = new ArrayList<>();
					
					Iterator<Proposition> sigIterator = rank.getSignature().iterator(); 
					while(sigIterator.hasNext()){
						 propositions.add(sigIterator.next());
					}
					
					if(rank.rank(translator.toPropositional(bGoal))== 0 && rank.rank(translator.toPropositional(new Negation(bGoal)))>0){
						HashSet<FolFormula> support = new HashSet<FolFormula>();
						supportSet.add(support);
					}else{
						int j = 0;
						label:
						while(j<propositions.size()){
							ArrayList<Proposition> props = new ArrayList<Proposition>();
							Iterator<Proposition> iterator = propositions.iterator();
							for(int i = 0; i<j;i++){
								props.add(iterator.next());
							}
							Conjunction conjunction = new Conjunction(props);
							if(props.isEmpty()){
								conjunction = new Conjunction(new Tautology(), new Tautology());
							}else{
								conjunction = new Conjunction(props);
							}
							
							iterator = propositions.iterator();
							for(int i = 0;i<j;i++){
								iterator.next();
							}
							while(iterator.hasNext()){
								Proposition prop = iterator.next();
								Conjunction con = conjunction.combineWithAnd(prop);
								
								rank = revisionOp.simulateOCFRevisionByFormulas(base, con);
								
								if(rank.rank(translator.toPropositional(bGoal)) == 0 && rank.rank(translator.toPropositional(new Negation(bGoal)))>0){
									//add found minimal support
									HashSet<FolFormula> support = new HashSet<FolFormula>();
									props.add(prop);
									for(Proposition p : props){
										support.add(translator.toFOL(p));
									}
									supportSet.add(support);
									
									//delete used propositions
									for(Proposition p : props){
										propositions.remove(p);
									}
									break label;
								}
							}
							j++;
						}
					}	
					
					
					for(HashSet<FolFormula> support: supportSet){
						goals.setBestGoal(bGoal);
						options.addOption(support, offer.getDemandsAndPromises().getFirst());
					}
				}
			}
		}else{
			if(!(lastDemands.containsAll(prommisesFromOpponent) && 
				 prommisesFromOpponent.containsAll(lastDemands))){
				options.getNextOptions();
				if(options.getOptions().isEmpty()){
					FolFormula newGoal = goals.deletBestGoal();
					options.initialize(newGoal, ag.getBeliefs().getWorldKnowledge());
				}
			}
		}		
		
		// send counter offer
		Offer counterOffer = new Offer(offer.getReceiverId(), offer.getSenderId(), options.getNextOptionsWithoutRemoving());
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(counterOffer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ Offer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
		return true;
	}
	
	/**
	 * Compute if with the offer from the other agent a better goal is achievable as before
	 * @param offer
	 * @param bbase
	 * @param goals
	 * @param bestGoal
	 * @return
	 */
	private FolFormula betterAchivableGoal(Offer offer, BaseBeliefbase bbase, Goals goals, int bestGoal){
		TreeMap<Integer, FolFormula> gList = goals.getGoalList();
		Collection<FolFormula> values = gList.descendingMap().tailMap(bestGoal).values();
		//Collection<FolFormula> values = gList.tailMap(bestGoal).values();
		//test for all better or equal goals whether they are achievable

		for(FolFormula entry: values){
			Options options = new Options();
			options.initialize(entry, bbase);
			for(Tupel<HashSet<FolFormula>, HashSet<FolFormula>> option :options.getOptions()){
				HashSet<FolFormula> promises = option.getLast();
				HashSet<FolFormula> demands = option.getFirst();
				if(promises.containsAll(offer.getDemandsAndPromises().getFirst()) && 
				   offer.getDemandsAndPromises().getFirst().containsAll(promises)){
					
					if(demands.containsAll(offer.getDemandsAndPromises().getLast())){
						return entry;
					}
				}
				
				
			}

		}
		return null;
	}

}
