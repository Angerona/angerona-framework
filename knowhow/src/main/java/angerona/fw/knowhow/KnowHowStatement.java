package angerona.fw.knowhow;

import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;

public class KnowHowStatement {
	
	private Atom target;
	private Vector<Atom> subTargets = new Vector<Atom>();
	private Vector<Atom> conditions = new Vector<Atom>();
	
	public KnowHowStatement(Atom target, Vector<Atom> subTargets, Vector<Atom> conditions) {
		this.target = target;
		this.subTargets.addAll(subTargets);
		this.conditions.addAll(conditions);
	}
	
	@Override
	public String toString() {
		return "(" + target.toString() + ", " + subTargets.toString() + ", " + conditions.toString() + ")";
	}
}
