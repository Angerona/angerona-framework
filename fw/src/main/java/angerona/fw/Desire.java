package angerona.fw;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;

/**
 * This class represents a desire as complex object.
 * In Angerona a desire might be linked to a plan, this object is responsible of
 * keeping track of those links.
 * 
 * @author Tim Janus
 */
public class Desire {
	/** tweety atom representing the desire */
	private Atom atom;
	
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
	public Desire(Atom desire) {
		this(desire, null);
	}
	
	public Desire(Atom desire, Perception reason) {
		this.atom = desire;
		this.perception = reason;
	}
	
	/**
	 * Sets the given plan as actual linked plan of the desire
	 * @param plan reference to a plan
	 */
	void setPlan(Subgoal plan) {
		this.plan = plan;
	}
	
	/** @return true if a plan is linked to the desire, false otherwise. */
	boolean hasPlan() {
		return plan != null;
	}
	
	/** @return reference to the plan linked to the desire, this might be null */
	public Subgoal getPlan() {
		return plan;
	}
	
	/** @return tweety representation of the desire */
	public Atom getDesire() {
		return atom;
	}

	public Perception getPerception() {
		return perception;
	}
	
	@Override
	public String toString() {
		return atom.toString();
	}
	
	@Override 
	public boolean equals(Object other) {
		if(other == null)	return false;
		
		if(other instanceof Atom) {
			return this.atom.equals((Atom)other);
		}
		
		if(other instanceof Desire) {
			Desire od = (Desire)other;
			return od.atom.equals(this.atom);
		}
		
		return false;
	}
}
