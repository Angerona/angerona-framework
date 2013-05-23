package angerona.fw.knowhow;

import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.ELPAtom;

/**
 * Class represents a KnowhowStatement like the one defined in Thimm, Krümpelmann 2009.
 * @author Tim Janus
 *
 */
public class KnowhowStatement {
	
	/** the id of the knowhow-statement (useable as index) */
	private int id;
	
	/** the target of the knowhow represent as an elp atom. */
	private ELPAtom target;
	
	/** sub targets of the knowhow-statement, this might be skills or other knowhow statements */
	private Vector<ELPAtom> subTargets = new Vector<ELPAtom>();
	
	/** conditions which have to be true in the beliefbase of the agent */
	private Vector<ELPAtom> conditions = new Vector<ELPAtom>();
	
	/** internal name of the knowhow statement */
	String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	public KnowhowStatement(ELPAtom target, Vector<ELPAtom> subTargets, Vector<ELPAtom> conditions) {
		id = counter;
		name = "kh_stmt_"+id;
		++counter;
		
		this.target = target;
		this.subTargets.addAll(subTargets);
		this.conditions.addAll(conditions);
	}
	
	/** @return	the id of the knowhow-statement (useable as index) */
	public int getId() {
		return id;
	}
	
	public ELPAtom getTarget() {
		return target;
	}
	
	public Vector<ELPAtom> getSubTargets() {
		return subTargets;
	}
	
	public Vector<ELPAtom> getConditions() {
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
	public String toString() {
		return "(" + target.toString() + ", " + subTargets.toString() + ", " + conditions.toString() + ")";
	}
}
