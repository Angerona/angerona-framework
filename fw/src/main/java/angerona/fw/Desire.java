package angerona.fw;

import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.util.Utility;

/**
 * This class represents a desire as complex object.
 * In Angerona a desire might be linked to a plan, this object is responsible of
 * keeping track of those links.
 * 
 * @author Tim Janus
 */
public class Desire {
	Logger LOG = LoggerFactory.getLogger(Desire.class);
	
	/** tweety atom representing the desire */
	private FolFormula formula;
	
	/** plan as subgoal */
	private Subgoal plan;
	
	/** the perception which initialized the desire, this might be null */
	private Perception perception;
	
	/** Default Ctor: Initialize plan and atom with null */
	public Desire() {}
	
	/**
	 * Ctor which sets the logical representation of the desire
	 * @param desire	a tweety atom representing the desire.
	 */
	public Desire(FolFormula desire) {
		this(desire, null);
	}
	
	public Desire(FolFormula desire, Perception reason) {
		this.formula = desire;
		this.perception = reason;
	}
	
	/**
	 * Sets the given plan as actual linked plan of the desire
	 * @param plan reference to a plan
	 * @deprecated
	 */
	void setPlan(Subgoal plan) {
		this.plan = plan;
	}
	
	/** 
	 * @deprecated
	 * @return true if a plan is linked to the desire, false otherwise. 
	 * */
	boolean hasPlan() {
		return plan != null;
	}
	
	/** 
	 * @deprecated
	 * @return reference to the plan linked to the desire, this might be null
	 */
	public Subgoal getPlan() {
		return plan;
	}
	
	/** @return tweety representation of the desire */
	public FolFormula getFormula() {
		return formula;
	}

	public Perception getPerception() {
		return perception;
	}
	
	@Override
	public String toString() {
		return formula.toString();
	}
	
	@Override 
	public boolean equals(Object other) {
		if(! (other instanceof Desire))	
			return false;
		
		Desire od = (Desire)other;
		if(!Utility.equals(this.formula, od.formula))	
			return false;
		
		if(!Utility.equals(this.perception, od.perception))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.formula.hashCode() +
				(this.perception == null ? 0 : this.perception.hashCode());
	}
}
