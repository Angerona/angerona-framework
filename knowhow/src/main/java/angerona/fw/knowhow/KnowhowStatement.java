package angerona.fw.knowhow;

import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;

/**
 * Class represents a KnowhowStatement like the one defined in Thimm, Krümpelmann 2009.
 * @author Tim Janus
 *
 */
public class KnowhowStatement {
	
	/** the id of the knowhow-statement (useable as index) */
	private int id;
	
	/** the target of the knowhow represent as an elp atom. */
	private Atom target;
	
	/** sub targets of the knowhow-statement, this might be skills or other knowhow statements */
	private Vector<Atom> subTargets = new Vector<Atom>();
	
	/** conditions which have to be true in the beliefbase of the agent */
	private Vector<Atom> conditions = new Vector<Atom>();
	
	/** internal name of the knowhow statement */
	String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	public KnowhowStatement(Atom target, Vector<Atom> subTargets, Vector<Atom> conditions) {
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
	
	public Atom getTarget() {
		return target;
	}
	
	public Vector<Atom> getSubTargets() {
		return subTargets;
	}
	
	public Vector<Atom> getConditions() {
		return conditions;
	}
	
	@Override
	public String toString() {
		return "(" + target.toString() + ", " + subTargets.toString() + ", " + conditions.toString() + ")";
	}
}
