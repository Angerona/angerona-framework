package com.github.kreatures.knowhow;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.tweety.lp.asp.syntax.DLPAtom;

import com.github.kreatures.core.util.Utility;

/**
 * Class represents a KnowhowStatement similar to the one defined in 
 * Thimm, Krümpelmann 2009. But this version has also the parameters
 * weight and irrelevance that are introduced in the Diploma Thesis
 * "Resource-boundend Planning of Communication under Confidentiality
 * Constraints for BDI Agents".
 * 
 * @author Tim Janus
 */
public class KnowhowStatement implements Serializable, Cloneable {
	
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
	private List<Double> irrelevance;
	
	/** internal name of the knowhow statement */
	private String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	/**
	 * Copy-Ctor: Generates a copy of the given KnowhowStatement
	 * @param other	The KnowhowStatement that acts as source for the copy
	 */
	public KnowhowStatement(KnowhowStatement other) {
		target = other.target.clone();
		subTargets = Utility.cloneList(other.subTargets, DLPAtom.class);
		conditions = Utility.cloneList(other.conditions, DLPAtom.class);
		this.name = other.name;
		this.id = other.id;
		this.weight = other.weight;
		this.irrelevance = new ArrayList<>(other.irrelevance);
	}
	
	/**
	 * Ctor for the Thimm and Krümpelmann version of the know-how statement, the irrelevance and
	 * the weight will get a default value of zero.
	 * @param target		The target (goal) of the KnowhowStatement
	 * @param subTargets	The sub targets that have to be achieved to fulfill the KnowhowStatement
	 * @param conditions	The conditions that have to be fulfilled, otherwise the KnowhowStatement is not applicable
	 */
	public KnowhowStatement(DLPAtom target, List<DLPAtom> subTargets, List<DLPAtom> conditions) {
		this(target, subTargets, conditions, 0, null);
	}
	
	/**
	 * Ctor for generating a full qualified know-how statement
	 * @param target		The target (goal) of the KnowhowStatement
	 * @param subTargets	The sub targets that have to be achieved to fulfill the KnowhowStatement
	 * @param conditions	The conditions that have to be fulfilled, otherwise the KnowhowStatement is not applicable
	 * @param weight		The weight of the KnowhowStatement, if statements have the same target then those statements
	 * 						are preferred that have a higher weight.
	 * @param irrelevance	@todo change irrelevance
	 */
	public KnowhowStatement(DLPAtom target, List<DLPAtom> subTargets, List<DLPAtom> conditions, 
			double weight, List<Double> irrelevance) {
		id = counter;
		name = "kh_stmt_"+id;
		++counter;
		
		this.target = target;
		this.subTargets.addAll(subTargets);
		this.conditions.addAll(conditions);
		this.weight = weight;
		
		if(irrelevance == null) {
			this.irrelevance = new ArrayList<>();
			for(int i=0; i<this.subTargets.size(); ++i) {
				this.irrelevance.add(new Double(0));
			}
		} else {
			this.irrelevance = irrelevance;
		}
		
		if(this.irrelevance.size() != this.subTargets.size()) {
			throw new IllegalArgumentException("The lenght of the irrelevance must be equal to the lenght of the subgoals");
		}
	}
	
	/** @return	the unique id of the knowhow-statement (useable as index), a static ID counter is used to generate the id */
	public int getId() {
		return id;
	}
	
	/** @return the unique name of the know-how statement, a static ID counter is used to generate the name */
	public String getName() {
		return name;
	}
	
	/** @return the target (goal) of the know-how statement */
	public DLPAtom getTarget() {
		return target;
	}
	
	/** @return an unmodifiable list of the sub-targets of this know-how statement */
	public List<DLPAtom> getSubTargets() {
		return Collections.unmodifiableList(subTargets);
	}
	
	/** @return an unmodifiable list of the conditions of this know-how statement */
	public List<DLPAtom> getConditions() {
		return Collections.unmodifiableList(conditions);
	}
	
	/** @return the weight of this know-how statement */
	public double getWeight() {
		return weight;
	}
	
	/** @return the irrelevance of this know-how statement */
	public List<Double> getIrrelevance() {
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
				Utility.equals(irrelevance, stmt.irrelevance) &&
				weight == stmt.weight;
	}
	
	@Override
	public int hashCode() {
		return (target.hashCode() + subTargets.hashCode() + conditions.hashCode() + 
				new Double(weight).hashCode() + irrelevance.hashCode()) * 13;
	}
	
	@Override
	public String toString() {
		String reval = "(" + target.toString() + ", {";
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<subTargets.size(); ++i) {
			builder.append(subTargets.get(i).toString());
			builder.append("-");
			builder.append(irrelevance.get(i).toString());
			builder.append(", ");
		}		
		String inner = builder.toString();
		
		reval += inner.substring(0, inner.length()-2) + "}, " + conditions.toString();
		if(weight != 0)
			reval += ", " + String.valueOf(weight);
		if(!irrelevance.isEmpty())
			reval += ", " + String.valueOf(irrelevance);
		reval += ")";
		return reval;
	}
	
	@Override
	public KnowhowStatement clone() {
		return new KnowhowStatement(this);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.target);
		out.writeObject(this.subTargets);
		out.writeObject(this.conditions);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.target = (DLPAtom)in.readObject();
		// this casts produce the warnings but are legal as long as the writeObject()
		// method is in-sync with this readObject method
		this.subTargets = (List<DLPAtom>)in.readObject();
		this.conditions = (List<DLPAtom>)in.readObject();
	}
}
