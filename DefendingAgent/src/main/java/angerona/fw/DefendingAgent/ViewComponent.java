package angerona.fw.DefendingAgent;


import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logics.propositionallogic.PlBeliefSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.conditional.ConditionalBeliefbase;

/**
 * Data component of an agent containing a set of views on other agents.
 * @see angerona.fw.DefendingAgent.View for details on views.
 * 
 * @author Sebastian Homann
 */
public class ViewComponent extends BaseAgentComponent {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(ViewComponent.class);

	/** map from Agents to Views **/
	private Map<String, View> views = new HashMap<String, View>();

	/** Default Ctor */
	public ViewComponent() {
		super();
	}

	/**
	 * Copy Ctor: The property listeners will be registered for each target and
	 * are not copied by secrets clone method
	 */
	public ViewComponent(ViewComponent other) {
		super(other);
		for(String agent : other.views.keySet()) {
			views.put(agent, new View(other.views.get(agent)));
		}
	}

	@Override
	public Object clone() {
		return new ViewComponent(this);
	}

	@Override
	public String toString() {
		String reval = "";
		for (String agent : views.keySet()) {
			reval += agent + ": " + views.get(agent);
			reval += "\n";
		}
			
		return reval;
	}
	
	public View getView(String agent) {
		return views.get(agent);
	}
	
	public void setView(String agent, View view) {
		this.views.put(agent, view);
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		Map<String, BaseBeliefbase> bbviews = getAgent().getBeliefs().getViewKnowledge();
		for(String agent : bbviews.keySet()) {
			BaseBeliefbase bbase = bbviews.get(agent);
			PlBeliefSet beliefset;
			if(bbase instanceof ConditionalBeliefbase) {
				beliefset = new PlBeliefSet(((ConditionalBeliefbase)bbase).getPropositions());
				this.views.put(agent, new View(beliefset));
			}
		}
	}
}