package com.github.kreatures.defendingagent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.Agent;
import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.kreatures.plwithknowledge.logic.PLWithKnowledgeReasoner;
import com.github.kreatures.core.util.LogicTranslator;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

public class BetterView implements GeneralView{

	public class Tupel<T, U>
	{
	   T knowledge;
	   U assertions;

	   Tupel(T a, U b)
	   {
		knowledge = a;
	    assertions = b;
	   }

	   public T getKnowledge(){ return knowledge;}
	   public U getAssertions(){ return assertions;}
	}
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(BetterView.class);
	
	private Tupel<Set<PropositionalFormula>, Set<PropositionalFormula>> view;
	
	private Agent ag;
	
	public BetterView(PLWithKnowledgeBeliefbase view) {
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) view.getReasoningOperator().getImplementation();
		Set<FolFormula> set = reasoner.infer(view);
		FOLPropTranslator translator = new FOLPropTranslator();
		Set<PropositionalFormula> assertions = new HashSet<PropositionalFormula>();
		for(FolFormula formula: set){
			assertions.add(translator.toPropositional(formula));
		}
		this.view = new Tupel<Set<PropositionalFormula>, Set<PropositionalFormula>>(view.getKnowledge(), assertions);
		ag = view.getAgent();
	}
	
	public BetterView(Set<PropositionalFormula> k, Set<PropositionalFormula> a, Agent ag){
		view = new Tupel<Set<PropositionalFormula>, Set<PropositionalFormula>>(k, a);
		this.ag = ag;
	}
	
	/**
	 * Default C'tor. Empty view.
	 */
	public BetterView() {
		this(new PLWithKnowledgeBeliefbase());
	}
	
	/**
	 * Copy C'tor
	 * @param other
	 */
	public BetterView(BetterView other) {
		this(new HashSet<PropositionalFormula>(other.view.knowledge), new HashSet<PropositionalFormula>(other.view.getAssertions()), other.ag);
	}
	
	/**
	 * View refinement: calculates the resulting view after a given query with a certain answer value.
	 * @param query A query action 
	 * @param av a fixed answer value
	 * @return the resulting view after the consideration of the query and the answer value
	 */
	public BetterView MentalRefineViewByQuery(FolFormula query, AnswerValue av) {
		PropositionalFormula q = LogicTranslator.FoToPl(query);
		BetterView newView = new BetterView(this);
		
		if(av == AnswerValue.AV_TRUE) {
			
			newView = conclude(newView, q, true);
			System.out.println(newView.view.assertions);
		} else if(av == AnswerValue.AV_FALSE) {
			newView = conclude(newView, new Negation(q), true);
			System.out.println(newView.view.assertions);
		} else if(av == AnswerValue.AV_UNKNOWN) {
			//do nothing
		} else {
			LOG.warn("unexpected answer value in query view refinement: " + av);
		}
		
		return newView;
	}
	
	/**
	 * View refinement: calculates the resulting view after a given query with a certain answer value.
	 * @param query A query action 
	 * @param av a fixed answer value
	 * @return the resulting view after the consideration of the query and the answer value
	 */
	public BetterView RefineViewByQuery(FolFormula query, AnswerValue av) {
		PropositionalFormula q = LogicTranslator.FoToPl(query);
		BetterView newView = new BetterView(this);
		
		if(av == AnswerValue.AV_TRUE) {
			newView.view.assertions.add(q);
		} else if(av == AnswerValue.AV_FALSE) {
			newView.view.assertions.add(new Negation(q));
		} else if(av == AnswerValue.AV_UNKNOWN) {
			//do nothing
		} else {
			LOG.warn("unexpected answer value in query view refinement: " + av);
		}
		
		return newView;
	}
	
	/**
	 * View Refinement: calculates the resulting view after the application of an update with 
	 * a presumed result notification.
	 * @param update An update action
	 * @param notification a fixed success/failure notification
	 * @return the resulting view after the consideration of the revision and the notification
	 */
	public BetterView RefineViewByUpdate(FolFormula revision, AnswerValue notification) {
		
		PropositionalFormula q = LogicTranslator.FoToPl(revision);
		BetterView newView = new BetterView(this);
		if(notification == AnswerValue.AV_TRUE) {
			newView = conclude(newView, q, false);
		} else if(notification == AnswerValue.AV_FALSE) {
			//do nothing
		} else {
			LOG.warn("unexpected answer value in update view refinement: " + notification);
		}
		
		return newView;
	}
	
	public String toString() {
		return "View: \n" + this.view.toString();
	}
	
	
	private BetterView conclude(BetterView v, PropositionalFormula formula, boolean a){
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) ag.getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
		PLWithKnowledgeBeliefbase bbase = new PLWithKnowledgeBeliefbase();
		LinkedList<PropositionalFormula> list = new LinkedList<>();
		if(a){
			list = new LinkedList<>(v.view.assertions);
		}else{
			list.add(new Conjunction(v.view.assertions));
		}
		list.addLast(formula);
		bbase.setAssertions(list);
		bbase.setKnowledge(v.view.knowledge);
		Set<FolFormula> l = reasoner.infer(bbase);
		
		FOLPropTranslator translator = new FOLPropTranslator();
		Set<PropositionalFormula> assertions = new HashSet<PropositionalFormula>();
		for(FolFormula f: l){
			assertions.add(translator.toPropositional(f));
		}
		return new BetterView(v.view.getKnowledge(), assertions, ag);
	}
	
	
	@Override
	public PropositionalSignature getSignature() {
		PropositionalSignature sig = new PropositionalSignature();
		for(PropositionalFormula fol: view.assertions){
			sig.add(fol.getSignature());
		}
		for(PropositionalFormula fol : view.knowledge){
			sig.add(fol.getSignature());
		}
		return sig;
	}
	
	public Agent getAgent(){
		return ag;
	}
	public Tupel<Set<PropositionalFormula>, Set<PropositionalFormula>> getView(){
		return view;
	}

}
