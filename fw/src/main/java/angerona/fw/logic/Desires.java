package angerona.fw.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.BaseAgentComponent;
import angerona.fw.Desire;

/**
 * The desire component of an Angerona Agent. The desires are a set of
 * formulas the agent want to become true. In the context of Angerona
 * they also a component of the Agent, so they are represent by a custom
 * class which implements the Entity interface.
 * 
 * @author Tim Janus
 */
public class Desires extends BaseAgentComponent {

	/** set of all desires */
	private Set<Desire> desires = new HashSet<Desire>();
	
	/** default ctor */
	public Desires() {}
	
	/** copy ctor, copies the content of other but shares the id */
	public Desires(Desires other) {
		super(other);
		desires.addAll(other.desires);
	}
	
	/** adds the given desire to the DesireComponent of the Agent */
	public boolean add(Desire desire) {
		return desires.add(desire);
	}
	
	public boolean remove(Desire desire) {
		// Normally the following line is not needed because set.remove(o) 
		// removes an element e with the condition o.equals(e) but strangly
		// this does not work with the given parameter.
		// only call return desires.remove(desire) will fail a unit test
		// (but I do not know why)
		Desire toRemove = getDesire(desire);
		return desires.remove(toRemove);
	}
	
	public Desire getDesire(Desire desire) {
		for(Desire des : desires) {
			if(desire.equals(des))
				return des;
		}
		return null;
	}
	
	public Desire getDesire(Atom twettyAtom) {
		for(Desire des : desires) {
			if(des.getAtom().equals(twettyAtom))
				return des;
		}
		return null;
	}
	
	public Set<Desire> getDesires() {
		return Collections.unmodifiableSet(desires);
	}
	
	public Set<Atom> getTweety() {
		Set<Atom> atoms = new HashSet<Atom>();
		for(Desire des : desires) {
			atoms.add(des.getAtom());
		}
		return atoms;
	}
	
	public Set<Desire> getDesiresByPredicate(Predicate pred) {
		Set<Desire> reval = new HashSet<Desire>();
		for(Desire d : desires) {
			if(d.getAtom().getPredicate().equals(pred)) {
				reval.add(d);
			}
		}
		return reval;
	}

	@Override
	public Object clone() {
		return new Desires(this);
	}
}
