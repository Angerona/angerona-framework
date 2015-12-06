package com.github.kreaturesfw.core.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.github.kreaturesfw.core.basic.AgentComponent;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.util.Utility;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

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
			for(AgentComponent alreadyAdded : customComponents) {
				if(alreadyAdded != component) {
					alreadyAdded.componentAdded(component);
					component.componentAdded(alreadyAdded);
				}
			}
		}
		return reval;
	}
	
	/**
	 * Searches a component thats type equals the given type parameter, that means
	 * components that are sub-classes of the given type parameter are not found by
	 * this method.
	 * @param cls	The type of the component hat the method shall return
	 * @return	An instance of the agent component or null if such a component is not
	 * 			registered.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AgentComponent> T getComponent(Class<? extends T> cls) {
		for (AgentComponent ea : customComponents) {
			if (ea.getClass().equals(cls))
				return (T) ea;
		}
		return null;
	}
	
	/**
	 * Searches a component of the given class or a sub class of the given component class.
	 * This method assumes that there is only one component of the given type or one of its
	 * sub-types registered as agent-component.
	 * If there are multiple components that are of the given type or a sub-type the behavior
	 * of this method is undefined, but it shall return that component that has been registered
	 * first.
	 * @param cls	The type of the component that the method shall return
	 * @return		An instance of the agent component or null if such a component is not
	 * 				registered.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AgentComponent> T getComponentOrSub(Class<? extends T> cls) {
		for(AgentComponent ea : customComponents) {
			Class<?> cur = ea.getClass();
			while(cur != null && !cur.equals(cls)) {
				cur = cur.getSuperclass();
			}
			if(cur != null) {
				return (T) ea;
			}
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
	
	/**
	 * This method adds the given formula to the world knowledge and every
	 * view such that it represents global knowledge.
	 * @param formula	A FOL formula representing the global knowledge
	 */
	public void addGlobalKnowledge(FolFormula formula) {
		worldKnowledge.addKnowledge(formula);
		for(BaseBeliefbase bb : viewKnowledge.values()) {
			bb.addKnowledge(formula);
		}
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
