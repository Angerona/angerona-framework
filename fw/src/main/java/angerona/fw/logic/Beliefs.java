package angerona.fw.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import angerona.fw.AgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.util.Utility;

/**
 * One instance of this class represents all beliefs of an agent.
 * @author Tim Janus
 */
public class Beliefs implements Cloneable
{
	private int copyDepth;
	
	/** the world knowledge of the agent (what the agent knows) */
	private BaseBeliefbase worldKnowledge;
	
	/** a list of views on other agents knowledge (what the agent beliefs about the knowledge of the other agents) */
	private Map<String, BaseBeliefbase> viewKnowledge = new HashMap<String, BaseBeliefbase>();
	
	/** The set of all registered AgentComponent instances */
	private Set<AgentComponent> customComponents = new HashSet<AgentComponent>();
	
	/**
	 * Ctor: generates agent beliefs with the given world, view and confidential knowledge.
	 * @param world			reference to a belief base representing the world knowledge.
	 * @param otherAgents	reference to a map from agent names to belief bases representing the view on the other agents
	 */
	public Beliefs(BaseBeliefbase world, Map<String, BaseBeliefbase> otherAgents) {
		copyDepth = 0;
		worldKnowledge = world;
		viewKnowledge.putAll(otherAgents);
	}
	
	/**
	 * Ctor: Used for performing a deep copy.
	 * @param toCopy	other Beliefs to copy.
	 */
	public Beliefs(Beliefs toCopy) {
		worldKnowledge = (BaseBeliefbase)toCopy.worldKnowledge.clone();
		for(Entry<String, BaseBeliefbase> entry: toCopy.viewKnowledge.entrySet()) {
			viewKnowledge.put(entry.getKey(), entry.getValue().clone());
		}
		
		for(AgentComponent comp : toCopy.customComponents) {
			customComponents.add(comp.clone());
		}
		this.copyDepth = toCopy.copyDepth+1;
	}
	
	/** @return the world knowledge of the agent (what the agent knows) */
	public BaseBeliefbase getWorldKnowledge() {
		return worldKnowledge;
	}
	
	
	/** @return knowledge about confidential informations */
	public Map<String, BaseBeliefbase> getViewKnowledge(){
		return viewKnowledge;
	}
	
	public Set<AgentComponent> getComponents() {
		return Collections.unmodifiableSet(customComponents);
	}
	
	public int getCopyDepth() {
		return copyDepth;
	}
	
	@Override
	public Beliefs clone() {
		return new Beliefs(this);
	}
	
	/**
	 * adds the given component to the agent.
	 * 
	 * @param component
	 *            Reference to the component.
	 * @return true if the component was successfully added, false if a
	 *         component of the type already exists.
	 */
	public boolean addComponent(AgentComponent component) {
		if (component == null)
			throw new IllegalArgumentException();

		boolean reval = true;
		for (AgentComponent loopEa : customComponents) {
			if (component.getClass().equals(loopEa.getClass())) {
				reval = false;
				break;
			}
		}

		if (reval) {
			customComponents.add(component);
		}
		return reval;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AgentComponent> T getComponent(Class<? extends T> cls) {
		for (AgentComponent ea : customComponents) {
			if (ea.getClass().equals(cls))
				return (T) ea;
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("World:\n" + worldKnowledge.toString() + "\n\n");
		buf.append("Views:\n");
		for(String name : viewKnowledge.keySet()) {
			buf.append(name + "\n" + viewKnowledge.get(name).toString() +"\n");
		}
		return buf.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Beliefs)) 	return false;
		Beliefs co = (Beliefs)other;
		
		if(!worldKnowledge.equals(co.worldKnowledge))	return false;
		if(!viewKnowledge.equals(co.viewKnowledge))		return false;
		if(!Utility.equals(customComponents, co.customComponents))	return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return worldKnowledge.hashCode() + viewKnowledge.hashCode() + 
				customComponents.hashCode() + 3;
	}
}
