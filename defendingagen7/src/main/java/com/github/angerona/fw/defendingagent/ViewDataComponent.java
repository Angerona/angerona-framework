package com.github.angerona.fw.defendingagent;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.tweety.logics.pl.PlBeliefSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalBeliefbase;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;

/**
 * Data component of an agent containing a set of views on other agents.
 * @see angerona.fw.defendingagent.View for details on views.
 * @see angerona.fw.defendingagent.ViewWithCompressedHistory for detail on views.
 * @see angerona.fw.defendingagent.ViewWithHistory for detail on views.
 * 
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ViewDataComponent extends BaseAgentComponent {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(ViewDataComponent.class);

	/** map from Agents to Views **/
	private Map<String, GeneralView> views = new HashMap<String, GeneralView>();

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
		Iterator<String> iterator = other.views.keySet().iterator();
		String s = iterator.next();
		GeneralView v = other.views.get(s);
		if(v instanceof View){
			views.put(s, new View((View)v));
			while(iterator.hasNext()){
				String agent = iterator.next();
				views.put(agent, new View((View)other.views.get(agent)));
			}
		}else if(v instanceof ViewWithCompressedHistory){
			views.put(s, new ViewWithCompressedHistory((ViewWithCompressedHistory)v));
			while(iterator.hasNext()){
				String agent = iterator.next();
				views.put(agent, new ViewWithCompressedHistory((ViewWithCompressedHistory)other.views.get(agent)));
			}
		}else if(v instanceof ViewWithHistory){
			views.put(s, new ViewWithHistory((ViewWithHistory)v));
			while(iterator.hasNext()){
				String agent = iterator.next();
				views.put(agent, new ViewWithHistory((ViewWithHistory)other.views.get(agent)));
			}
		}else if(v instanceof BetterView){
			views.put(s, new BetterView((BetterView)v));
			while(iterator.hasNext()){
				String agent = iterator.next();
				views.put(agent, new BetterView((BetterView)other.views.get(agent)));
			}
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
	
	public GeneralView getView(String agent) {
		return views.get(agent);
	}
	
	public Map<String, GeneralView> getViews() {
		return new HashMap<>(views);
	}
	
	public void setView(String agent, GeneralView view) {
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
			}else if(bbase instanceof PLWithKnowledgeBeliefbase){
				if(additionalData.containsKey("View")){
					String str = additionalData.get("View");
					if(str.equals("ViewCompressed")){
						ViewWithCompressedHistory view = new ViewWithCompressedHistory((PLWithKnowledgeBeliefbase)bbase);
						this.views.put(agent, view);
						report("Added view for new agent '" + agent +"': " + view.toString());
					}else if(str.equals("ViewHistory")){
						ViewWithHistory view = new ViewWithHistory((PLWithKnowledgeBeliefbase)bbase);
						this.views.put(agent, view);
						report("Added view for new agent '" + agent +"': " + view.toString());
					}else if(str.equals("BetterView")){
						BetterView view = new BetterView((PLWithKnowledgeBeliefbase)bbase);
						this.views.put(agent, view);
						report("Added view for new agent '" + agent +"': " + view.toString());
					}
				}
			}
		}
	}
}