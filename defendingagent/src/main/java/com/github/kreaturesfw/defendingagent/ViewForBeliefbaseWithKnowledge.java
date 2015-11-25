package com.github.kreaturesfw.defendingagent;

import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;

import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * The view for Agent with the PLWithKnowledgeBeliefbase as Beliefbase.
 * The view is also a PLWithKnowledgeBeliefbase.
 * 
 * @author Pia Wierzoch
 */
public class ViewForBeliefbaseWithKnowledge implements GeneralView{
	
	protected PLWithKnowledgeBeliefbase view;

	public ViewForBeliefbaseWithKnowledge(){
		view = new PLWithKnowledgeBeliefbase();
	}
	
	public ViewForBeliefbaseWithKnowledge(PLWithKnowledgeBeliefbase beliefbase){
		this.view = beliefbase;
	}
	
	@Override
	public PropositionalSignature getSignature() {
		return (PropositionalSignature) view.getSignature();
	}
	
	public void setView(PLWithKnowledgeBeliefbase view){
		this.view = view;
	}
	
	public PLWithKnowledgeBeliefbase getView(){
		return view;
	}

}
