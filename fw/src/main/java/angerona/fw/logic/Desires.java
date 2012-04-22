package angerona.fw.logic;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import angerona.fw.internal.EntityAtomic;
import angerona.fw.internal.IdGenerator;

/**
 * The desire component of an Angerona Agent. The desires are a set of
 * formulas the agent want to become true. In the context of Angerona
 * they also a component of the Agent, so they are represent by a custom
 * class which implements the Entity interface.
 * 
 * @author Tim Janus
 */
public class Desires extends HashSet<Formula> implements EntityAtomic {

	/** kill warning */
	private static final long serialVersionUID = 3632568908513258322L;

	private Long id;
	
	private Long parent;
	
	public Desires(Long parent) {
		id = IdGenerator.generate(this);
		this.parent = parent;
	}
	
	public Desires(Desires other) {
		id = other.id;
		parent = other.parent;
		this.addAll(other);
	}
	
	@Override
	public Long getGUID() {
		return id;
	}

	@Override
	public Long getParent() {
		return parent;
	}

	@Override
	public List<Long> getChilds() {
		return new LinkedList<Long>();
	}
	
	@Override
	public Object clone() {
		return new Desires(this);
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Desires) {
			Desires dot = (Desires)other;
			return this.id.equals(dot.id);
		}
		return super.equals(other);
	}
}
