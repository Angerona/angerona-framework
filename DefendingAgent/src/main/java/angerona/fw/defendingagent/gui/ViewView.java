package angerona.fw.defendingagent.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import angerona.fw.defendingagent.CensorComponent;
import angerona.fw.defendingagent.View;
import angerona.fw.defendingagent.ViewComponent;
import angerona.fw.gui.view.ListViewColored;
import angerona.fw.internal.Entity;

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
		if(obj instanceof ViewComponent) {
			CensorComponent cexec = new CensorComponent();
			
			ViewComponent viewComponent = (ViewComponent)obj;
			Map<String, View> views = viewComponent.getViews();
			for(String agent : views.keySet()) {
				View currentView = views.get(agent);
				reval.add("View on agent " + agent + ": <B+, B-, C> with");
				reval.add("   B+ = " + currentView.getPositiveConditionalBeliefs());
				reval.add("   B- = " + currentView.getNegativeConditionalBeliefs());
				reval.add("   C  = " + currentView.getBeliefSet());
				reval.add("   sceptical inferences: ");
				List<FolFormula> inf = cexec.scepticalInferences(currentView);
				for(FolFormula fol : inf) {
					reval.add("    " + fol.toString());					
				}
			}
		}
		return reval;
	}

	@Override
	public Class<? extends ViewComponent> getObservedType() {
		return ViewComponent.class;
	}

}
