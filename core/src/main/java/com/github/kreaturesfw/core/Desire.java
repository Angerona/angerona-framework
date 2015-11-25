package com.github.kreaturesfw.core;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.util.Utility;

/**
 * This class represents a desire.
 * 
 * @author Tim Janus
 */
public class Desire implements Comparable<Desire>{
	Logger LOG = LoggerFactory.getLogger(Desire.class);
	
	/** tweety atom representing the desire */
	private FolFormula formula;
	
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
	
	public Desire(Desire other) {
		this.formula = other.formula.clone();
		this.perception = other.perception;
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
		return formula != null ? formula.toString() : "";
	}
	
	@Override 
	public boolean equals(Object other) {
		if(this == other) 	return true;
		if(other == null || other.getClass() != this.getClass()) return false;
		
		Desire od = (Desire)other;
		if(!Utility.equals(this.formula, od.formula))			return false;
		if(!Utility.equals(this.perception, od.perception))		return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (this.formula.hashCode() +
				(this.perception == null ? 0 : this.perception.hashCode())) * 11;
	}

	@Override
	public int compareTo(Desire other) {
		return this.toString().compareTo(other.toString());
	}
}
