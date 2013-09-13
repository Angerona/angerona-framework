package com.github.angerona.knowhow.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Selector extends GraphNodeAdapter  {

	/** serial version id */
	private static final long serialVersionUID = -6949598700368734085L;

	public Selector(String name) {
		super(name);
	}
	
	public Collection<Processor> getProcessors(Collection<Processor> candidates) {
		Set<Processor> reval = new HashSet<>();
		boolean atomic = name.startsWith("s_");
		if(atomic) {
			int index = name.indexOf('(');
			String testName = null;
			if(index == -1) {
				testName = name.substring(2);
			} else {
				testName = name.substring(2, index);
			}
			for(Processor cur : candidates) {
				if(cur.isAtomic() && cur.getTarget().equals(testName)) {
					reval.add(cur);
				}
			}
		} else {
			for(Processor cur : candidates) {
				if(!cur.isAtomic() && cur.getTarget().equals(name)) {
					reval.add(cur);
				}
			}
		}
		return reval;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.NT_SELECTOR;
	}
	
	@Override
	public int hashCode() {
		return (name == null ? 0 : name.hashCode()) + 7;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof Selector))
			return false;
		
		Selector pn = (Selector)other;
		return this.name.equals(pn.name);
	}
}
