package com.github.kreatures.defendingagent.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.defendingagent.BetterView;
import com.github.kreatures.defendingagent.CensorComponent;
import com.github.kreatures.defendingagent.CompressedHistory;
import com.github.kreatures.defendingagent.GeneralView;
import com.github.kreatures.defendingagent.HistoryComponent;
import com.github.kreatures.defendingagent.View;
import com.github.kreatures.defendingagent.ViewDataComponent;
import com.github.kreatures.defendingagent.ViewWithCompressedHistory;
import com.github.kreatures.defendingagent.ViewWithHistory;
import com.github.kreatures.gui.view.ListViewColored;
import com.github.kreatures.core.internal.Entity;

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
					reval.add("   Assertions = " + currentView.getView().getAssertions());
					reval.add("   sceptical inferences: ");
					List<FolFormula> inf = cexec.scepticalInferences(currentView);
					for(FolFormula fol : inf) {
						reval.add("    " + fol.toString());					
					}
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
