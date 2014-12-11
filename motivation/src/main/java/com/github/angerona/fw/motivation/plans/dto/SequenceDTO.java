package com.github.angerona.fw.motivation.plans.dto;

import java.util.Collection;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class SequenceDTO implements PlanDTO {

	protected String id;
	protected Collection<ActionDTO> act;

	public SequenceDTO() {}

	public SequenceDTO(String id, Collection<ActionDTO> act) {
		this.id = id;
		this.act = act;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<ActionDTO> getAct() {
		return act;
	}

	public void setAct(Collection<ActionDTO> act) {
		this.act = act;
	}

	@Override
	public String toString() {
		return "[" + id + ", " + act + "]";
	}

}
