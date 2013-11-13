package com.github.angerona.fw.defendingagent;


import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logics.pl.PlBeliefSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalBeliefbase;

/**
 * Data component of an agent containing a set of views on other agents.
 * @see angerona.fw.defendingagent.View for details on views.
 * 
 * @author Sebastian Homann
 */
public class ViewDataComponent extends BaseAgentComponent {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(ViewDataComponent.class);

	/** map from Agents to Views **/
	private Map<String, View> views = new HashMap<String, View>();

	/** Default Ctor */
	public ViewDataComponent() {
		super();
	}

	/**
	 * Copy Ctor: The property listeners will be registered for each target and
	 * are not copied by secrets clone method
	 */
	public ViewDataComponent(ViewDataComponent other) {
		super(other);
		for(String agent : other.views.keySet()) {
			views.put(agent, new View(other.views.get(agent)));
		}
	}

	@Override
	public ViewDataComponent clone() {
		return new ViewDataComponent(this);
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
	
	public Map<String, View> getViews() {
		return new HashMap<>(views);
	}
	
	public void setView(String agent, View view) {
		this.views.put(agent, view);
		report("Updated view of agent '" + agent +"': " + view.toString());
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		Map<String, BaseBeliefbase> bbviews = getAgent().getBeliefs().getViewKnowledge();
		for(String agent : bbviews.keySet()) {
			BaseBeliefbase bbase = bbviews.get(agent);
			PlBeliefSet beliefset;
			if(bbase instanceof ConditionalBeliefbase) {
				beliefset = new PlBeliefSet(((ConditionalBeliefbase)bbase).getPropositions());
				View view = new View(beliefset);
				this.views.put(agent, view);
				report("Added view for new agent '" + agent +"': " + view.toString());
			}
		}
	}
}