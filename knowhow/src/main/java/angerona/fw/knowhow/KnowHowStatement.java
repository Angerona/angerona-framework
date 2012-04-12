package angerona.fw.knowhow;

import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;

/**
 * Class represents a KnowhowStatement like the one defined in Thimm, Krümpelmann 2009.
 * @author Tim Janus
 *
 */
public class KnowhowStatement {
	
	private Atom target;
	private Vector<Atom> subTargets = new Vector<Atom>();
	private Vector<Atom> conditions = new Vector<Atom>();
	
	/** internal name of the knowhow statement */
	String name;
	
	/** counter used for automatic name generation */
	private static int counter = 1;
	
	public KnowhowStatement(Atom target, Vector<Atom> subTargets, Vector<Atom> conditions) {
		name = "kh_stmt_"+counter;
		++counter;
		
		this.target = target;
		this.subTargets.addAll(subTargets);
		this.conditions.addAll(conditions);
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
