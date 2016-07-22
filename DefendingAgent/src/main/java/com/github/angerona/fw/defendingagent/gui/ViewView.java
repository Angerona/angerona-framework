package com.github.angerona.fw.defendingagent.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.semantics.NicePossibleWorld;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalPredicate;

import com.github.angerona.fw.defendingagent.BetterView;
import com.github.angerona.fw.defendingagent.CensorComponent;
import com.github.angerona.fw.defendingagent.CompressedHistory;
import com.github.angerona.fw.defendingagent.GeneralView;
import com.github.angerona.fw.defendingagent.HistoryComponent;
import com.github.angerona.fw.defendingagent.View;
import com.github.angerona.fw.defendingagent.ViewDataComponent;
import com.github.angerona.fw.defendingagent.ViewWithCompressedHistory;
import com.github.angerona.fw.defendingagent.ViewWithHistory;
import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.plwithknowledge.logic.ModelTupel;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeReasoner;

/**
 * This class extends the ListViewColored View to report all extended views
 * currently held by the agent about other agents.
 * For each agent that we are currently aware of the list contains the three
 * components of a view, namely the set of positive conditionals, the set of negative
 * conditionals and the set of propositions.
 * Further for each view, the output contains a list of all literals, that may be
 * infered from this view using the klmlean inference system.
 * 
 * @author Sebastian Homann
 */
public class ViewView extends ListViewColored {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		List<String> reval = new LinkedList<>();
		if(obj instanceof ViewDataComponent) {
			ViewDataComponent viewComponent = (ViewDataComponent)obj;
			CensorComponent cexec = viewComponent.getAgent().getComponent(CensorComponent.class);
			PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) viewComponent.getAgent().getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
			Map<String, GeneralView> views = viewComponent.getViews();
			for(String agent : views.keySet()) {
				if(views.get(agent) instanceof View){
					View currentView = (View)views.get(agent);
					reval.add("View on agent " + agent + ": <B+, B-, C> with");
					reval.add("   B+ = " + currentView.getPositiveConditionalBeliefs());
					reval.add("   B- = " + currentView.getNegativeConditionalBeliefs());
					reval.add("   C  = " + currentView.getBeliefSet());
					reval.add("   sceptical inferences: ");
					List<FolFormula> inf = cexec.scepticalInferences(currentView);
					for(FolFormula fol : inf) {
						reval.add("    " + fol.toString());					
					}
				}else if(views.get(agent) instanceof ViewWithHistory){
					ViewWithHistory currentView = (ViewWithHistory)views.get(agent);
					reval.add("View on agent " + agent + ": <Knowledge, Assertions> with");
					reval.add("   Knowledge  = " + currentView.getView().getKnowledge());
					reval.add("   Assertions = " + currentView.getView().getAssertions());
					reval.add("   sceptical inferences: ");
					List<FolFormula> inf = cexec.scepticalInferences(currentView);
					for(FolFormula fol : inf) {
						reval.add("    " + fol.toString());					
					}
				}else if(views.get(agent) instanceof ViewWithCompressedHistory){
					ViewWithCompressedHistory currentView = (ViewWithCompressedHistory)views.get(agent);
					reval.add("View on agent " + agent + ": <Knowledge, Assertions> with");
					reval.add("   Knowledge  = " + currentView.getView().getKnowledge());
					reval.add("   Assertions = " + currentView.getView().getAssertions());
					reval.add("   sceptical inferences: ");
					CompressedHistory history = (CompressedHistory)viewComponent.getAgent().getComponent(HistoryComponent.class).getHistories().get(agent);
					List<FolFormula> inf = cexec.scepticalInferences(currentView, history);
					for(FolFormula fol : inf) {
						reval.add("    " + fol.toString());					
					}
				}else if(views.get(agent) instanceof BetterView){
					BetterView currentView = (BetterView)views.get(agent);
					reval.add("View on agent " + agent + ": <Knowledge, Assertions> with");
					reval.add("   Knowledge  = " + currentView.getView().getKnowledge());
					//reval.add("   Assertions = " + currentView.getView().getAssertions());
					reval.add("   Identity and Price: ");
					PLWithKnowledgeBeliefbase base = (PLWithKnowledgeBeliefbase) currentView.getAgent().getBeliefs().getWorldKnowledge();
					PLWithKnowledgeReasoner reason = (PLWithKnowledgeReasoner) base.getReasoningOperator().getImplementation();
					
					boolean ca = true,cb = true, p1 = true, p2 = true;
					for(NicePossibleWorld world: reason.getModels(base).getModels()){
						if(!world.satisfies(new Proposition("CA"))){
							ca = false;
						}
						if(!world.satisfies(new Proposition("CB"))){
							cb = false;
						}
						if(!world.satisfies(new Proposition("PR1"))){
							p1 = false;
						}
						if(!world.satisfies(new Proposition("PR2"))){
							p2 = false;
						}
					}
					if(ca){
						reval.add("    " + "CA");
					}
					if(cb){
						reval.add("    " + "CB");
					}
					if(p1){
						reval.add("    " + "PR1");
					}
					if(p2){
						reval.add("    " + "PR2");
					}
					
//					PLWithKnowledgeBeliefbase bbase = new PLWithKnowledgeBeliefbase();
//					bbase.setKnowledge(currentView.getView().getKnowledge());
//					bbase.setAssertions(new LinkedList<>(new Conjunction(currentView.getView().getAssertions())));
//					ModelTupel models = reasoner.getModels(bbase);
//					//List<FolFormula> inf = cexec.scepticalInferences(currentView);
//					for(NicePossibleWorld model : models.getModels()) {
//						reval.add("    " + model.toString());					
//					}
				}
			}
		}
		return reval;
	}

	@Override
	public Class<? extends ViewDataComponent> getObservedType() {
		return ViewDataComponent.class;
	}

}
