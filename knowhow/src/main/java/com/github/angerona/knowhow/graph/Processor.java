package com.github.angerona.knowhow.graph;

import com.github.angerona.knowhow.KnowhowStatement;


public class Processor extends GraphNodeAdapter {	
	/** serial version id */
	private static final long serialVersionUID = -5269593517151014379L;

	private KnowhowStatement statement;
	
	private String target;
	
	public Processor(String name) {
		super(name);
		target = name;
	}
	
	public Processor(KnowhowStatement stmt) {
		super("#"+stmt.getId() + " (" + stmt.getTarget().toString() + ") - (" + stmt.getConditions().toString() + ")");
		this.statement = stmt;
		this.target = stmt.getTarget().toString();
	}
	
	public boolean isAtomic() {
		return statement == null;
	}
	
	public String getTarget() {
		return target;
	}
	
	@Override
	public NodeType getType() {
		return NodeType.NT_PROCESSOR;
	}
	
	@Override
	public int hashCode() {
		return (this.name == null ? 0 : this.name.hashCode()) + 3;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof Processor))
			return false;
		
		Processor processor = (Processor)other;
		return this.name.equals(processor.name);
	}
}
