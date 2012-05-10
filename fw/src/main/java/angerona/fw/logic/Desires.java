package angerona.fw.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseAgentComponent;

/**
 * The desire component of an Angerona Agent. The desires are a set of
 * formulas the agent want to become true. In the context of Angerona
 * they also a component of the Agent, so they are represent by a custom
 * class which implements the Entity interface.
 * 
 * @author Tim Janus
 */
public class Desires extends BaseAgentComponent {

	private Set<FolFormula> desires = new HashSet<FolFormula>();
	
	public Desires() {}
	
	public Desires(Desires other) {
		super(other);
		desires.addAll(other.desires);
	}
	
	public boolean add(FolFormula desire) {
		return desires.add(desire);
	}
	
	public boolean addAll(Collection<? extends FolFormula> elements) {
		return desires.addAll(elements);
	}
	
	public boolean remove(FolFormula desire) {
		return desires.remove(desire);
	}
	
	public Set<FolFormula> getTweety() {
		return Collections.unmodifiableSet(desires);
	}

	@Override
	public Object clone() {
		return new Desires(this);
	}
}
