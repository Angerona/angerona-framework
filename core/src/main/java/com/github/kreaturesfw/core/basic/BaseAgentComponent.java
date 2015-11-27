package com.github.kreaturesfw.core.basic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.kreaturesfw.core.internal.AngeronaReporter;
import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.internal.IdGenerator;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.report.FullReporter;
import com.github.kreaturesfw.core.report.ReportPoster;
import com.github.kreaturesfw.core.report.Reporter;
import com.github.kreaturesfw.core.util.Utility;

/**
 * Base class for special extensions of the agent model. Every subclass
 * of BaseAgentComponent implements a special data component like 
 * Secrecy Knowledge or Know-How.
 * 
 * This base class provides the unique id concept for entities which is
 * used by the Angerona report system for example.
 * 
 * It also provides an interface to inform listeners of data components 
 * about changes of the data. So it implements an abstract model object
 * in a MVC Pattern.
 * @author Tim Janus
 *
 */
public abstract class BaseAgentComponent 
implements 	AgentComponent,
			Reporter,
			ReportPoster,
			Cloneable{
	
	/** the unique id of the parent of the component (the agent) */
	private Long parentId;
	
	/** the unique id of the agent component */
	private Long id;
	
	private FullReporter reporter = new AngeronaReporter();
	
	protected boolean initalized = false;
	
	/** 
	 * 	how deep is this instance in the copy hierachy. 0 means its original, 1 its 
	 *  a copy of the orignal and 2 that it is a copy of the copy of the original.
	 */
	private int copyDepth;
	
	/** implementation for property change */
	private PropertyChangeSupport propertyChangeSupport;
	
	/** Default Ctor: Initializes the ids and copy-depth */
	public BaseAgentComponent() {
		id = IdGenerator.generate(this);
		copyDepth = 0;
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	/** Copy Ctor: Creates the component by copying the component from the given other component */
	public BaseAgentComponent(BaseAgentComponent other) {
		this.id = other.id;
		this.parentId = other.parentId != null ? other.parentId : null;
		copyDepth = other.copyDepth + 1;
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public boolean isInitialized() {
		return initalized;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	
	/**
	 * Fires a property change event. This method is used by sub classes to inform
	 * listeners about changes of the data.
	 * @param propertyName		The name of the property which is changed
	 * @param oldValue			The old value of the property
	 * @param newValue			The new value of the property
	 */
	protected void firePropertyChangeListener(String propertyName, 
			Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	@Override
	public void setParent(Long id) {
		if(!Utility.equals(parentId, id)) {
			parentId = id;
			getAgent().addListener(this);
			reporter.setDefaultPoster(this);
			reporter.setOperatorStack(getAgent());
		}
	}

	@Override
	public Agent getAgent() {
		Entity reval = IdGenerator.getEntityWithId(parentId);
		if(reval != null) {
			return (Agent)reval;
		}
		return null;
	}
	
	@Override
	public void report(String msg) {
		reporter.report(msg, (Entity)this);
	}
	
	@Override
	public void report(String msg, Entity attachment) {
		reporter.report(msg, attachment);
	}
		
	@Override
	public void init(Map<String, String> additionalData) { 
		initalized = true;
	}
	
	@Override
	public Long getGUID() {
		return id;
	}

	@Override
	public Long getParent() {
		return parentId;
	}

	@Override
	public List<Long> getChilds() {
		return new LinkedList<Long>();
	}
	
	@Override
	public int getCopyDepth() {
		return copyDepth;
	}
	
	@Override
	public abstract BaseAgentComponent clone();
	
	@Override
	public void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs) {
		// does nothing in default case, sub classes may override it.	
	}
	
	@Override
	public void beliefbaseChanged(BaseBeliefbase bb, Perception percept, String space) {
		// does nothing in default case, sub classes may override it.
	}
	
	@Override
	public void componentAdded(AgentComponent comp) {
		// does nothing
	}

	@Override
	public void componentRemoved(AgentComponent comp) {
		// does nothing
	}
	
	@Override
	public void componentInitialized(AgentComponent comp) {
		// does nothing
	}
	
	@Override
	public String getPosterName() {
		return getClass().getSimpleName();
	}
}
