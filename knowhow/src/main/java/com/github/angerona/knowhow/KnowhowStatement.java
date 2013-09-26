package com.github.angerona.knowhow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;

import com.github.angerona.fw.util.Utility;

/**
 * Class represents a KnowhowStatement like the one defined in Thimm, Krümpelmann 2009.
 * 
 * @author Tim Janus
 *
 */
public class KnowhowStatement implements Serializable {
	
	/** serial version id */
	private static final long serialVersionUID = -6842116356054584387L;

	/** the id of the knowhow-statement (useable as index) */
	private int id;
	
	/** the target of the knowhow represent as an elp atom. */
	private DLPAtom target;
	
	/** sub targets of the knowhow-statement, this might be skills or other knowhow statements */
	private List<DLPAtom> subTargets = new ArrayList<>();
	
	/** conditions which have to be true in the beliefbase of the agent */
	private List<DLPAtom> conditions = new ArrayList<>();
	
	/** the weight of this know-how statement */
	private double weight;
	
	/** 
	 * the irrelevance value of the know-how statement giving the subjective importance 
	 * of the sub-step encoded in the know-how statement
	 */
	private double irrelevance;
	
	/** internal name of the knowhow statement */
	private String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	public KnowhowStatement(KnowhowStatement other) {
		this(other.target.clone(), 
			Utility.cloneList(other.subTargets, DLPAtom.class), 
			Utility.cloneList(other.conditions, DLPAtom.class));
		
		if(!this.equals(other)) {
			this.equals(other);
		}
	}
	
	public KnowhowStatement(DLPAtom target, Collection<DLPAtom> subTargets, Collection<DLPAtom> conditions) {
		this(target, subTargets, conditions, 0, 0);
	}
		
	public KnowhowStatement(DLPAtom target, Collection<DLPAtom> subTargets, Collection<DLPAtom> conditions, 
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
	
	public String getName() {
		return name;
	}
	
	public DLPAtom getTarget() {
		return target;
	}
	
	
	public List<DLPAtom> getSubTargets() {
		return Collections.unmodifiableList(subTargets);
	}
	
	public List<DLPAtom> getConditions() {
		return Collections.unmodifiableList(conditions);
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getIrrelevance() {
		return irrelevance;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(other == this)	return true;
		if(other == null || other.getClass() != getClass()) return false;
		KnowhowStatement stmt = (KnowhowStatement)other;
		
		return 	target.equals(stmt.target) &&
				subTargets.equals(stmt.subTargets) &&
				conditions.equals(stmt.conditions) &&
				irrelevance == stmt.irrelevance &&
				weight == stmt.weight;
	}
	
	@Override
	public int hashCode() {
		return (target.hashCode() + subTargets.hashCode() + conditions.hashCode() + 
				new Double(weight).hashCode() + new Double(irrelevance).hashCode()) * 13;
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
	
	@Override
	public KnowhowStatement clone() {
		return new KnowhowStatement(this);
	}
}
