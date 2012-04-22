package angerona.fw.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * One instance of this class represents all beliefs of an agent.
 * @author Tim Janus
 */
public class Beliefs implements Cloneable
{
	/** the world knowledge of the agent (what the agent knows) */
	private BaseBeliefbase worldKnowledge;
	
	/** a list of views on other agents knowledge (what the agent beliefs about the knowledge of the other agents) */
	private Map<String, BaseBeliefbase> viewKnowledge = new HashMap<String, BaseBeliefbase>();
	
	/** knowledge about confidential informations */
	private BaseBeliefbase confidentialKnowledge;
	
	/**
	 * Ctor: generates agent beliefs with the given world, view and confidential knowledge.
	 * @param world			reference to a belief base representing the world knowledge.
	 * @param otherAgents	reference to a map from agent names to belief bases representing the view on the other agents
	 */
	public Beliefs(BaseBeliefbase world, Map<String, BaseBeliefbase> otherAgents) {
		worldKnowledge = world;
		viewKnowledge.putAll(otherAgents);
	}
	
	/**
	 * Ctor: Used for performing a deep copy.
	 * @param toCopy	other Beliefs to copy.
	 */
	public Beliefs(Beliefs toCopy) {
		worldKnowledge = (BaseBeliefbase)toCopy.worldKnowledge.clone();
		for(String name : toCopy.viewKnowledge.keySet()) {
			viewKnowledge.put(name, (BaseBeliefbase)toCopy.getViewKnowledge().get(name).clone());
		}
	}
	
	/** @return the world knowledge of the agent (what the agent knows) */
	public BaseBeliefbase getWorldKnowledge() {
		return worldKnowledge;
	}
	
	
	/** @return knowledge about confidential informations */
	public Map<String, BaseBeliefbase> getViewKnowledge(){
		return viewKnowledge;
	}
	
	@Override
	public Object clone() {
		return new Beliefs(this);
	}
	
	@Override
	public String toString() {
		String reval = "World:\n" + worldKnowledge.toString() + "\n\n";
		reval += "Views:\n";
		for(String name : viewKnowledge.keySet()) {
			reval += name + "\n" + viewKnowledge.get(name).toString() +"\n";
		}
		reval+="\nConfidential:\n" + confidentialKnowledge.toString();
		return reval;
	}
}
