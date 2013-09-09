package com.github.angerona.fw.am.secrecy.operators;

import com.github.angerona.fw.logic.Beliefs;


/** 
 *	A data-structure containing information about the result of a violation processing.
 *	The flag alright is used to determine if a secret has to be weaken or if no secrecy violation occurs.
 *	The attribute beliefs is used to store the beliefs that were used to process the violates result.
 *
 *	The ViolatesResult data-structure can be combined with each other to bind violates result togehter that
 *	reflect multiple steps in a plan etc.
 *
 *	@author Tim Janus, 
 *  @author Daniel Dilger
 */
public class ViolatesResult implements Cloneable {
	/** flag indicating if everything is alright (no secret is violated) */
	private boolean alright;
	
	/** the beliefs used that generated this ViolatesResult */
	private Beliefs beliefs;
	
	/** Default Ctor: Assumes that everything is alright (no violation occurs) */
	public ViolatesResult() {
		this(true);
	}
	
	/** Copy-Ctor */
	public ViolatesResult(ViolatesResult other) {
		this.alright = other.alright;
	}
	
	/**
	 * Set the alright flag but dont save more sophisticated information about secret weakening
	 * @param alright	flag indicating if an violation occured.
	 */
	public ViolatesResult(boolean alright) {
		this.alright = alright;
	}
	
	/** @return true if no secret was weaken */
	public boolean isAlright() {
		return alright;
	}
	
	/**
	 * combines two ViolatesResult to form one the beliefs of the violates result given
	 * as parameter are used because this method assumes the parameter is newer than this.
	 * 
	 * Internally the boolean flag of both violates results are combined with the logical
	 * and operator.
	 * 
	 * @see angerona.fw.am.secrecy.Secret
	 * @param newer		Reference to the other ViolatesResult to combine.
	 * @return	The combination of this ViolatesResult with one given as parameter.
	 */
	public ViolatesResult combine(ViolatesResult newer) {
		ViolatesResult reval = new ViolatesResult(alright && newer.alright);
		reval.beliefs = newer.beliefs;
		return reval;
	}
	
	/**
	 * Changes the beliefs saved in this violates result structure
	 * @param beliefs	Reference to the new beliefs saved in this violates result structure
	 */
	public void setBeliefs(Beliefs beliefs) {
		this.beliefs = beliefs;
	}
	
	/**
	 * @return The beliefs that were used to process this violates result structure.
	 */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	@Override
	public Object clone() {
		return new ViolatesResult(this);
	}
}
