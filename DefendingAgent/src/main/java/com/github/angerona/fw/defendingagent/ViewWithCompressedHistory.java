package com.github.angerona.fw.defendingagent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.defendingagent.CompressedHistory.Triplet;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeReasoner;
import com.github.angerona.fw.util.LogicTranslator;

public class ViewWithCompressedHistory extends ViewForBeliefbaseWithKnowledge{

	//private Set<PLWithKnowledgeBeliefbase> possibleBeliefbases = new HashSet<>();


	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViewWithCompressedHistory.class);
	
	
	public ViewWithCompressedHistory(PLWithKnowledgeBeliefbase beliefbase) {
		super(beliefbase);
	}
	
	/**
	 * Default C'tor. Empty view.
	 */
	public ViewWithCompressedHistory() {
		this(new PLWithKnowledgeBeliefbase());
	}
		
	/**
	 * Copy C'tor
	 * @param other
	 */
	public ViewWithCompressedHistory(ViewWithCompressedHistory other) {
		this.view = new PLWithKnowledgeBeliefbase(other.view);
	}
	
	public Set<PLWithKnowledgeBeliefbase> calculatePossibleBeliefbases(CompressedHistory history){
		Set<PLWithKnowledgeBeliefbase> possibleBeliefbases = new HashSet<>();
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner)this.view.getAgent().getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
		Set<PropositionalFormula> phi = computePhi(reasoner);
		
		Set<PLWithKnowledgeBeliefbase> views = new HashSet<PLWithKnowledgeBeliefbase>();
		PLWithKnowledgeBeliefbase bbase;
		//create all possible beliefbases of the defender with one phi as first update
		for(PropositionalFormula p : phi){
			bbase = new PLWithKnowledgeBeliefbase();
			bbase.setKnowledge(new HashSet<PropositionalFormula>(this.view.getKnowledge()));
			LinkedList<PropositionalFormula> assertions = new LinkedList<PropositionalFormula>(this.view.getAssertions());
			assertions.removeFirst();
			assertions.addFirst(p);
			bbase.setAssertions(assertions);
			views.add(bbase);
		}
		
		//test for all formulas in all B+ and B- in the history if the beliefbase is valid 
		FOLPropTranslator translator = new FOLPropTranslator();
		
		LinkedList<Triplet<PropositionalFormula, Set<PropositionalFormula>, Set<PropositionalFormula>>> triple = history.getHistory();
		int i;
		boolean valid;
		
		for(PLWithKnowledgeBeliefbase base : views){
			PLWithKnowledgeBeliefbase b;
			valid = true;
			i=1;
			outerloop:
			for(Triplet<PropositionalFormula, Set<PropositionalFormula>, Set<PropositionalFormula>> element :triple){
				b = new PLWithKnowledgeBeliefbase(base);
				List<PropositionalFormula> assertion = (List<PropositionalFormula>) b.getAssertions().subList(0, i);
				b.setAssertions(new LinkedList<PropositionalFormula>(assertion));
				Set<FolFormula> infer = reasoner.infer(b);
				//B+
				for(PropositionalFormula f : element.answers){
					if(!infer.contains(translator.toFOL(f))){
						valid = false;
						break outerloop;
					}
				}
				//B-
				for(PropositionalFormula f : element.undefAnswers){
					if(infer.contains(translator.toFOL(f)) || infer.contains(translator.toFOL(new Negation(f)))){
						valid = false;
						break outerloop;
					}
				}
				i++;
			}
			if(valid){
				possibleBeliefbases.add(base);
			}
		}
		return possibleBeliefbases;
	}	
	
	private Set<PropositionalFormula> computePhi(PLWithKnowledgeReasoner reasoner){
		//TODO implementiern
		Iterator<Proposition> iterator = ((PropositionalSignature) this.view.getSignature()).iterator();
		HashSet<PropositionalFormula> retval = new HashSet<PropositionalFormula>();
		PropositionalFormula formula;
		while (iterator.hasNext()) {
			formula = iterator.next();
			HashSet<PropositionalFormula> a = new HashSet<PropositionalFormula>();
			a.addAll(new HashSet<PropositionalFormula>(this.view.getKnowledge()));
			a.add(formula);
			if((new PlBeliefSet(a)).isConsistent()){
				PLWithKnowledgeBeliefbase base = new PLWithKnowledgeBeliefbase();
				base.setKnowledge(new HashSet<PropositionalFormula>(view.getKnowledge()));
				LinkedList<PropositionalFormula> list = new LinkedList<PropositionalFormula>();
				list.add(formula);
				base.setAssertions(list);
				if(reasoner.infer(base).contains((new FOLPropTranslator()).toFOL(view.getAssertions().getFirst()))){
					retval.add(formula);
				}
			}			
		}
		return retval;
	}
	
	/**
	 * View Refinement: calculates the resulting view after the application of an update with 
	 * a presumed result notification.
	 * @param update An update action
	 * @param notification a fixed success/failure notification
	 * @return the resulting view after the consideration of the revision and the notification
	 */
	public ViewWithCompressedHistory RefineViewByUpdate(FolFormula update, AnswerValue notification) {
		
		PropositionalFormula q = LogicTranslator.FoToPl(update);
		ViewWithCompressedHistory newView = new ViewWithCompressedHistory(this);
		if(notification == AnswerValue.AV_TRUE) {
			newView.getView().getAssertions().addLast(q);
		} else if(notification == AnswerValue.AV_FALSE) {
			newView.getView().getAssertions().addLast(q);
		} else {
			LOG.warn("unexpected answer value in update view refinement: " + notification);
		}
		return newView;
	}
	
	public String toString() {
		return "View: \n" + this.view.toString();
	}
}
