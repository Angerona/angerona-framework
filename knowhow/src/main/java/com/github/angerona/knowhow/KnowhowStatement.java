package com.github.angerona.knowhow;

import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;

/**
 * Class represents a KnowhowStatement like the one defined in Thimm, Krümpelmann 2009.
 * @author Tim Janus
 *
 */
public class KnowhowStatement {
	
	/** the id of the knowhow-statement (useable as index) */
	private int id;
	
	/** the target of the knowhow represent as an elp atom. */
	private DLPAtom target;
	
	/** sub targets of the knowhow-statement, this might be skills or other knowhow statements */
	private Vector<DLPAtom> subTargets = new Vector<DLPAtom>();
	
	/** conditions which have to be true in the beliefbase of the agent */
	private Vector<DLPAtom> conditions = new Vector<DLPAtom>();
	
	private double weight;
	
	private double irrelevance;
	
	/** internal name of the knowhow statement */
	String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	public KnowhowStatement(DLPAtom target, Vector<DLPAtom> subTargets, Vector<DLPAtom> conditions) {
		this(target, subTargets, conditions, 0, 0);
	}
		
	public KnowhowStatement(DLPAtom target, Vector<DLPAtom> subTargets, Vector<DLPAtom> conditions, 
			double weight, double irrelevance) {
		id = counter;
		name = "kh_stmt_"+id;
		++counter;
		
		this.target = target;
		this.subTargets.addAll(subTargets);
		this.conditions.addAll(conditions);
		this.weight = 0;
		this.irrelevance = 0;
	}
	
	/** @return	the id of the knowhow-statement (useable as index) */
	public int getId() {
		return id;
	}
	
	public DLPAtom getTarget() {
		return target;
	}
	
	public Vector<DLPAtom> getSubTargets() {
		return subTargets;
	}
	
	public Vector<DLPAtom> getConditions() {
		return conditions;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof KnowhowStatement))	return false;
		KnowhowStatement o = (KnowhowStatement)other;
		
		return 	target.equals(o.target) &&
				subTargets.equals(o.subTargets) &&
				conditions.equals(o.conditions);
	}
	
	@Override
	public int hashCode() {
		return (target.hashCode() + subTargets.hashCode() + conditions.hashCode()) * 13;
	}
	
	@Override
	public String toString() {
		String reval = "(" + target.toString() + ", " + subTargets.toString() + ", " + conditions.toString();
		if(weight != 0)
			reval += ", " + String.valueOf(weight);
		if(irrelevance != 0) 
			reval += ", " + String.valueOf(irrelevance);
		reval += ")";
		return reval;
	}
}
