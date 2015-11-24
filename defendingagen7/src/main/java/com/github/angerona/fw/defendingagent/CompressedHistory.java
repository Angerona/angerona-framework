package com.github.angerona.fw.defendingagent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.comm.Update;
import com.github.angerona.fw.logic.AnswerValue;

public class CompressedHistory implements GeneralHistory{

	class Triplet<T, U, V>
	{
	   T update;
	   U answers;
	   V undefAnswers;

	   Triplet(T a, U b, V c)
	   {
	    update = a;
	    answers = b;
	    undefAnswers = c;
	   }

	   T getUpdate(){ return update;}
	   U getAnswers(){ return answers;}
	   V getUndefAnswers(){ return undefAnswers;}
	}
	
	public CompressedHistory() {}
	
	/** Copy Ctor */
	public CompressedHistory(CompressedHistory other) {
		
		
		Iterator<Triplet<PropositionalFormula, 
		   Set<PropositionalFormula>,
		   Set<PropositionalFormula>>> iterator = other.history.iterator();
		while(iterator.hasNext()) {
			Triplet<PropositionalFormula, 
			   Set<PropositionalFormula>,
			   Set<PropositionalFormula>> tripel = iterator.next();
			
			HashSet<PropositionalFormula> answers = new HashSet<PropositionalFormula>();
			for(PropositionalFormula entry : tripel.getAnswers()) {
				answers.add(entry.clone());
			}
			
			HashSet<PropositionalFormula> udefAnswers = new HashSet<PropositionalFormula>();
			for(PropositionalFormula entry : tripel.getUndefAnswers()) {
				udefAnswers.add(entry.clone());
			}
			
			history.add(new Triplet<PropositionalFormula, 
					   Set<PropositionalFormula>,
					   Set<PropositionalFormula>>(tripel.getUpdate().clone(), answers, udefAnswers));
		}
	}
	
	private LinkedList<Triplet<PropositionalFormula, 
	 						   Set<PropositionalFormula>,
	 						   Set<PropositionalFormula>>> history = new LinkedList<Triplet<PropositionalFormula, 
							   														Set<PropositionalFormula>, 
							   														Set<PropositionalFormula>>>(); 
	
	
	/**
	 * Puts the given perception to the history
	 * @param perception
	 * @param answer value of the answer to the perception
	 */

	public void putAction(Perception perception, AnswerValue answer) {
		if(perception instanceof Query) {
			//If the perception is a Query the sets of the triplet will be updatet dependent on the AnswerValue
			if(answer == AnswerValue.AV_UNKNOWN){
				Query query = ((Query) perception);
				FOLPropTranslator translator = new FOLPropTranslator();
				PropositionalFormula formula = translator.toPropositional(query.getQuestion());
				history.getLast().getUndefAnswers().add(formula);
			}else if(answer == AnswerValue.AV_TRUE){
				Query query = ((Query) perception);
				FOLPropTranslator translator = new FOLPropTranslator();
				PropositionalFormula formula = translator.toPropositional(query.getQuestion());
				history.getLast().getAnswers().add(formula);
			}else if(answer == AnswerValue.AV_FALSE){
				Query query = ((Query) perception);
				FOLPropTranslator translator = new FOLPropTranslator();
				PropositionalFormula formula = translator.toPropositional(query.getQuestion());
				formula = ((PropositionalFormula)new Negation(formula));
				history.getLast().getAnswers().add(formula);
			}
		}else if(perception instanceof Update){
			if(answer == AnswerValue.AV_TRUE){
				//If the perception is a successful Update a new triplet is added to the history
				Update update = (Update) perception;
				FOLPropTranslator translator = new FOLPropTranslator();
				PropositionalFormula formula = translator.toPropositional(update.getProposition());
				Triplet<PropositionalFormula, 
						Set<PropositionalFormula>, 
						Set<PropositionalFormula>> triplet = new Triplet<PropositionalFormula, 
																					Set<PropositionalFormula>, 
																					Set<PropositionalFormula>>(formula, 
																														  new HashSet<PropositionalFormula>(), 
																														  new HashSet<PropositionalFormula>());
				history.add(triplet);
			}
			
		}
	}
	
	public LinkedList<Triplet<PropositionalFormula, 
	   Set<PropositionalFormula>,
	   Set<PropositionalFormula>>> getHistory(){
		return history;
	}
	
	public void init(PropositionalFormula upd){
		Triplet<PropositionalFormula, 
		Set<PropositionalFormula>, 
		Set<PropositionalFormula>> triplet = new Triplet<PropositionalFormula, 
																	Set<PropositionalFormula>, 
																	Set<PropositionalFormula>>(upd, 
																										  new HashSet<PropositionalFormula>(), 
																										  new HashSet<PropositionalFormula>());
		this.history.add(triplet);
	}
	
	public String toString(){
		String reval = "History: ";
		for(Triplet<PropositionalFormula, Set<PropositionalFormula>, Set<PropositionalFormula>> t : history){
			reval += "< Update: " + t.update + ", B+: " + t.getAnswers() +", B-: " + t.getUndefAnswers() + ">";
 		}
		return reval;
	}
}
