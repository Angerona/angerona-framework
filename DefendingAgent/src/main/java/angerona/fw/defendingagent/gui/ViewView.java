package angerona.fw.defendingagent.gui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.defendingagent.View;
import angerona.fw.defendingagent.ViewComponent;
import angerona.fw.gui.view.ListViewColored;
import angerona.fw.internal.Entity;
import angerona.fw.logic.Desires;

/**
 * This class extends the default Beliefbase View to an ASP specific belief base
 * view. It outputs the answer sets for a specific reasoner using the ASPReasoner
 * interface specific processAnswerSets method.
 * It outputs the answer sets in the middle between the ELP (belief base) and
 * the set of inferred formulas.
 * @author Tim Janus
 */
public class ViewView extends ListViewColored<ViewComponent> {

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof ViewComponent) {
			List<String> reval = new LinkedList<>();
			ViewComponent viewComponent = (ViewComponent)obj;
			Map<String, View> views = viewComponent.getViews();
			for(String agent : views.keySet()) {
				View currentView = views.get(agent);
				reval.add("View on agent " + agent + ": <B+, B-, C> with");
				reval.add(" B+ = " + currentView.getPositiveConditionalBeliefs());
				reval.add(" B- = " + currentView.getNegativeConditionalBeliefs());
				reval.add(" C  = " + currentView.getBeliefSet());
				reval.add("sceptical inferences: ");
				
			}
			
//			List<String> reval = Arrays.asList(views.toString().split("\n"));
//			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends ViewComponent> getObservedType() {
		return ViewComponent.class;
	}

}
